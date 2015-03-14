package CHILD.Window;

import java.awt.EventQueue;

public class App
{
    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
	EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		try
		{
		    MainView window = new MainView();
		    window.setVisible(true);
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	});
    }
}
