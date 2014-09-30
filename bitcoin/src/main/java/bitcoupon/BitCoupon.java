package bitcoupon;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.ArrayList;

public class BitCoupon {

  public static Transaction generateCreationTransaction(String strPrivateKey) {

    ArrayList<Creation> creations = new ArrayList<Creation>();
    ArrayList<Input> inputs = new ArrayList<Input>();
    ArrayList<Output> outputs = new ArrayList<Output>();

    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    byte[] hashedPublicKey = Bitcoin.hash160(publicKey);
    String strHashedPublicKey = Hex.encodeHexString(hashedPublicKey);

    Creation creation = new Creation(strHashedPublicKey, "0", 1);
    creations.add(creation);
    Output output = new Output(strHashedPublicKey + "-" + "0", 1, strHashedPublicKey);
    outputs.add(output);

    Transaction transaction = new Transaction(creations, inputs, outputs);
    transaction.signTransaction(privateKey);
    return transaction;

  }

  public static Transaction generateSendTransaction(String privateKey, String creatorPublicKey, String address, String transactionHistoryJson) {
    return null;
  }

}
