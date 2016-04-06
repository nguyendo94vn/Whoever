package v.whoever.service.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import vn.whoever.service.GenerateToken;

public class GenerateTokenImpl implements GenerateToken, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 18487378329292L;

	private GenerateTokenImpl() {}
	
	private static GenerateToken tokenImpl = new GenerateTokenImpl();
	
	public static void main(String[] args) {
		System.out.println(GenerateTokenImpl.getToken().getTokenId("nguyendo"));
	}
	
	public synchronized static GenerateToken getToken() {
		return tokenImpl;
	}
	
	public synchronized String getTokenId(String ssoId) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] data = (ssoId + (new Date()).getTime()).getBytes();
			
			byte[] result = md.digest(data);
			BigInteger number = new BigInteger(1, result);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            char[] arrToken = hashtext.toCharArray();
            arrToken[7] = '-';
            arrToken[11] = '-';
            arrToken[15] = '-';
            arrToken[19] = '-';
            return String.copyValueOf(arrToken);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
