package org.capoz.TOTPGenerator.logic;
import org.capoz.TOTPGenerator.interfaces.*;
import java.util.Calendar;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTPcompute implements GenerateTOTP{

	public String compute(String secretKey) {

		int epochTime = getParsedEpochTime();
		String encodedEpochTime = Integer.toString(epochTime);
		
		String digested = this.calcHmacSha256(secretKey, encodedEpochTime);
	
		int offSet = Integer.parseInt(digested.substring(digested.length()-1), 16);
		
		String digestedRes = digested.substring(offSet - (offSet % 2), offSet - (offSet % 2) + 8 );
		int totp = (int) (Long.parseLong(digestedRes, 16) % Math.pow(10 , 6));
		String totpString = Integer.toString(totp);
		if (totpString.length() == 5) {
			totpString = "0".concat(totpString);
		}
		return totpString;
	}

	public static int getParsedEpochTime() {

		long lresTime = 0;
		int iresTime = 0;

		lresTime = Calendar.getInstance().getTimeInMillis() /1000;

		iresTime = (int) lresTime / 30;

		return iresTime;

	}

	public String calcHmacSha256(String secretKey, String message) {
		String res = "";
		byte[] hmacSha256 = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
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
