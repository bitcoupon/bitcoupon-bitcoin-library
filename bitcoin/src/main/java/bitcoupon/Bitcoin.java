package bitcoupon;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

import java.math.BigInteger;

public class Bitcoin {

  private static final char[]
      BASE58ALPHABET =
      "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
  private static final byte[]
      BASE58VALUES =
      {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, -1, -1, -1, -1, -1,
       -1, 9, 10, 11, 12, 13, 14, 15, 16, -1, 17, 18, 19, 20, 21, -1,
       22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, -1, -1, -1, -1, -1,
       -1, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, -1, 44, 45, 46,
       47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

  static byte[] hash256(byte[] bytes) {
    SHA256Digest firstDigest = new SHA256Digest();
    firstDigest.update(bytes, 0, bytes.length);
    byte[] firstHash = new byte[32];
    firstDigest.doFinal(firstHash, 0);
    SHA256Digest secondDigest = new SHA256Digest();
    secondDigest.update(firstHash, 0, firstHash.length);
    byte[] secondHash = new byte[32];
    secondDigest.doFinal(secondHash, 0);
    return secondHash;
  }

  static byte[] hash160(byte[] bytes) {
    SHA256Digest firstDigest = new SHA256Digest();
    firstDigest.update(bytes, 0, bytes.length);
    byte[] firstHash = new byte[32];
    firstDigest.doFinal(firstHash, 0);
    RIPEMD160Digest secondDigest = new RIPEMD160Digest();
    secondDigest.update(firstHash, 0, firstHash.length);
    byte[] secondHash = new byte[20];
    secondDigest.doFinal(secondHash, 0);
    return secondHash;
  }

  static String getAddress(String publicKey) {
    try {
      byte[] bPublicKey = Hex.decodeHex(publicKey.toCharArray());
      byte[] bHash = hash160(bPublicKey);
      byte[] bExtendedHash = new byte[bHash.length + 1];
      bExtendedHash[0] = 0;
      System.arraycopy(bHash, 0, bExtendedHash, 1, bHash.length);
      byte[] bChecksum = hash256(bExtendedHash);
      byte[] bAddress = new byte[25];
      System.arraycopy(bExtendedHash, 0, bAddress, 0, bExtendedHash.length);
      System.arraycopy(bChecksum, 0, bAddress, bExtendedHash.length, 4);
      String address = encodeBase58(bAddress);
      return address;
    } catch (DecoderException e) {
      e.printStackTrace();
      return null;
    }
  }

  static String encodeBase58(byte[] bytes) {
    BigInteger value = new BigInteger(1, bytes);
    BigInteger base = BigInteger.valueOf(58);
    BigInteger zero = BigInteger.valueOf(0);
    String string = "";
    while (!value.equals(zero)) {
      BigInteger[] divideAndRemainder = value.divideAndRemainder(base);
      value = divideAndRemainder[0];
      BigInteger remainder = divideAndRemainder[1];
      string = BASE58ALPHABET[remainder.intValue()] + string;
    }
    for (int i = 0; i < bytes.length && bytes[i] == 0; i++) {
      string = BASE58ALPHABET[0] + string;
    }
    return string;
  }

  static byte[] decodeBase58(String string) {
    BigInteger value = BigInteger.valueOf(0);
    BigInteger base = BigInteger.valueOf(58);
    for (int i = 0; i < string.length(); i++) {
      value = value.multiply(base);
      byte digit = BASE58VALUES[string.charAt(i)];
      value = value.add(BigInteger.valueOf(digit));
    }
    byte[] bValue = value.toByteArray();
    int zeros = 0;
    for (int i = 0; i < string.length() && string.charAt(i) == '1'; i++) {
      zeros++;
    }
    byte[] bytes;
    if (bValue[0] == 0) {
      bytes = new byte[bValue.length - 1 + zeros];
      System.arraycopy(bValue, 1, bytes, zeros, bValue.length - 1);
    } else {
      bytes = new byte[bValue.length + zeros];
      System.arraycopy(bValue, 0, bytes, zeros, bValue.length);
    }
    return bytes;
  }

  static byte[] intToByteArray(int i) {
    return new byte[]{
        (byte) ((i >> 24) & 0xff),
        (byte) ((i >> 16) & 0xff),
        (byte) ((i >> 8) & 0xff),
        (byte) ((i >> 0) & 0xff)
    };
  }

  static byte[] longToByteArray(long l) {
    return new byte[]{
        (byte) ((l >> 56) & 0xff),
        (byte) ((l >> 48) & 0xff),
        (byte) ((l >> 40) & 0xff),
        (byte) ((l >> 32) & 0xff),
        (byte) ((l >> 24) & 0xff),
        (byte) ((l >> 16) & 0xff),
        (byte) ((l >> 8) & 0xff),
        (byte) ((l >> 0) & 0xff)
    };
  }

}
