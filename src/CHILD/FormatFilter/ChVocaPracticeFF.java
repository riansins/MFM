package CHILD.FormatFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import CHILD.File.TextIS;

public class ChVocaPracticeFF extends IFileFormatFilter
{
	@Override
	public Boolean Parse(File file)
	{
		ArrayList<String> result = new ArrayList<String>();
		String path = file.getAbsolutePath();
		TextIS tis = new TextIS();
		String text = tis.GetText(path);

		if (text == null || text == "")
			return false;

		Pattern patten = Pattern.compile("∙([一-龠，。、？]+)");
		Matcher matches = patten.matcher(text);

		while (matches.find())
		{
			String s = matches.group();
			result.add(s);
		}
		_Data = result;

		if (_Data.size() == 0)
			return false;
		else
			return true;
	}
}
