package bitcoupon;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi;
import org.bouncycastle.jcajce.provider.digest.RIPEMD160;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Bitcoin {

  private static final char[]
      BASE58ALPHABET =
      "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

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
    BigInteger base = new BigInteger("58");
    BigInteger zero = new BigInteger("0");
    String string = "";
    while (!value.equals(zero)) {
      BigInteger[] divideAndRemainder = value.divideAndRemainder(base);
      BigInteger quotient = divideAndRemainder[0];
      BigInteger remainder = divideAndRemainder[1];
      value = quotient;
      string = BASE58ALPHABET[remainder.intValue()] + string;
    }
    for (int i = 0; i < bytes.length && bytes[i] == 0; i++) {
      string = BASE58ALPHABET[0] + string;
    }
    return string;
  }

}
