package bitcoupon;

import java.util.List;

/**
 * Created by Patrick on 01.10.2014.
 */
public class BitCouponCli {


  static void verifyTransaction(String transactionJson, String transactionHistoryJson) {
    Transaction transaction = Transaction.fromJson(transactionJson);
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.verifyTransaction(transaction, transactionHistory);
  }

  static void getCreatorPublicKeys(String privateKey, String transactionHistoryJson) {
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.getCreatorAddresses(privateKey, transactionHistory);
  }

  static void generateSendTransaction(String privateKey, String creatorPublicKey, String transactionHistoryJson,
                                      String receiverAddress) {
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.generateSendTransaction(privateKey, creatorPublicKey, receiverAddress, transactionHistory);
  }

  static void generateCreationTransaction(String privateKey) {
    BitCoupon.generateCreationTransaction(privateKey);
  }
}
