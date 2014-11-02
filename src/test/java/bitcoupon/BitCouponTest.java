package bitcoupon;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bitcoupon.transaction.Coupon;
import bitcoupon.transaction.CouponList;
import bitcoupon.transaction.Creation;
import bitcoupon.transaction.Input;
import bitcoupon.transaction.Output;
import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.Transaction;

public class BitCouponTest extends TestCase {

  //private final static String PRIVATE_KEY = "5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT";
  //private final static String CREATOR_ADDRESS = getAddressFromPrivateKey(PRIVATE_KEY);

  public void testScenario() {
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
    List<Output> historyOutputs = new ArrayList<>();
    // Transaction 1
    Transaction transaction1 = BitCoupon.generateCreateTransaction(privateKey1, "First Coupon");
    assertTrue(BitCoupon.verifyTransaction(transaction1, new OutputHistory(historyOutputs)));
    historyOutputs.addAll(transaction1.getOutputs());
    // Transaction 2
    Transaction transaction2 = BitCoupon.generateSendTransaction(privateKey1, new Coupon(address1, "First Coupon"), address2, new OutputHistory(historyOutputs));
    assertTrue(BitCoupon.verifyTransaction(transaction2, new OutputHistory(historyOutputs)));
    historyOutputs.addAll(transaction2.getOutputs());
    // Transaction 3
    Transaction transaction3 = BitCoupon.generateSendTransaction(privateKey1, new Coupon(address1, "First Coupon"), address3, new OutputHistory(historyOutputs));
    assertFalse(BitCoupon.verifyTransaction(transaction3, new OutputHistory(historyOutputs)));
    // Transaction 4
    Transaction transaction4 = BitCoupon.generateSendTransaction(privateKey2, new Coupon(address1, "First Coupon"), address3, new OutputHistory(historyOutputs));
    assertTrue(BitCoupon.verifyTransaction(transaction4, new OutputHistory(historyOutputs)));
    historyOutputs.addAll(transaction4.getOutputs());
    // Transaction 5
    Transaction transaction5 = BitCoupon.generateCreateTransaction(privateKey4, "Second Coupon");
    assertTrue(BitCoupon.verifyTransaction(transaction5, new OutputHistory(historyOutputs)));
    historyOutputs.addAll(transaction5.getOutputs());
    // Transaction 6
    Transaction transaction6 = BitCoupon.generateSendTransaction(privateKey1, new Coupon(address4, "Second Coupon"), address1, new OutputHistory(historyOutputs));
    assertFalse(BitCoupon.verifyTransaction(transaction6, new OutputHistory(historyOutputs)));
    // Transaction 7
    Transaction transaction7 = BitCoupon.generateSendTransaction(privateKey4, new Coupon(address4, "Second Coupon"), address5, new OutputHistory(historyOutputs));
    assertTrue(BitCoupon.verifyTransaction(transaction7, new OutputHistory(historyOutputs)));
    historyOutputs.addAll(transaction7.getOutputs());
  }

  public void testGenerateCreateTransaction() throws Exception {
    String strPrivateKey = "5KJk52cBLWUF1PVHzGrmGeGULX6wqCwgt2WyoAALN3iw4UU6U17";
    String address = "144jGLYm61jqk7rpra6R5scicLx8BmE8ai";
    String payload = "payload";

    Transaction transaction = BitCoupon.generateCreateTransaction(strPrivateKey, payload);
    assertEquals(0, transaction.getTransactionId());

    List<Creation> creations = transaction.getCreations();
    assertEquals(1, creations.size());

    Creation creation = creations.get(0);
    assertEquals(0, creation.getCreationId());
    assertEquals(address, creation.getCreatorAddress());
    assertEquals(payload, creation.getPayload());
    assertEquals(1, creation.getAmount());
    //assertEquals("signature", creation.getSignature());

    List<Input> inputs = transaction.getInputs();
    assertEquals(0, inputs.size());

    List<Output> outputs = transaction.getOutputs();
    assertEquals(1, outputs.size());

    Output output = outputs.get(0);
    assertEquals(0, output.getOutputId());
    assertEquals(address, output.getCreatorAddress());
    assertEquals(payload, output.getPayload());
    assertEquals(1, output.getAmount());
    assertEquals(address, output.getReceiverAddress());
    assertEquals(0, output.getReferringInput());
  }

