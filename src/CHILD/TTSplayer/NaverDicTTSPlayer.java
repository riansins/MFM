package CHILD.TTSPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;

import maryb.player.PlayerEventListener;
import CHILD.Debug.D;
import CHILD.Enum.TTS_ERROR;
import CHILD.FormatFilter.IFileFormatFilter;

public class NaverDicTTSPlayer extends ITTSPlayer
{
	private ArrayList<Pair> _VoicePaths;
	private int _VoicePathsIdx;
	private PlayerEventListener _PlayerEventListener = new PlayerEventListener()
	{
		@Override
		public void stateChanged()
		{
		}

		@Override
		public void endOfMedia()
		{
			if (_VoicePaths == null)
				return;
			if (!(0 <= _VoicePathsIdx && _VoicePathsIdx < _VoicePaths.size()))
				return;
			String file_path = _VoicePaths.get(_VoicePathsIdx++).Value;
			if (file_path != null && !file_path.isEmpty())
			{
				PlayAudio(file_path);
			}
			D.Log("Call " + file_path);
		}

		@Override
		public void buffer()
		{
		}
	};

	private class Pair
	{
		public String Key;
		public String Value;

		public Pair(String key, String value)
		{
			Key = key;
			Value = value;
		}
	}

	// 폴더 만드는 와중에 생길 수 있는 에러 체크
	public NaverDicTTSPlayer(IFileFormatFilter fff) throws IOException
	{
		super(fff);
	}

	@Override
	public void NextTTSPlay() throws TTSPlayerException
	{
		TTSPlay(_trackIdx++);
	}

	@Override
	public void TTSPlay(int idx) throws TTSPlayerException
	{
		String http_data = null;
		ArrayList<String> regex_ret;

		// text 파일에서 문장 가져오는 코드
		String sentence = QuerySentence(idx);
		// 문장을 네이버사전에서 검색하고 필요한 부분만 뺌
		ArrayList<Pair> lang_p = AnalysisSentence(sentence);
		// 문장에서 단어 단위로 뺀 뒤에 다시 해당 단어와 발음기호가 일치하는 http에서 voice url 을 검색
		ArrayList<Pair> voice_urls = QueryVoiceUrl(lang_p);
		// 다운로드 ..
		DonwloadMp3File(voice_urls);

		_VoicePaths = voice_urls;
		_VoicePathsIdx = 0;
		_Player.setListener(_PlayerEventListener);
		_PlayerEventListener.endOfMedia();
	}

	private ArrayList<Pair> AnalysisSentence(String sentence) throws TTSPlayerException
	{
		String http_data;
		ArrayList<String> regex_ret;
		try
		{
			http_data = GetNaverSearchingResult(sentence);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new TTSPlayerException(TTS_ERROR.HTTP_ERROR.getString());
		}

		regex_ret = RegexString("중국어 문장 분석 결과([\\W\\w]+?)<\\/ul>", http_data, 1);
		if (regex_ret == null || regex_ret.size() == 0)
			throw new TTSPlayerException(TTS_ERROR.REGEX_ERROR.getString());
		String regex_body = regex_ret.get(0);
		// HTTP 바디 부분에서 단어 : 발음기호 페어로 추출
		Pattern pattern = Pattern.compile("([一-龠，。、？《》!]+)[\\w\\W]+?<p>([\\w\\W]*?)<\\/p>");
		Matcher matcher = pattern.matcher(regex_body);
		ArrayList<Pair> lang_p = new ArrayList<Pair>();
		while (matcher.find())
			lang_p.add(new Pair(matcher.group(1), matcher.group(2)));
		return lang_p;
	}

	private String QuerySentence(int idx) throws TTSPlayerException
	{
		if (_FFilter == null)
			throw new TTSPlayerException(TTS_ERROR.MEMVER_VAR_NULL_ERROR.getString());

		ArrayList<String> array_str_data = _FFilter.GetData();

		if (!(0 <= idx && idx < array_str_data.size()))
			throw new TTSPlayerException(TTS_ERROR.INDEX_ENDED.getString());

		String sentence = array_str_data.get(idx);
		if (sentence.isEmpty() || sentence == null)
			throw new TTSPlayerException(TTS_ERROR.STRING_NULL_OR_EMTPY.getString());

		return sentence;
	}

