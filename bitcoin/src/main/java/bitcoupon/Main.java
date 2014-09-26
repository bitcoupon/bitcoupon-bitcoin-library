package bitcoupon;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

public class Main {

  public static void main(String[] args) {
    String strPrivateKey = "5JeaLSdKKpgkwjWUMQG3s5fTmYdKZTYUuggFxarkbvLbQdy48Lh";
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);
    System.out.println(address);
  }

}
