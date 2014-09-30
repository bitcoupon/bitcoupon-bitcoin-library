package bitcoupon;

import java.util.List;

public class BitCoupon {

  public static Transaction generateCreationTransaction(String privateKey) {
    return null;
  }

  public static Transaction generateSendTransaction(String privateKey, String creatorPublicKey,
                                                    List<Transaction> receiverAddress, String transactionHistory) {
    return null;
  }

  public static List<Transaction> getCreatorPublicKeys(List<Transaction> transactionHistory) {
    return null;
  }


  public static boolean verifyTransaction(Transaction transaction, List<Transaction> transactionHistory) {
    return false;

  }
}
