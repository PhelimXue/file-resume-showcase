package org.phelim.model;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5CheckSum {
	private String hex;

	public MD5CheckSum(File file) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(file);

		byte[] dataBytes = new byte[1024];

		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}

		byte[] mdbytes = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		this.hex = sb.toString();
	}

	public String getHex() {
		return hex;
	}
}
