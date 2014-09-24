package bitcoupon;

import org.apache.commons.codec.binary.Hex;

public class Main {

  public static void main(String[] args) {
    System.out.println("Hello World!");
    String
        publicKey =
        "0450863AD64A87AE8A2FE83C1AF1A8403CB53F53E486D8511DAD8A04887E5B23522CD470243453A299FA9E77237716103ABC11A1DF38855ED6F2EE187E9C582BA6";
    String address = Bitcoin.getAddress(publicKey);
    System.out.println(address);
    try {
      String str = "00ff52";
      byte[] bStr = Hex.decodeHex(str.toCharArray());
      String s58 = Bitcoin.encodeBase58(bStr);
      System.out.println(s58);
      System.out.println(Hex.encodeHexString(Bitcoin.decodeBase58(s58)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
