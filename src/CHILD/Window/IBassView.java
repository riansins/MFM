package CHILD.Window;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import CHILD.Enum.MFM;

public abstract class IBassView extends JFrame
{
	protected IBassView()
	{
		InitLookAndFeel();
		InitFrame();
	}

	protected void InitLookAndFeel()
	{
		System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
		try
		{
			UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
	}

	protected void InitFrame()
	{
		this.setSize(MFM.WIN_SIZE_WIDTH, MFM.WIN_SIZE_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(MFM.APP_NAME);
		this.setLocationRelativeTo(null);
	}

	abstract protected void InitComponent();
}
