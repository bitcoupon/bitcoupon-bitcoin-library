package bitcoupon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BitCoupon {


  private static final boolean DEBUG = true;

  public static Transaction generateSendTransaction(String strPrivateKey, String creatorAddress, String receiverAddress,
                                                    TransactionHistory transactionHistory) {
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();

    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);

    for (int i = 0; i < transactionHistory.size() && inputs.size() == 0; i++) {
      List<Output> outputHistory = transactionHistory.get(i).getOutputs();
      for (int j = 0; j < outputHistory.size() && inputs.size() == 0; j++) {
        Output output = outputHistory.get(i);
        if (output.getAddress().equals(address) && output.getInputId() == 0 && output.getCreatorAddress()
            .equals(creatorAddress) && output.getAmount() == 1) {
          Input input = new Input(output.getOutputId());
          inputs.add(input);
        }
      }
    }
    if (inputs.size() == 0) {
      throw new IllegalArgumentException();
    }

    Output output = new Output(creatorAddress, 1, receiverAddress);
    outputs.add(output);

    Transaction transaction = new Transaction(creations, inputs, outputs);
    transaction.signTransaction(privateKey);
    return transaction;
  }

  public static List<String> getCreatorAddresses(String strPrivateKey, TransactionHistory transactionHistory) {

    // Get address from private key
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);

    // Create list for creator addresses
    ArrayList<String> creatorAddresses = new ArrayList<String>();

    // For every output in every transaction
    Iterator<Transaction> transactionIterator = transactionHistory.iterator();
    while (transactionIterator.hasNext()) {
      Transaction transaction = transactionIterator.next();
      Iterator<Output> outputIterator = transaction.getOutputs().iterator();
      while (outputIterator.hasNext()) {
        Output output = outputIterator.next();

        // Check if output is addressed to this user
        if (output.getAddress().equals(address)) {

          // Add the coupons in the output to the list of creator addresses
          for (int i = 0; i < output.getAmount(); i++) {
            creatorAddresses.add(output.getCreatorAddress());
          }
        }
      }
    }

    // Return creator addresses for the coupons this user owns
    return creatorAddresses;

  }

  public static Transaction generateCreationTransaction(String strPrivateKey) {

    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();

    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);

    Creation creation = new Creation(address, 1);
    creations.add(creation);
    Output output = new Output(address, 1, address);
    outputs.add(output);

    Transaction transaction = new Transaction(creations, inputs, outputs);
    transaction.signTransaction(privateKey);
    return transaction;

  }

  // This function verifies that a transaction is consistent with previous transactions
  // and that all signatures are correct
  public static boolean verifyTransaction(Transaction transaction, TransactionHistory transactionHistory) {
    boolean inputIsValid = transaction.verifyInput(transactionHistory);
    boolean signatureIsValid = transaction.verifySignatures(transactionHistory);
    boolean amountIsValid = transaction.verifyAmount(transactionHistory);
    return (inputIsValid && signatureIsValid && amountIsValid);
  }
}
