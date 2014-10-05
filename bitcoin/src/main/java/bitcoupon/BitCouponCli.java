package bitcoupon;

import java.util.List;

/**
 * Created by Patrick on 01.10.2014.
 */
public class BitCouponCli {

  public static void verifyTransaction(String transactionJson, String transactionHistoryJson) {
    Transaction transaction = Transaction.fromJson(transactionJson);
    TransactionList transactionHistory = TransactionList.fromJson(transactionHistoryJson);
    boolean out = BitCoupon.verifyTransaction(transaction, transactionHistory);
    System.out.println("" + out);
  }

  public static void getCreatorPublicKeys(String privateKey, String transactionHistoryJson) {
    TransactionList transactionHistory = TransactionList.fromJson(transactionHistoryJson);
    List<String> out = BitCoupon.getCreatorAddresses(privateKey, transactionHistory);
    for (String s : out) {
      System.out.println(s);
    }
  }

  public static void generateSendTransaction(String privateKey, String creatorPublicKey, String transactionHistoryJson,
                                             String receiverAddress) {
    TransactionList transactionHistory = TransactionList.fromJson(transactionHistoryJson);
    Transaction
        out =
        BitCoupon.generateSendTransaction(privateKey, creatorPublicKey, receiverAddress, transactionHistory);
    System.out.println(Transaction.toJson(out));
  }

  public static void generateCreationTransaction(String privateKey) {
    Transaction out = BitCoupon.generateCreationTransaction(privateKey);
    System.out.println(Transaction.toJson(out));
  }
}
