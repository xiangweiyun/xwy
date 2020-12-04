package com.xwy.common.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 密码盐渍算法工具类,生成70个字符的密码hash,可以调整SALT_BYTE_SIZE,HASH_BYTE_SIZE来改变
 *
 * <br>
 * how to use:
 *
 * <pre>
 * String password = &quot;123456&quot;;
 * boolean success = PasswordHash.validatePassword(password, PasswordHash.createHash(password));
 * </pre>
 **/
public class PasswordHash {
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	// The following constants may be changed without breaking existing hashes.
	public static final int SALT_BYTE_SIZE = 16;
	public static final int HASH_BYTE_SIZE = 16;
	public static final int PBKDF2_ITERATIONS = 1000;

	public static final int ITERATION_INDEX = 0;
	public static final int SALT_INDEX = 1;
	public static final int PBKDF2_INDEX = 2;

	public static final String SEPARATOR = ":";

	/**
	 * 加盐处理密码,返回处理后的hash
	 *
	 * @param password
	 * @return 加盐处理后的hash
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return createHash(password.toCharArray());
	}

	/**
	 * 加盐处理密码,返回处理后的hash
	 *
	 * @param password
	 * @return 加盐处理后的hash
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Generate a random salt
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTE_SIZE];
		random.nextBytes(salt);

		// Hash the password
		byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
		// format iterations:salt:hash
		return PBKDF2_ITERATIONS + SEPARATOR + toHex(salt) + SEPARATOR + toHex(hash);
	}

	/**
	 * 验证密码与 盐渍hash 是否匹配
	 * <p>
	 * return true 表示匹配,反之则false
	 * </p>
	 *
	 * @param password
	 * @param correctHash
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static boolean validatePassword(String password, String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return validatePassword(password.toCharArray(), correctHash);
	}

	/**
	 * 验证密码与 盐渍hash 是否匹配
	 * <p>
	 * return true 表示匹配,反之则false
	 * </p>
	 *
	 * @param password
	 * @param correctHash
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static boolean validatePassword(char[] password, String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Decode the hash into its parameters
		String[] params = correctHash.split(SEPARATOR);
		int iterations = Integer.parseInt(params[ITERATION_INDEX]);
		byte[] salt = fromHex(params[SALT_INDEX]);
		byte[] hash = fromHex(params[PBKDF2_INDEX]);
		// Compute the hash of the provided password, using the same salt,
		// iteration count, and hash length
		byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return slowEquals(hash, testHash);
	}

	/**
	 * md5密码验证
	 * 
	 * @param password
	 * @param oldPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean validateOldPassword(String password, String oldPassword) throws NoSuchAlgorithmException {

		String md5Password = MD5Util.string2MD5(password).toUpperCase();
		if (md5Password.equals(oldPassword)) {
			return true;
		}
		return false;
	}

	/**
	 * Compares two byte arrays in length-constant time. This comparison method is
	 * used so that password hashes cannot be extracted from an on-line system using
	 * a timing attack and then attacked off-line.
	 *
	 * @param a the first byte array
	 * @param b the second byte array
	 * @return true if both byte arrays are the same, false if not
	 */
	private static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}

	/**
	 * Computes the PBKDF2 hash of a password.
	 *
	 * @param password   the password to hash.
	 * @param salt       the salt
	 * @param iterations the iteration count (slowness factor)
	 * @param bytes      the length of the hash to compute in bytes
	 * @return the PBDKF2 hash of the password
	 */
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
		return skf.generateSecret(spec).getEncoded();
	}

	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 *
	 * @param hex the hex string
	 * @return the hex string decoded into a byte array
	 */
	private static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	/**
	 * Converts a byte array into a hexadecimal string.
	 *
	 * @param array the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	// 测试
	public static void main(String[] args) {
		try {
			// Print out 10 hashes
			for (int i = 0; i < 10; i++) {
				System.out.println(PasswordHash.createHash("p\r\nassw0Rd!"));
			}

			String p1 = PasswordHash.createHash("111111");
			String p2 = createHash("111111");
			if (p1.equals(p2)) {
				System.out.println("check password success!");
			} else {
				System.out.println("p1:=" + p1);
				System.out.println("P2:=" + p2);
			}

			// Test password validation
			boolean failure = false;
			System.out.println("Running tests...");
			for (int i = 0; i < 100; i++) {
				String password = "" + i;
				String hash = createHash(password);
				String secondHash = createHash(password);
				if (hash.equals(secondHash)) {
					System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
					failure = true;
				}
				String wrongPassword = "" + (i + 1);
				if (validatePassword(wrongPassword, hash)) {
					System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
					failure = true;
				}
				if (!validatePassword(password, hash)) {
					System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
					failure = true;
				}
			}
			if (failure) {
				System.out.println("TESTS FAILED!");
			} else {
				System.out.println("TESTS PASSED!");
			}
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex);
		}

		String pwd = "111111";
		try {
			String dbpwd = PasswordHash.createHash(pwd);
			System.out.println(dbpwd);
			dbpwd = "1000:30c95036fc29ef8470d78887008d439d:01b4ab0b83c4fa8e87d42b206e7dbb16";
			boolean success = PasswordHash.validatePassword(pwd, dbpwd);
			if (success) {
				System.out.println("successed!");
			} else {
				System.out.println("password error!");
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
