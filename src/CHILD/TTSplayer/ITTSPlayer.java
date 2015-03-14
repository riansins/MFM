package CHILD.TTSplayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import CHILD.Enum.TTSPlayerState;
import CHILD.FormatFilter.IFileFormatFilter;
import CHILD.HttpProtocol.HttpProtocol;

public abstract class ITTSPlayer extends HttpProtocol
{
	protected IFileFormatFilter _FFilter;
	protected int _trackIdx = 0;
	protected PlayEndedListener _PlayEndedListener;

	public ITTSPlayer(IFileFormatFilter fff)
	{
		_FFilter = fff;
	}

	public void PlayAudio(String url)
	{
	}

	public abstract TTSPlayerState NextTTSPlay();

	public abstract TTSPlayerState TTSPlay(int idx);

	public void AddPlayEndedListener(PlayEndedListener listener)
	{
		_PlayEndedListener = listener;
	}

	public IFileFormatFilter GetTextData()
	{
		return _FFilter;
	}
}