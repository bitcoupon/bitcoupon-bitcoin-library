package bitcoupon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bitcoupon.transaction.Coupon;
import bitcoupon.transaction.Creation;
import bitcoupon.transaction.Input;
import bitcoupon.transaction.Output;
import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.Transaction;
import bitcoupon.transaction.TransactionHistory;

public class BitCoupon {

  private static final boolean DEBUG = true;

  /**
   * This function generates a transaction where a coupon is created.
   *
   * @param strPrivateKey The private key of the user creating the coupon.
   * @param payload       The payload of the coupon that is to be created.
   * @return A transaction creating a coupon. This transaction needs to be sent to the server.
   */
  public static Transaction generateCreateTransaction(String strPrivateKey, String payload) {
    List<Creation> creations = new ArrayList<Creation>();
    List<Input> inputs = new ArrayList<Input>();
    List<Output> outputs = new ArrayList<Output>();
    Transaction transaction = new Transaction(creations, inputs, outputs);
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String creatorAddress = Bitcoin.publicKeyToAddress(publicKey);
    creations.add(new Creation(creatorAddress, payload, 1));
    outputs.add(new Output(creatorAddress, payload, 1, creatorAddress));
    transaction.signTransaction(privateKey);
    return transaction;
  }

  /**
   * This function generates a transaction where a coupon is sent from one user (defined by strPrivateKey) to another
   * user (defined by receiverAddress).
   *
   * @param strPrivateKey   The private key of the user sending the coupon.
   * @param coupon          The coupon that is to be sent.
   * @param receiverAddress The address of the user receiving the coupon.
   * @param outputHistory   Output history in which the sender owns the coupon.
   * @return A transaction sending a coupon. This transaction needs to be sent to the server.
   */
  public static Transaction generateSendTransaction(String strPrivateKey, Coupon coupon, String receiverAddress,
                                                    OutputHistory outputHistory) {
    List<Creation> creations = new ArrayList<Creation>();
    List<Input> inputs = new ArrayList<Input>();
    List<Output> outputs = new ArrayList<Output>();
    Transaction transaction = new Transaction(creations, inputs, outputs);
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String senderAddress = Bitcoin.publicKeyToAddress(publicKey);
    int couponsInInputs = 0;
    for (Output output : outputHistory.getOutputList()) {
      if (output.getCreatorAddress().equals(coupon.getCreatorAddress())
          && output.getPayload().equals(coupon.getPayload())
          && output.getReceiverAddress().equals(senderAddress)
          && output.getReferringInput() == 0) {
        inputs.add(new Input(output.getOutputId()));
        couponsInInputs += output.getAmount();
        if (couponsInInputs >= 1) {
          break;
        }
      }
    }
    if (couponsInInputs < 1) {
      throw new IllegalArgumentException();
    }
    outputs.add(new Output(coupon.getCreatorAddress(), coupon.getPayload(), 1, receiverAddress));
    if (couponsInInputs > 1) {
      outputs.add(new Output(coupon.getCreatorAddress(), coupon.getPayload(), couponsInInputs - 1, senderAddress));
    }
    transaction.signTransaction(privateKey);
    return transaction;
  }

  public static boolean verifyTransaction(Transaction transaction, OutputHistory outputHistory) {
    return false;
  }

  /**
   * This function lists all coupons in an output history that are owned by a user (defined by strPrivateKey).
   *
   * @param strPrivateKey The private key of the user listing his/her coupons.
   * @param outputHistory Output history for which the user wants to list his/her coupons.
   * @return A list of all coupons that the user owns in the output history.
   */
  public static List<Coupon> getCoupons(String strPrivateKey, OutputHistory outputHistory) {
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);
    List<Coupon> coupons = new ArrayList<Coupon>();
    for (Output output : outputHistory.getOutputList()) {
      if (output.getReceiverAddress().equals(address) && output.getReferringInput() == 0) {
        for (int i = 0; i < output.getAmount(); i++) {
          coupons.add(new Coupon(output.getCreatorAddress(), output.getPayload()));
        }
      }
    }
    return coupons;
  }

  /**
   * This function lists the addresses of all users who owns a coupon with the specified creator address and payload.
   *
   * @param creatorAddress The creator address of coupons to list coupon owner for.
   * @param payload        The payload of coupons to list coupon owner for.
   * @param outputHistory  Output history for which coupon owners should be listed.
   * @return A list of all owners of coupons (as specified) in the output history.
   */
  public static List<String> getCouponOwners(String creatorAddress, String payload, OutputHistory outputHistory) {
    List<String> couponOwners = new ArrayList<String>();
    for (Output output : outputHistory.getOutputList()) {
      if (output.getCreatorAddress().equals(creatorAddress)
          && output.getPayload().equals(payload)
          && output.getReferringInput() == 0) {
        for (int i = 0; i < output.getAmount(); i++) {
          couponOwners.add(new String(output.getReceiverAddress()));
        }
      }
    }
    return couponOwners;
  }


  /**
   * This function verifies that a transaction is consistent with previous transactions and that all signatures are
   * correct. This function is used by the server to verify every new transaction it receives.
   *
   * @param transaction        The transaction that is going to be verified.
   * @param transactionHistory Previous transactions that the transaction needs to be consistent with.
   * @return Returns true is the transaction is valid.
   */
  public static boolean verifyTransaction(Transaction transaction, TransactionHistory transactionHistory) {
    boolean inputIsValid = transaction.verifyInput(transactionHistory);
    boolean signatureIsValid = transaction.verifySignatures(transactionHistory);
    boolean amountIsValid = transaction.verifyAmount(transactionHistory);
    return inputIsValid && signatureIsValid && amountIsValid;
  }

  /**
   * This function generates a private key by calling the generatePrivateKey function in Bitcoin.java.
   *
   * @return Private key
   */
  public static String generatePrivateKey() {
    return Bitcoin.generatePrivateKey();
  }

}
