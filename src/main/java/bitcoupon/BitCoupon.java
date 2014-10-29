package bitcoupon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bitcoupon.transaction.Coupon;
import bitcoupon.transaction.CouponList;
import bitcoupon.transaction.CouponOwner;
import bitcoupon.transaction.CouponOwnerList;
import bitcoupon.transaction.Creation;
import bitcoupon.transaction.Input;
import bitcoupon.transaction.Output;
import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.OutputHistoryRequest;
import bitcoupon.transaction.Transaction;

/**
 * This class contains static functions that are used by both server and clients to handle coupon transactions.
 */
public class BitCoupon {

  // private static final boolean DEBUG = true;

  /**
   * This function generates a transaction where a coupon is created.
   *
   * @param strPrivateKey The private key of the user creating the coupon.
   * @param payload       The payload of the coupon that is to be created.
   * @return A transaction creating a coupon. This transaction needs to be sent to the server.
   */
  public static Transaction generateCreateTransaction(String strPrivateKey, String payload) {
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();
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
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();
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

  /**
   * This function generates a transaction where a coupon is deleted.
   *
   * @param strPrivateKey The private key of the user deleting the coupon.
   * @param coupon        The coupon that is to be deleted.
   * @param outputHistory Output history in which the user owns the coupon.
   * @return A transaction deleting a coupon. This transaction needs to be sent to the server.
   */
  public static Transaction generateDeleteTransaction(String strPrivateKey, Coupon coupon,
                                                      OutputHistory outputHistory) {
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();
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
    if (couponsInInputs > 1) {
      outputs.add(new Output(coupon.getCreatorAddress(), coupon.getPayload(), couponsInInputs - 1, senderAddress));
    }
    transaction.signTransaction(privateKey);
    return transaction;
  }

  /**
   * This function checks that a transaction is consistent with an output history and that all signatures are valid.
   *
   * @param transaction   The transaction that is going to be checked.
   * @param outputHistory Output history that transaction should be consistent with.
   * @return True if the transaction is consistent with the output history and all signatures are valid.
   */
  public static boolean verifyTransaction(Transaction transaction, OutputHistory outputHistory) {
    return transaction.verifyConsistency(outputHistory) && transaction.verifyReceiverAddresses()
           && transaction.verifySignatures(outputHistory);
  }

  /**
   * This function generates an output history request which is used by clients to request information about previous
   * transactions from the server.
   *
   * @param strPrivateKey The private key of the user requesting output history.
   * @return Output history that can be used when calling other functions in the library.
   */
  public static OutputHistoryRequest generateOutputHistoryRequest(String strPrivateKey) {
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String address = Bitcoin.publicKeyToAddress(publicKey);
    OutputHistoryRequest outputHistoryRequest = new OutputHistoryRequest(address);
    outputHistoryRequest.signOutputHistoryRequest(privateKey);
    return outputHistoryRequest;
  }

  /**
   * This function is used by the server for verifying that the signature in an output history request i valid.
   *
   * @param outputHistoryRequest The output history request that should be verified.
   * @return True if the signature of the output history request is valid.
   */
  public static boolean verifyOutputHistoryRequest(OutputHistoryRequest outputHistoryRequest) {
    return outputHistoryRequest.verifySignature();
  }

  /**
   * This function lists all coupons in an output history that are owned by a user.
   *
   * @param address       The address of the user listing his/her coupons.
   * @param outputHistory Output history for which the user wants to list his/her coupons.
   * @return A list of all coupons that the user owns in the output history.
   */
  public static CouponList getCoupons(String address, OutputHistory outputHistory) {
    List<Coupon> coupons = new ArrayList<>();
    for (Output output : outputHistory.getOutputList()) {
      if (output.getReceiverAddress().equals(address) && output.getReferringInput() == 0) {
        for (int i = 0; i < output.getAmount(); i++) {
          coupons.add(new Coupon(output.getCreatorAddress(), output.getPayload()));
        }
      }
    }
    return new CouponList(coupons);
  }

  /**
   * This function lists  all coupons along with the addresses of the current owner of the coupon for all coupons
   * created by creatorAddress.
   *
   * @param creatorAddress The creator address of coupons to list coupon owner for.
   * @param outputHistory  Output history for which coupon owners should be listed.
   * @return A list of all owners of coupons created by creatorAddress.
   */
  public static CouponOwnerList getCouponOwners(String creatorAddress, OutputHistory outputHistory) {
    List<CouponOwner> couponOwners = new ArrayList<>();
    for (Output output : outputHistory.getOutputList()) {
      if (output.getCreatorAddress().equals(creatorAddress) && output.getReferringInput() == 0) {
        for (int i = 0; i < output.getAmount(); i++) {
          couponOwners.add(new CouponOwner(new Coupon(output.getCreatorAddress(), output.getPayload()),
                                           output.getReceiverAddress()));
        }
      }
    }
    return new CouponOwnerList(couponOwners);
  }

  /**
   * This function generates a private key by calling the generatePrivateKey function in Bitcoin.java.
   *
   * @return Private key in Base58 representation.
   */
  public static String generatePrivateKey() {
    return Bitcoin.generatePrivateKey();
  }

  /**
   * This function generates the address corresponding to a private key.
   *
   * @param strPrivateKey The private key to generate address for.
   * @return The address corresponding to the private key.
   */
  public static String generateAddress(String strPrivateKey) {
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    return Bitcoin.publicKeyToAddress(publicKey);
  }

}
