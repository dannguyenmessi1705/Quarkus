package org.didan.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {
  private static String generateSalt(){
    byte[] salt = new byte[16];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    return bytesToHex(salt);
  }

  private static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(salt.getBytes());
    byte[] hashedBytes = digest.digest(password.getBytes());
    return bytesToHex(hashedBytes);
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b: bytes) {
      String hex = Integer.toHexString(0xff & b);
      if(hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public static String encodePassword(String password) throws NoSuchAlgorithmException {
    String salt = generateSalt();
    String hashPassword = hashPassword(password, salt);
    return hashPassword + "." + salt;
  }
}
