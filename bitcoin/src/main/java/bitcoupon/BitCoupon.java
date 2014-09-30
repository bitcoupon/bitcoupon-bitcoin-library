package bitcoupon;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BitCoupon {

  public static Transaction generateSendTransaction(String privateKey, String creatorPublicKey,
                                                    List<Transaction> receiverAddress, String transactionHistory) {
    return null;
  }

  public static List<Transaction> getCreatorPublicKeys(List<Transaction> transactionHistory) {
    return null;
  }

  public static Transaction generateCreationTransaction(String strPrivateKey) {

    List<Creation> creations = new ArrayList<Creation>();
    List<Input> inputs = new ArrayList<Input>();
    List<Output> outputs = new ArrayList<Output>();

    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    byte[] hashedPublicKey = Bitcoin.hash160(publicKey);
    String strHashedPublicKey = Hex.encodeHexString(hashedPublicKey);

    Creation creation = new Creation(strHashedPublicKey,  1);
    creations.add(creation);
    Output output = new Output(strHashedPublicKey, 1, strHashedPublicKey);
    outputs.add(output);

    Transaction transaction = new Transaction(creations, inputs, outputs);
    transaction.signTransaction(privateKey);
    return transaction;

  }


  public static boolean verifyTransaction(Transaction transaction, List<Transaction> transactionHistory) {
    return false;

  }
}