  public void testGenerateSendTransaction() throws Exception {
    String strPrivateKey = "5KJk52cBLWUF1PVHzGrmGeGULX6wqCwgt2WyoAALN3iw4UU6U17";
    String address = "144jGLYm61jqk7rpra6R5scicLx8BmE8ai";
    String payload = "payload";

    Transaction historyTransaction = BitCoupon.generateCreateTransaction(strPrivateKey, payload);
    OutputHistory outputHistory = new OutputHistory(historyTransaction.getOutputs());

    Transaction transaction = BitCoupon.generateSendTransaction(strPrivateKey, new Coupon(address, payload), address, outputHistory);
    assertEquals(0, transaction.getTransactionId());

    List<Creation> creations = transaction.getCreations();
    assertEquals(0, creations.size());

    List<Input> inputs = transaction.getInputs();
    assertEquals(1, inputs.size());

    Input input = inputs.get(0);
    assertEquals(0, input.getInputId());
    assertEquals(0, input.getReferredOutput());
    //assertEquals("signature", input.getSignature());

    List<Output> outputs = transaction.getOutputs();
    assertEquals(1, outputs.size());

    Output output = outputs.get(0);
    assertEquals(0, output.getOutputId());
    assertEquals(address, output.getCreatorAddress());
    assertEquals(payload, output.getPayload());
    assertEquals(1, output.getAmount());
    assertEquals(address, output.getReceiverAddress());
    assertEquals(0, output.getReferringInput());
  }

  public void testGenerateDeleteTransaction() throws Exception {
    String strPrivateKey = "5KJk52cBLWUF1PVHzGrmGeGULX6wqCwgt2WyoAALN3iw4UU6U17";
    String address = "144jGLYm61jqk7rpra6R5scicLx8BmE8ai";
    String payload = "payload";

    Transaction historyTransaction = BitCoupon.generateCreateTransaction(strPrivateKey, payload);
    OutputHistory outputHistory = new OutputHistory(historyTransaction.getOutputs());

    Transaction transaction = BitCoupon.generateDeleteTransaction(strPrivateKey, new Coupon(address, payload),
                                                                  outputHistory);
    assertEquals(0, transaction.getTransactionId());

    List<Creation> creations = transaction.getCreations();
    assertEquals(0, creations.size());

    List<Input> inputs = transaction.getInputs();
    assertEquals(1, inputs.size());

    Input input = inputs.get(0);
    assertEquals(0, input.getInputId());
    assertEquals(0, input.getReferredOutput());
    //assertEquals("signature", input.getSignature());

    List<Output> outputs = transaction.getOutputs();
    assertEquals(0, outputs.size());
  }

  public void testGetCoupons() throws Exception {
    String strPrivateKey = "5KJk52cBLWUF1PVHzGrmGeGULX6wqCwgt2WyoAALN3iw4UU6U17";
    String address = "144jGLYm61jqk7rpra6R5scicLx8BmE8ai";
    String payload = "payload";

    Transaction historyTransaction = BitCoupon.generateCreateTransaction(strPrivateKey, payload);
    OutputHistory outputHistory = new OutputHistory(historyTransaction.getOutputs());

    CouponList couponList = BitCoupon.getCoupons(address, outputHistory);
    List<Coupon> coupons = couponList.getCoupons();
    assertEquals(1, coupons.size());

    Coupon coupon = coupons.get(0);
    assertEquals(address, coupon.getCreatorAddress());
    assertEquals(payload, coupon.getPayload());
  }

//
//


//
//  public void testVerifyTransaction() throws Exception {
//    Transaction transaction = BitCoupon.generateCreationTransaction(PRIVATE_KEY);
//    List<Transaction> transactions = new ArrayList<>();
//    transactions.add(transaction);
//    TransactionHistory transactionHistory = new TransactionHistory(transactions);
//
//    final boolean verified = BitCoupon.verifyTransaction(transaction, transactionHistory);
//    assertTrue("Failed to verify transaction", verified);
//  }

}