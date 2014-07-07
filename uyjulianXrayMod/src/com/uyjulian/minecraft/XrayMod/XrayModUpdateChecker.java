package com.uyjulian.minecraft.XrayMod;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class XrayModUpdateChecker implements Runnable {
	private final String UpdateURL = "http://pastebin.com/raw.php?i=iFGwDPR6";
	private final String CurrentVersion = "7";
	@Override
	public void run() {
		Boolean foundNewVersion = false;
		while (foundNewVersion == false) {
			InputStream currentInputStream = null;
			ByteArrayOutputStream currentByteArrayOutputStream = null;
			try {
				Thread.sleep(4000L);
				URL currentURL = new URL(UpdateURL);
				currentInputStream = currentURL.openStream();
				currentByteArrayOutputStream = new ByteArrayOutputStream();
				copy(currentInputStream, currentByteArrayOutputStream);
				String NewVersion = new String(currentByteArrayOutputStream.toByteArray()).trim();
				if (!(NewVersion.equals(CurrentVersion))) {
					UyjuliansXrayModMain.putLineInChat("Update detected, please go to bit.ly/x-ray-mod to retrive the latest version of this mod.");
					UyjuliansXrayModMain.putLineInChat("Current: " + CurrentVersion);
					UyjuliansXrayModMain.putLineInChat("Updated: " + NewVersion);
					foundNewVersion = true;
				}
			}
			catch (Exception currentException) {
				currentException.printStackTrace();
			}
			finally {
				closeQuietly(currentInputStream);
			}
		}
	}
	
	private static long copy(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException
	{
		byte[] arrayOfByte = new byte[4096];
		long l = 0L;
		int i = 0;
		while ((i = paramInputStream.read(arrayOfByte)) != -1)
		{
			paramOutputStream.write(arrayOfByte, 0, i);
			l += i;
		}
		return l;
	}

	private static void closeQuietly(Closeable paramCloseable)
	{
		try
		{
			if (paramCloseable != null)
				paramCloseable.close();
		}
		catch (Exception currentException){}
	}

}
