package CHILD.TTSplayer;

import java.util.ArrayList;

import CHILD.Enum.TTSPlayerState;
import CHILD.FormatFilter.IFileFormatFilter;

public class NaverDicTTSPlayer extends ITTSPlayer
{
	public NaverDicTTSPlayer(IFileFormatFilter fff)
	{
		super(fff);
	}

	@Override
	public TTSPlayerState NextTTSPlay()
	{
		return TTSPlayerState.PLAYING;
	}

	@Override
	public TTSPlayerState TTSPlay(int idx)
	{
		if (_FFilter == null)
			return TTSPlayerState.END;

		ArrayList<String> data = _FFilter.GetData();

		if (!(0 <= idx && idx < data.size()))
			return TTSPlayerState.END;
		
		
		

		return TTSPlayerState.PLAYING;
	}
}
