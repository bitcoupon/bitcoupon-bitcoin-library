package bitcoupon;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECDomainParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class contains static functions that is used inside the BitCoupon library. These are functions that handles
 * basic functionality of the Bitcoin protocol.
 */
public class Bitcoin {

  private static final char[]
      BASE58ALPHABET =
      "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
  private static final byte[]
      BASE58VALUES =
      {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,
       -1, -1, -1, -1, -1, -1, -1, 9, 10, 11, 12, 13, 14, 15, 16, -1, 17, 18, 19, 20, 21, -1, 22, 23, 24, 25, 26, 27,
       28, 29, 30, 31, 32, -1, -1, -1, -1, -1, -1, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, -1, 44, 45, 46, 47, 48,
       49, 50, 51, 52, 53, 54, 55, 56, 57, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

  private static final ECDomainParameters EC_PARAMS;

  static {
    X9ECParameters params = SECNamedCurves.getByName("secp256k1");
    EC_PARAMS =
        new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH(), params.getSeed());
  }

  /**
   * This function generates a private key that can be used for signing transactions. From a private key a public key
   * and an address can be generated.
   *
   * @return A private key in Base58 representation.
   */
  public static String generatePrivateKey() {
    byte[] secret = new byte[32];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(secret);
    byte[] data = new byte[33];
    data[0] = (byte) 0x80;
    System.arraycopy(secret, 0, data, 1, 32);
    return encodeBase58(data);
  }

  /**
   * This function converts a private key from base58 representation to a BigInteger. The BigInteger representation is
   * used for signing operations while the base58 representation is easier to handle by humans as it is readable.
   *
   * @param encodedPrivateKey Base58 representation of the private key that is going to be converted.
   * @return BigInteger representation of the private key.
   */
  public static BigInteger decodePrivateKey(String encodedPrivateKey) {
    byte[] data = decodeBase58(encodedPrivateKey);
    if (data != null && data.length == 33 && (data[0] & 0xff) == 0x80) {
      byte[] decodedPrivateKey = new byte[32];
      System.arraycopy(data, 1, decodedPrivateKey, 0, 32);
      return new BigInteger(1, decodedPrivateKey);
    }
    throw new IllegalArgumentException();
  }

  /**
   * This function generates the public key corresponding to the private key that is given as argument. The public key
   * is used to verify signatures created with the private key.
   *
   * @param privateKey The private key for which a corresponding public key should be generated.
   * @return The public corresponding to the private key given as argument.
   */
  public static byte[] generatePublicKey(BigInteger privateKey) {
    return EC_PARAMS.getG().multiply(privateKey).getEncoded();
  }

  /**
   * This function generates a BitCoupon address from a public key given as argument. The address is used when creating
   * transactions and is in base58 as it is readable for humans.
   *
   * @param publicKey Public key to generate address from.
   * @return BitCoupon address generated from the public key given as argument.
   */
  public static String publicKeyToAddress(byte[] publicKey) {
    byte[] hashedPublicKey = hash160(publicKey);
    byte[] data = new byte[hashedPublicKey.length + 1];
    System.arraycopy(hashedPublicKey, 0, data, 1, hashedPublicKey.length);
    return encodeBase58(data);
  }

  /**
   * This function calculates the SHA256-hash of the SHA256-hash of the input to this function. This hash function is
   * used for many things in the BitCoupon protocol. One of them are to calculate a hash value of a transaction. It is
   * the hash value of a transaction that is signed when a new transaction is created.
   *
   * @param bytes Data to calculate hash from.
   * @return Double SHA256-hash of the input data.
   */
  public static byte[] hash256(byte[] bytes) {
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

  /**
   * This function calculates the RIPEMD160-hash of the SHA256-hash of the input to this function. This hash function is
   * used to generate a Bitcoupon address from a public key.
   *
   * @param bytes Data to calculate hash from.
   * @return RIPEMD160-hash of SHA256-hash of the input data.
   */
  public static byte[] hash160(byte[] bytes) {
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

  /**
   * This function converts data from a byte array to base58 representation. Base58 representation is used when humans
   * need to handle data as it is readable and contains a checksum to prevent reading/typing mistakes.
   *
   * @param data Data that should be converted to base58 representation.
   * @return Base58 representation of the input data.
   */
  public static String encodeBase58(byte[] data) {
    byte[] checksum = hash256(data);
    byte[] bytes = new byte[data.length + 4];
    System.arraycopy(data, 0, bytes, 0, data.length);
    System.arraycopy(checksum, 0, bytes, data.length, 4);
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

  /**
   * This function converts data from base58 representation to a byte array. Base58 representation is used when humans
   * need to handle data as it is readable and contains a checksum to prevent reading/typing mistakes.
   *
   * @param string Base58 string that should be converted to a byte array.
   * @return Byte array with the data from the input string.
   */
  public static byte[] decodeBase58(String string) {
    if (string == null) {
      throw new IllegalArgumentException();
    }
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
    if (bytes.length < 4) {
      throw new IllegalArgumentException();
    }
    byte[] data = new byte[bytes.length - 4];
    System.arraycopy(bytes, 0, data, 0, data.length);
    byte[] checksum = hash256(data);
    for (int i = 0; i < 4; i++) {
      if (checksum[i] != bytes[data.length + i]) {
        throw new IllegalArgumentException();
      }
    }
    return data;
  }

  /**
   * This function converts an int to a byte array representation. This is used when calculating hash values for
   * transactions.
   *
   * @param i Int to get byte array representation for.
   * @return Byte array representation of the input int.
   */
  public static byte[] intToByteArray(int i) {
    return new byte[]{(byte) (i >> 24 & 0xff), //
                      (byte) (i >> 16 & 0xff), //
                      (byte) (i >> 8 & 0xff), //
                      (byte) (i >> 0 & 0xff) //
    };
  }

  /**
   * This function converts a long to a byte array representation. This is used when calculating hash values for
   * transactions.
   *
   * @param l Long to get byte array representation for.
   * @return Byte array representation of the input long.
   */
  public static byte[] longToByteArray(long l) {
    return new byte[]{(byte) (l >> 56 & 0xff), //
                      (byte) (l >> 48 & 0xff), //
                      (byte) (l >> 40 & 0xff), //
                      (byte) (l >> 32 & 0xff), //
                      (byte) (l >> 24 & 0xff), //
                      (byte) (l >> 16 & 0xff), //
                      (byte) (l >> 8 & 0xff), //
                      (byte) (l >> 0 & 0xff) //
    };
  }

}
