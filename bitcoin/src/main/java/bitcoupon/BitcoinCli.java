package bitcoupon;

import java.util.List;

/**
 * Created by Patrick on 01.10.2014.
 */
public class BitcoinCli {


  static void verifyTransaction(String transactionJson, String transactionHistoryJson) {
    Transaction transaction = Transaction.fromJson(transactionJson);
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.verifyTransaction(transaction, transactionHistory);
  }

  static void getCreatorPublicKeys(String transactionHistoryJson) {
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.getCreatorAddresses(transactionHistory);
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
