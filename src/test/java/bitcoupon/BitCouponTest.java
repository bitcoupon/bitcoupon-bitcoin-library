package bitcoupon;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bitcoupon.transaction.Coupon;
import bitcoupon.transaction.Creation;
import bitcoupon.transaction.Input;
import bitcoupon.transaction.Output;
import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.OutputHistoryRequest;
import bitcoupon.transaction.Transaction;

public class BitCouponTest extends TestCase {

  public void testVerifyTransaction() {

    // Private keys and addresses of five users
    String privateKey1 = "5KJk52cBLWUF1PVHzGrmGeGULX6wqCwgt2WyoAALN3iw4UU6U17";
    String address1 = "144jGLYm61jqk7rpra6R5scicLx8BmE8ai";
    String privateKey2 = "5J4L1KDQeyCtECrTYoGUcsqtJ894zpnRXhsJd3eXNttJzWKMELQ";
    String address2 = "15xvd7yEdMzFMbJHD5BTEbVYYNmjamQh5T";
    String privateKey3 = "5JRgTW4S9Vzqw9YVBwcWh8gkzNi4eW9iQb1oBqisD7Xe4HgHeyt";
    String address3 = "17fzNLv6oNyfbsnA66mr1DBVy2Zb7wdFu2";
    String privateKey4 = "5K52CWxfXyGCTHiuZSAET8GdDjoFE5PbHMsPs5cAPTssNnF35nn";
    String address4 = "1HdbVb6NJs7BkhzyzCFf34xgR8gj6rSvEJ";
    String privateKey5 = "5KJYugZugsM6Vh8dAu8gcg8P4Wg1JmXYUc2xG5w2hRfWqtUWH2G";
    String address5 = "1FG73czRCvF8R8QqHF6FVzBJS2i8xMTeRo";

    // List of outputs for output history
    OutputHistory outputHistory = new OutputHistory(new ArrayList<Output>());

    // Transaction 1, User 1 creates "Coupon" to himself
    Transaction transaction1 = BitCoupon.generateCreateTransaction(privateKey1, "Coupon");
    assertTrue(BitCoupon.verifyTransaction(transaction1, outputHistory));
    outputHistory.addTransactionToHistory(transaction1);

    // Transaction 2, User 1 sends "Coupon" to User 2
    Transaction transaction2 = BitCoupon.generateSendTransaction(privateKey1, new Coupon(address1, "Coupon"), address2, outputHistory);
    assertTrue(BitCoupon.verifyTransaction(transaction2, outputHistory));
    outputHistory.addTransactionToHistory(transaction2);

    // Transaction 3, User 2 sends "Coupon" to User 3
    Transaction transaction3 = BitCoupon.generateSendTransaction(privateKey2, new Coupon(address1, "Coupon"), address3, outputHistory);
    assertTrue(BitCoupon.verifyTransaction(transaction3, outputHistory));
    outputHistory.addTransactionToHistory(transaction3);

    // Transaction 4-5, User 3 sends "Coupon" to User 1 and to himself (double spend coupon)
    Transaction transaction4 = BitCoupon.generateSendTransaction(privateKey3, new Coupon(address1, "Coupon"), address1, outputHistory);
    Transaction transaction5 = BitCoupon.generateSendTransaction(privateKey3, new Coupon(address1, "Coupon"), address3, outputHistory);
    assertTrue(BitCoupon.verifyTransaction(transaction4, outputHistory));
    outputHistory.addTransactionToHistory(transaction4);
    assertFalse(BitCoupon.verifyTransaction(transaction5, outputHistory));

    // Transaction 6, User 1 creates "Coupon" to User 3 with User 3's signature (forge coupon)
    Transaction transaction6 = generateCreateTransactionWithSpecificCreatorAddress(privateKey3, address1, "Coupon");
    assertFalse(BitCoupon.verifyTransaction(transaction6, outputHistory));

    // Transaction 7, User 1 sends "Coupon" to User 2
    Transaction transaction7 = BitCoupon.generateSendTransaction(privateKey1, new Coupon(address1, "Coupon"), address2, outputHistory);
    assertTrue(BitCoupon.verifyTransaction(transaction7, outputHistory));
    outputHistory.addTransactionToHistory(transaction7);

    // Transaction 8, User 2 sends "Coupon" to User 3 without signature (stealing coupon)
    Transaction transaction8 = generateSendTransactionWithoutSignature(address2, new Coupon(address1, "Coupon"), address3, outputHistory);
    assertFalse(BitCoupon.verifyTransaction(transaction8, outputHistory));

    // Transaction 9, User 1 creates "Coupon" to User 3 without creation (forge coupon)
    Transaction transaction9 = generateCreateTransactionWithoutCreation(privateKey3, address1, "Coupon");
    assertFalse(BitCoupon.verifyTransaction(transaction9, outputHistory));
  }

