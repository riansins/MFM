package CHILD.TTSPlayer;

import java.io.File;
import java.io.IOException;

import maryb.player.Player;
import maryb.player.PlayerEventListener;
import CHILD.Debug.D;
import CHILD.Enum.TTS_ERROR;
import CHILD.FormatFilter.IFileFormatFilter;
import CHILD.HttpProtocol.HttpProtocol;

public abstract class ITTSPlayer extends HttpProtocol
{
	protected IFileFormatFilter _FFilter;
	protected int _trackIdx = 0;
	protected PlayEndedListener _PlayEndedListener;
	protected File _TempFile;
	protected Player _Player = new Player();

	protected void InitTempFolder() throws IOException
	{
		_TempFile = File.createTempFile("mfm_temp", Long.toString(System.nanoTime()));
		_TempFile.delete();
		_TempFile.mkdir();
	}

	public ITTSPlayer(IFileFormatFilter fff) throws IOException
	{
		_FFilter = fff;
		InitTempFolder();
	}

	public void PlayAudio(String url)
	{
		_Player.setSourceLocation(url);
		_Player.play();
	}

	public abstract void NextTTSPlay() throws TTSPlayerException;

	public abstract void TTSPlay(int idx) throws TTSPlayerException;

	public void AddPlayEndedListener(PlayEndedListener listener)
	{
		_PlayEndedListener = listener;
	}

	public IFileFormatFilter GetTextData()
	{
		return _FFilter;
	}
}