package org.capoz.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOPTcompute {
	
	

	public String compute(String secretKey) {

		int epochTime = getParsedEpochTime();
		String encodedEpochTime = Integer.toString(epochTime);
		
		String digested = this.calcHmacSha256(secretKey, encodedEpochTime);
	
		int offSet = Integer.parseInt(digested.substring(digested.length()-1), 16);
		
		String digestedRes = digested.substring(offSet - (offSet % 2), offSet - (offSet % 2) + 8 );
		int totp = (int) (Integer.parseInt(digestedRes, 16) % Math.pow(10 , 6));
		String totpString = Integer.toString(totp);
		if (totpString.length() == 5) {
			totpString = "0".concat(totpString);
		}
		return totpString;
	}

	public static int getParsedEpochTime() {

		long lresTime = 0;
		int iresTime = 0;

		Date today = Calendar.getInstance().getTime();

		// Constructs a SimpleDateFormat using the given pattern
		SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");

		// format() formats a Date into a date/time string.
		String currentTime = crunchifyFormat.format(today);

		try {

			// parse() parses text from the beginning of the given string to produce a date.
			Date date = crunchifyFormat.parse(currentTime);

			// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00
			// GMT represented by this Date object.
			lresTime = date.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}

		iresTime = (int) lresTime / 30;

		return iresTime;

	}

	public String calcHmacSha256(String secretKey, String message) {
		String res = "";
		byte[] hmacSha256 = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
			mac.init(secretKeySpec);
			hmacSha256 = mac.doFinal(message.getBytes("ASCII"));
		} catch (Exception e) {
			throw new RuntimeException("Failed to calculate hmac-sha256", e);
		}

		res = parseBytes(hmacSha256);

		return res;
	}

	private static String parseBytes(byte[] bytes) {
		String digest = "";
		StringBuffer hash = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				hash.append('0');
			}
			hash.append(hex);
		}
		digest = hash.toString();

		return digest;
	}

}