  private static Transaction generateCreateTransactionWithSpecificCreatorAddress(String strPrivateKey, String creatorAddress, String payload) {
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();
    Transaction transaction = new Transaction(creations, inputs, outputs);
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String receiverAddress = Bitcoin.publicKeyToAddress(publicKey);
    creations.add(new Creation(creatorAddress, payload, 1));
    outputs.add(new Output(creatorAddress, payload, 1, receiverAddress));
    transaction.signTransaction(privateKey);
    return transaction;
  }

  private static Transaction generateCreateTransactionWithoutCreation(String strPrivateKey, String creatorAddress, String payload) {
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();
    Transaction transaction = new Transaction(creations, inputs, outputs);
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    String receiverAddress = Bitcoin.publicKeyToAddress(publicKey);
    outputs.add(new Output(creatorAddress, payload, 1, receiverAddress));
    transaction.signTransaction(privateKey);
    return transaction;
  }

  private static Transaction generateSendTransactionWithoutSignature(String senderAddress, Coupon coupon, String receiverAddress, OutputHistory outputHistory) {
    List<Creation> creations = new ArrayList<>();
    List<Input> inputs = new ArrayList<>();
    List<Output> outputs = new ArrayList<>();
    Transaction transaction = new Transaction(creations, inputs, outputs);
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
    return transaction;
  }

  public void testVerifyOutputHistoryRequest() {

    // Private keys and addresses of five users
    String privateKey1 = "5KJk52cBLWUF1PVHzGrmGeGULX6wqCwgt2WyoAALN3iw4UU6U17";
    String address1 = "144jGLYm61jqk7rpra6R5scicLx8BmE8ai";
    String privateKey2 = "5J4L1KDQeyCtECrTYoGUcsqtJ894zpnRXhsJd3eXNttJzWKMELQ";
    String address2 = "15xvd7yEdMzFMbJHD5BTEbVYYNmjamQh5T";
    String privateKey3 = "5JRgTW4S9Vzqw9YVBwcWh8gkzNi4eW9iQb1oBqisD7Xe4HgHeyt";
    String address3 = "17fzNLv6oNyfbsnA66mr1DBVy2Zb7wdFu2";
    String privateKey4 = "5K52CWxfXyGCTHiuZSAET8GdDjoFE5PbHMsPs5cAPTssNnF35nn";
    String address4 = "1HdbVb6NJs7BkhzyzCFf34xgR8gj6rSvEJ";
    String privateKey5 = "5KJYugZugsM6Vh8dAu8gcg8P4Wg1JmXYUc2xG5w2hRfWqtUWH2G";
    String address5 = "1FG73czRCvF8R8QqHF6FVzBJS2i8xMTeRo";

    OutputHistoryRequest outputHistoryRequest1 = BitCoupon.generateOutputHistoryRequest(privateKey1);
    assertTrue(BitCoupon.verifyOutputHistoryRequest(outputHistoryRequest1));

    OutputHistoryRequest outputHistoryRequest2 = generateOutputHistoryRequestWithSpecificAddress(privateKey2, address1);
    assertFalse(BitCoupon.verifyOutputHistoryRequest(outputHistoryRequest2));

    OutputHistoryRequest outputHistoryRequest3 = generateOutputHistoryRequestWithoutSignature(address1);
    assertFalse(BitCoupon.verifyOutputHistoryRequest(outputHistoryRequest3));

  }

  private static OutputHistoryRequest generateOutputHistoryRequestWithSpecificAddress(String strPrivateKey, String address) {
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    OutputHistoryRequest outputHistoryRequest = new OutputHistoryRequest(address);
    outputHistoryRequest.signOutputHistoryRequest(privateKey);
    return outputHistoryRequest;
  }

  private static OutputHistoryRequest generateOutputHistoryRequestWithoutSignature(String address) {
    OutputHistoryRequest outputHistoryRequest = new OutputHistoryRequest(address);
    return outputHistoryRequest;
  }

}