	private ArrayList<Pair> QueryVoiceUrl(ArrayList<Pair> lang_p) throws TTSPlayerException
	{
		ArrayList<Pair> voice_urls = new ArrayList<Pair>();
		String http_data;
		ArrayList<String> regex_ret;
		for (int i = 0; i < lang_p.size(); i++)
		{
			String key = lang_p.get(i).Key;
			String value = lang_p.get(i).Value;

			if (key.compareTo("，") == 0 || key.compareTo("、") == 0)
			{
				voice_urls.add(new Pair("", ""));
				continue;
			}
			if (key.compareTo("。") == 0 || key.compareTo("？") == 0 || key.compareTo("《") == 0 || key.compareTo("》") == 0
					|| key.compareTo("!") == 0)
				continue;
			try
			{
				http_data = GetNaverSearchingResult(key);
				ArrayList<String> a1 = RegexString("단어 검색결과([\\w\\W]+?)<\\/script>", http_data, 1);
				if (a1 == null || a1.size() == 0)
					throw new TTSPlayerException(TTS_ERROR.REGEX_ERROR.getString());
				String script_str = a1.get(0);
				String regex_for_voice = String.format("%s[\\w\\W]+?\\[%s\\][\\w\\W]+?name=\"male\" value=\"([\\w\\W]+?)\"", key, value);
				regex_ret = RegexString(regex_for_voice, script_str, 1);
				if (regex_ret == null || regex_ret.size() == 0)
					throw new TTSPlayerException(TTS_ERROR.REGEX_ERROR.getString());
				String voice_url = regex_ret.get(0);
				ArrayList<String> ais = RegexString("vcode=([0-9]+)", voice_url, 1);
				String filename = _TempFile.getAbsolutePath() + File.separator + ais.get(0) + ".mp3";
				voice_urls.add(new Pair(voice_url, filename));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new TTSPlayerException(TTS_ERROR.HTTP_ERROR.getString());
			}
		}
		return voice_urls;
	}

	private void DonwloadMp3File(ArrayList<Pair> voice_urls) throws TTSPlayerException
	{
		for (int i = 0; i < voice_urls.size(); i++)
		{
			String voice_url = voice_urls.get(i).Key;
			String file_path = voice_urls.get(i).Value;
			if (voice_url == null || voice_url.isEmpty())
				continue;

			File f = new File(file_path);
			if (f.exists())
				f.delete();

			byte[] bytes = null;
			try
			{
				bytes = RequestUrl(voice_url, false);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
				throw new TTSPlayerException(TTS_ERROR.HTTP_ERROR.getString());
			}
			FileOutputStream stream = null;
			try
			{
				stream = new FileOutputStream(file_path);
				stream.write(bytes);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				throw new TTSPlayerException(TTS_ERROR.IO_STREAM_ERROR.getString());
			}
			finally
			{
				try
				{
					stream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					throw new TTSPlayerException(TTS_ERROR.IO_STREAM_ERROR.getString());
				}
			}
		}
	}

	private ArrayList<String> RegexString(String regex_str, String data_str, int groipIdx)
	{
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(regex_str);
		matcher = pattern.matcher(data_str);
		ArrayList<String> li = new ArrayList<String>();
		while (matcher.find())
		{
			String s = matcher.group(groipIdx);
			li.add(s);
		}
		return li;
	}

	private String GetNaverSearchingResult(String sentence) throws Exception
	{
		final String http_search_url_format = "http://cndic.naver.com/search/all?q=%s";
		String url = String.format(http_search_url_format, sentence);
		byte[] requestedData = RequestUrl(url, false);
		return new String(requestedData, 0, requestedData.length);
	}
}
