/* Copyright (c) 2014-2017, Julian Uy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.uyjulian.minecraft.XrayMod;

import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XrayModUpdateChecker implements Runnable {
	private static String newVersion = "";
	@Override
	public void run() {
		while (newVersion.equals("")) {
			InputStream currentInputStream = null;
			ByteArrayOutputStream currentByteArrayOutputStream;
			try {
				String updateURL = "https://api.github.com/repos/uyjulian/MinecraftX-RAY/releases/latest";
				URL currentURL = new URL(updateURL);
				currentInputStream = currentURL.openStream();
				currentByteArrayOutputStream = new ByteArrayOutputStream();
				copy(currentInputStream, currentByteArrayOutputStream);
				Pattern pattern = Pattern.compile("\"tag_name\":\"(.+?)\"");
				String NewVersionJSON = new String(currentByteArrayOutputStream.toByteArray());

				Matcher matcher = pattern.matcher(NewVersionJSON);
				matcher.find();
				String NewVersion = matcher.group(1);

				if (!(NewVersion.equals(UyjuliansXrayModMain.currentVersion)) && (NewVersion.length() <= 100)) {
					newVersion = NewVersion;
				} else {
					Thread.sleep((1000 * 60) * 2);
				}
			}
			catch (Exception currentException) {
				currentException.printStackTrace();
				try {
					Thread.sleep((1000 * 60) * 2);
				} catch (InterruptedException ignored) {}
			}
			finally {
				closeQuietly(currentInputStream);
			}
		}
		UyjuliansXrayModMain.waitForPlayer();
		XrayModVersionUtils.putLineInChat("§c§lUpdate available§r: " + newVersion);
	}
	
	private static void copy(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException
	{
		byte[] arrayOfByte = new byte[4096];
		int i;
		while ((i = paramInputStream.read(arrayOfByte)) != -1)
		{
			paramOutputStream.write(arrayOfByte, 0, i);
		}
	}

	private static void closeQuietly(Closeable paramCloseable)
	{
		try
		{
			if (paramCloseable != null)
				paramCloseable.close();
		}
		catch (Exception ignored){}
	}
}
