package CHILD.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelIS
{
	private POIFSFileSystem FileSystem;
	private HSSFWorkbook Wookbook;

	public void IniVariable(String xlsFile) throws IOException
	{
		FileInputStream fileIS = new FileInputStream(xlsFile);
		FileSystem = new POIFSFileSystem(fileIS);
		Wookbook = new HSSFWorkbook(FileSystem);
		fileIS.close();
	}

	public int GetWorkbookSize()
	{
		if (Wookbook == null)
			return 0;
		return Wookbook.getNumberOfSheets();
	}

	public String GetSheetName(int wookboox_index)
	{
		if (Wookbook == null)
			return null;
		return Wookbook.getSheetName(wookboox_index);
	}

	public ArrayList<String> ExtractStringInfo(int wookboox_index)
	{
		if (Wookbook == null)
			return null;
		HSSFSheet sheet = Wookbook.getSheetAt(wookboox_index);
		if (sheet == null)
			return null;
		int row_size = sheet.getPhysicalNumberOfRows();
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < row_size; i++)
		{
			HSSFRow row = sheet.getRow(i);
			if (row == null)
				continue;
			short f = row.getFirstCellNum();
			short l = row.getLastCellNum();
			for (short j = f; j < l; j++)
			{
				HSSFCell cell = row.getCell(j);
				if (cell == null)
					continue;
				String value = cell.getStringCellValue();
				result.add(value);
			}
		}
		return result;
	}
}