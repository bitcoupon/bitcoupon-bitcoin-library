package bitcoupon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bitcoupon.transaction.Creation;
import bitcoupon.transaction.Input;
import bitcoupon.transaction.Output;
import bitcoupon.transaction.Transaction;
import bitcoupon.transaction.TransactionHistory;

public class BitCoupon {


  private static final boolean DEBUG = true;

  public static Transaction generateSendTransaction(String strPrivateKey, String creatorAddress, String receiverAddress,
                                                    TransactionHistory transactionHistory) {

    // Lists for creations, inputs and outputs in the transaction
    List<Creation> creations = new ArrayList<Creation>();
    List<Input> inputs = new ArrayList<Input>();
    List<Output> outputs = new ArrayList<Output>();

    // Get sender address from private key
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String senderAddress = Bitcoin.publicKeyToAddress(publicKey);

    // Variable for counting number of coupons in referred transaction outputs
    int couponsInInputs = 0;

    // Find previous transaction outputs to refer to
    Iterator<Transaction> transactionIterator = transactionHistory.iterator();
    while (transactionIterator.hasNext() && couponsInInputs < 1) {
      Transaction transaction = transactionIterator.next();
      Iterator<Output> outputIterator = transaction.getOutputs().iterator();
      while (outputIterator.hasNext() && couponsInInputs < 1) {
        Output output = outputIterator.next();
        if (output.getCreatorAddress().equals(creatorAddress) && output.getAddress().equals(senderAddress)
            && output.getInputId() == 0) {
          Input input = new Input(output.getOutputId());
          inputs.add(input);
          couponsInInputs += output.getAmount();
        }
      }
    }

    // Check if enough coupons are available
    if (couponsInInputs <= 0) {
      throw new IllegalArgumentException();
    }

    // Set that 1 coupon should be sent to receiver address
    Output output = new Output(creatorAddress, 1, receiverAddress);
    outputs.add(output);

    // Send remaining coupons in referred transaction outputs back to the user as change
    if (couponsInInputs > 1) {
      Output changeOutput = new Output(creatorAddress, couponsInInputs - 1, senderAddress);
      outputs.add(changeOutput);
    }

    // Create transaction, sign it and return it
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
        if (output.getAddress().equals(address) && output.getInputId() == 0) {

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

    // Lists for creations, inputs and outputs in the transaction
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();

    // Get address from private key
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);

    // Create 1 coupon
    Creation creation = new Creation(address, 1);
    creations.add(creation);

    // Send the coupon to the creator
    Output output = new Output(address, 1, address);
    outputs.add(output);

    // Create transaction, sign it and return it
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
