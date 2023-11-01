package br.com.kingsdevs.helpers;

import java.security.MessageDigest;

public class MD5Helper {

	private static MessageDigest messageDigest = null;
	
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static char[] hexCodes(byte[] text) {
		char[] hexOutput = new char[text.length * 2];
		String hexString;
		for (int i = 0; i < text.length; i++) {
			hexString = "00" + Integer.toHexString(text[i]);

			hexString.toUpperCase().getChars(hexString.length() - 2, hexString.length(), hexOutput, i * 2);
		}
		return hexOutput;
	}

	public static String encrypt(String value) {
		if (value != null) {
			return new String(hexCodes(messageDigest.digest(value.getBytes()))).toLowerCase();
		}
		return null;
	}

}
