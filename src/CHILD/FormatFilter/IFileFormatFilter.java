package CHILD.FormatFilter;

import java.io.File;
import java.util.ArrayList;

public abstract class IFileFormatFilter
{
	protected ArrayList<String> _Data;

	public ArrayList<String> GetData()
	{
		return _Data;
	}

	public abstract Boolean Parse(File file);
}