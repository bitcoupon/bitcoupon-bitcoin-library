package bitcoupon;

import junit.framework.TestCase;

import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.util.Arrays;

public class BitcoinTest extends TestCase {

  @Test
  public void test_hash256() {
    byte[] data1 = Hex.decode("d7 a9 65 2a 70 3a 68 16 e6 75");
    byte[] hash1 = Hex.decode("3e5d6d72cea8f9b9da08f537a7c95dd09984466da640e2be5a6ada4ac47bcbb6");
    assertTrue(Arrays.equals(hash1, Bitcoin.hash256(data1)));

    byte[] data2 = Hex.decode("f0 51 da 72 1f 09 5b 27 20 95 a6 11 38 ec 7b ca 03 63 ec 26");
    byte[] hash2 = Hex.decode("e6bd148c38029ff68a81581909c55df42b35492c9f347ba85d88bebd38633c8c");
    assertTrue(Arrays.equals(hash2, Bitcoin.hash256(data2)));

    byte[] data3 = null;
    try {
      Bitcoin.hash256(data3);
      fail();
    } catch (IllegalArgumentException e) {}
  }

  @Test
  public void test_hash160() {
    byte[] data1 = Hex.decode("d7 a9 65 2a 70 3a 68 16 e6 75");
    byte[] hash1 = Hex.decode("5c514fcb4bf779c97ccf1fc3a10b284ce99bcd0d");
    assertTrue(Arrays.equals(hash1, Bitcoin.hash160(data1)));

    byte[] data2 = Hex.decode("f0 51 da 72 1f 09 5b 27 20 95 a6 11 38 ec 7b ca 03 63 ec 26");
    byte[] hash2 = Hex.decode("f9f9116d218a7e0c11a15d9c5bf27008a3f79647");
    assertTrue(Arrays.equals(hash2, Bitcoin.hash160(data2)));

    byte[] data3 = null;
    try {
      Bitcoin.hash256(data3);
      fail();
    } catch (IllegalArgumentException e) {}
  }

  @Test
  public void test_encodeBase58() {
    byte[] bytes1 = Hex.decode("24 43 f2 f4");
    String string1 = "74pZgXk9mx2";
    assertEquals(string1, Bitcoin.encodeBase58(bytes1));

    byte[] bytes2 = Hex.decode("37 b6 64 ab");
    String string2 = "AKUtMRSJitc";
    assertEquals(string2, Bitcoin.encodeBase58(bytes2));

    byte[] byte3 = null;
    try {
      Bitcoin.encodeBase58(byte3);
      fail();
    } catch (IllegalArgumentException e) {}
  }

  @Test
  public void test_decodeBase58() {
    String string1 = "74pZgXk9mx2";
    byte[] bytes1 = Hex.decode("24 43 f2 f4");
    assertTrue(Arrays.equals(bytes1, Bitcoin.decodeBase58(string1)));

    String string2 = "AKUtMRSJitc";
    byte[] bytes2 = Hex.decode("37 b6 64 ab");
    assertTrue(Arrays.equals(bytes2, Bitcoin.decodeBase58(string2)));

    String string3 = "AKUtMRSJitd";
    try {
      Bitcoin.decodeBase58(string3);
      fail();
    } catch (IllegalArgumentException e) {}

    String string4 = "AKUtMRSJit0";
    try {
      Bitcoin.decodeBase58(string4);
      fail();
    } catch (IllegalArgumentException e) {}

    String string5 = null;
    try {
      Bitcoin.decodeBase58(string5);
      fail();
    } catch (IllegalArgumentException e) {}
  }

  @Test
  public void test_intToByteArray() {
    int int1 = 3;
    byte[] bytes1 = {0, 0, 0, 3};
    assertTrue(Arrays.equals(bytes1, Bitcoin.intToByteArray(int1)));

    int int2 = 67129232;
    byte[] bytes2 = {4, 0, 79, -112};
    assertTrue(Arrays.equals(bytes2, Bitcoin.intToByteArray(int2)));

    int int3 = -12;
    byte[] bytes3 = {-1, -1, -1, -12};
    assertTrue(Arrays.equals(bytes3, Bitcoin.intToByteArray(int3)));
  }

  @Test
  public void test_longToByteArray() {
    long long1 = 8L;
    byte[] bytes1 = {0, 0, 0, 0, 0, 0, 0, 8};
    assertTrue(Arrays.equals(bytes1, Bitcoin.longToByteArray(long1)));

    long long2 = 6135276234334L;
    byte[] bytes2 = {0, 0, 5, -108, 122, -11, -26, 94};
    assertTrue(Arrays.equals(bytes2, Bitcoin.longToByteArray(long2)));

    long long3 = -752L;
    byte[] bytes3 = {-1, -1, -1, -1, -1, -1, -3, 16};
    assertTrue(Arrays.equals(bytes3, Bitcoin.longToByteArray(long3)));
  }

}
