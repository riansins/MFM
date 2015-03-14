package CHILD.Window;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import CHILD.Debug.D;
import CHILD.FormatFilter.ChVocaPracticeFF;
import CHILD.FormatFilter.IFileFormatFilter;
import CHILD.TTSPlayer.NaverDicTTSPlayer;
import CHILD.TTSPlayer.TTSPlayerException;

public class MainView extends IBassView
{
	private JProgressBar _TTSProgressBar;

	private ActionListener _ListenerForTTSPlay;
	private ActionListener _ListenerForLookText;
	private ActionListener _ListenerForLoadFile;

	private File _TargetFile;
	private IFileFormatFilter _CurrentLoadedFF;

	MainView()
	{
		InitListeners();
		InitComponent();
	}

	private void PrepareTTS()
	{
		ChVocaPracticeFF cvp = new ChVocaPracticeFF();
		if (cvp.Parse(_TargetFile))
		{
			_CurrentLoadedFF = cvp;
			// 작업 계속
			try
			{
				NaverDicTTSPlayer ndtp = new NaverDicTTSPlayer(_CurrentLoadedFF);
				ndtp.TTSPlay(0);
			}
			catch (IOException | TTSPlayerException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void InitListeners()
	{
		_ListenerForLoadFile = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("파일을 선택해 주세요.");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					D.Log("getSelectedFile() : " + fileChooser.getSelectedFile());
					_TargetFile = fileChooser.getSelectedFile();
					PrepareTTS();
				}
				else
				{
					System.out.println("No Selection ");
				}
			}
		};
	}

	private ImageIcon CreateImageIcon(String path)
	{
		java.net.URL imgURL = getClass().getClassLoader().getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL);
		}
		else
		{
			D.Log("Image Read Error");
			return null;
		}
	}

	private JButton MakeImageButton(Icon resource_path_idle, Icon resource_path_hover)
	{
		JButton button = new JButton();
		button.setContentAreaFilled(false);
		button.setIcon(resource_path_idle);
		button.setRolloverIcon(resource_path_hover);
		button.setPressedIcon(resource_path_hover);
		button.setDisabledIcon(resource_path_idle);
		button.setBorder(BorderFactory.createEmptyBorder());
		return button;
	}

	protected void InitComponent()
	{
		getContentPane().setLayout(new BorderLayout(0, 0));

		Box horizontalBox = Box.createHorizontalBox();
		getContentPane().add(horizontalBox, BorderLayout.SOUTH);

		JButton btnPlay = new JButton("재생");
		btnPlay.addActionListener(_ListenerForTTSPlay);
		horizontalBox.add(btnPlay);

		JButton btnPause = new JButton("채점");
		btnPause.addActionListener(_ListenerForLookText);
		horizontalBox.add(btnPause);

		JButton btnLoad = new JButton("불러오기");
		btnLoad.addActionListener(_ListenerForLoadFile);
		horizontalBox.add(btnLoad);

		JProgressBar progressBar = new JProgressBar();
		getContentPane().add(progressBar, BorderLayout.NORTH);
		_TTSProgressBar = progressBar;
	}
}