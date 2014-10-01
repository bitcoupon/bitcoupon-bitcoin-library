package bitcoupon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BitCoupon {

  public static Transaction generateSendTransaction(String privateKey, String creatorAddress,
                                                    String receiverAddress, List<Transaction> transactionHistory) {
    return null;
  }

  public static List<String> getCreatorAddresses(List<Transaction> transactionHistory) {
    return null;
  }

  public static Transaction generateCreationTransaction(String strPrivateKey) {

    List<Creation> creations = new ArrayList<Creation>();
    List<Input> inputs = new ArrayList<Input>();
    List<Output> outputs = new ArrayList<Output>();

    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);

    Creation creation = new Creation(address,  1);
    creations.add(creation);
    Output output = new Output(address, 1, address);
    outputs.add(output);

    Transaction transaction = new Transaction(creations, inputs, outputs);
    transaction.signTransaction(privateKey);
    return transaction;

  }


  public static boolean verifyTransaction(Transaction transaction, List<Output> transactionHistory) {
    if (transaction.verifyInput(transactionHistory) && transaction.verifySignatures(transactionHistory) && transaction.verifyAmount(transactionHistory)){
        return true;
    }
    return false;

  }
}
