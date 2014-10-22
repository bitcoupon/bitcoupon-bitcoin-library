package bitcoupon;

import bitcoupon.transaction.Coupon;
import bitcoupon.transaction.CouponList;
import bitcoupon.transaction.CouponOwnerList;
import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.OutputHistoryRequest;
import bitcoupon.transaction.Transaction;

/**
 * This class provides a Json interface for the class BitCoupon.java.
 */
public class BitCouponCli {

  public static void generateCreateTransaction(String strPrivateKey, String payload) {
    Transaction transaction = BitCoupon.generateCreateTransaction(strPrivateKey, payload);
    System.out.println(Transaction.toJson(transaction));
  }

  public static void generateSendTransaction(String strPrivateKey, String creatorAddress, String payload,
                                             String receiverAddress, String outputHistoryJson) {
    Coupon coupon = new Coupon(creatorAddress, payload);
    OutputHistory outputHistory = OutputHistory.fromJson(outputHistoryJson);
    Transaction transaction = BitCoupon.generateSendTransaction(strPrivateKey, coupon, receiverAddress, outputHistory);
    System.out.println(Transaction.toJson(transaction));
  }

  public static void generateDeleteTransaction(String strPrivateKey, String creatorAddress, String payload,
                                               String outputHistoryJson) {
    Coupon coupon = new Coupon(creatorAddress, payload);
    OutputHistory outputHistory = OutputHistory.fromJson(outputHistoryJson);
    Transaction transaction = BitCoupon.generateDeleteTransaction(strPrivateKey, coupon, outputHistory);
    System.out.println(Transaction.toJson(transaction));
  }

  public static void verifyTransaction(String transactionJson, String outputHistoryJson) {
    Transaction transaction = Transaction.fromJson(transactionJson);
    OutputHistory outputHistory = OutputHistory.fromJson(outputHistoryJson);
    boolean validTransaction = BitCoupon.verifyTransaction(transaction, outputHistory);
    System.out.println(validTransaction);
  }

  public static void generateOutputHistoryRequest(String strPrivateKey) {
    OutputHistoryRequest outputHistoryRequest = BitCoupon.generateOutputHistoryRequest(strPrivateKey);
    System.out.println(OutputHistoryRequest.toJson(outputHistoryRequest));
  }

  public static void verifyOutputHistoryRequest(String outputHistoryRequestJson) {
    OutputHistoryRequest outputHistoryRequest = OutputHistoryRequest.fromJson(outputHistoryRequestJson);
    boolean validOutputHistoryRequest = BitCoupon.verifyOutputHistoryRequest(outputHistoryRequest);
    System.out.println(validOutputHistoryRequest);
  }

  public static void getCoupons(String address, String outputHistoryJson) {
    OutputHistory outputHistory = OutputHistory.fromJson(outputHistoryJson);
    CouponList couponList = BitCoupon.getCoupons(address, outputHistory);
    System.out.println(CouponList.toJson(couponList));
  }

  public static void getCouponOwners(String creatorAddress, String outputHistoryJson) {
    OutputHistory outputHistory = OutputHistory.fromJson(outputHistoryJson);
    CouponOwnerList couponOwnerList = BitCoupon.getCouponOwners(creatorAddress, outputHistory);
    System.out.println(CouponOwnerList.toJson(couponOwnerList));
  }

  public static void generatePrivateKey() {
    String privateKey = BitCoupon.generatePrivateKey();
    System.out.println(privateKey);
  }

  public static void generateAddress(String strPrivateKey) {
    String address = BitCoupon.generateAddress(strPrivateKey);
    System.out.println(address);
  }

}
