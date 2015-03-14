package CHILD.File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextIS
{
	public static String GetText(String file_path)
	{
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		File file = new File(file_path);
		String temp = null;
		StringBuilder content = new StringBuilder();
		try
		{
			// 파일을 읽어들여 File Input 스트림 객체 생성
			fis = new FileInputStream(file);
			// File Input 스트림 객체를 이용해 Input 스트림 객체를 생서하는데 인코딩을 UTF-8로 지정
			isr = new InputStreamReader(fis, "UTF-8");
			// Input 스트림 객체를 이용하여 버퍼를 생성
			br = new BufferedReader(isr);
			// 버퍼를 한줄한줄 읽어들여 내용 추출
			while ((temp = br.readLine()) != null)
			{
				content.append(temp);
				content.append('\n');
			}
			System.out.println("================== 파일 내용 출력 ==================");
			System.out.println(content);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			try
			{
				isr.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return content.toString();
	}
}
