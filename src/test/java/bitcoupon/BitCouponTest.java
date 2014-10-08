package bitcoupon;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bitcoupon.transaction.Output;
import bitcoupon.transaction.Transaction;
import bitcoupon.transaction.TransactionHistory;

public class BitCouponTest extends TestCase {

  private final static String PRIVATE_KEY = "5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT";
  private final static String CREATOR_ADDRESS = getAddressFromPrivateKey(PRIVATE_KEY);

  public void testGenerateCreationTransaction() throws Exception {
    final Transaction creationTransaction = BitCoupon.generateCreationTransaction(PRIVATE_KEY);
    assertEquals(0, creationTransaction.getId());

    final List<Output> outputs = creationTransaction.getOutputs();
    assertNotNull(outputs);
    assertEquals(1, outputs.size());

    final Output output = outputs.get(0);
    assertEquals(CREATOR_ADDRESS, output.getAddress());
    assertEquals(CREATOR_ADDRESS, output.getCreatorAddress());
    assertEquals(1, output.getAmount());
  }

  public void testGenerateCreationTransactionWithNullArgumentThrowsException() throws Exception {
    try {
      BitCoupon.generateCreationTransaction(null);
      fail("We didn't get the expected IllegalArgumentException when passing a null private key");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testGetCreatorAddresses() throws Exception {
    Transaction transaction = BitCoupon.generateCreationTransaction(PRIVATE_KEY);
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction);
    TransactionHistory transactionHistory = new TransactionHistory(transactions);

    final List<String> creatorAddresses = BitCoupon.getCreatorAddresses(PRIVATE_KEY, transactionHistory);
    assertNotNull(creatorAddresses);
    assertEquals(1, creatorAddresses.size());

    assertEquals(CREATOR_ADDRESS, creatorAddresses.get(0));
  }

  public void testGenerateSendTransaction() throws Exception {
    Transaction creationTransaction = BitCoupon.generateCreationTransaction(PRIVATE_KEY);

    List<Transaction> transactions = new ArrayList<>();
    transactions.add(creationTransaction);

    TransactionHistory transactionHistory = new TransactionHistory(transactions);

    final Transaction sendTransaction = BitCoupon.generateSendTransaction(
        PRIVATE_KEY,
        CREATOR_ADDRESS,
        "ReceiverAddressStringAndIDontKnowWhatItsUsedFor",
        transactionHistory);
    assertEquals(0, sendTransaction.getId());
  }

  public void testVerifyTransaction() throws Exception {
    Transaction transaction = BitCoupon.generateCreationTransaction(PRIVATE_KEY);
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction);
    TransactionHistory transactionHistory = new TransactionHistory(transactions);

    final boolean verified = BitCoupon.verifyTransaction(transaction, transactionHistory);
    assertTrue("Failed to verify transaction", verified);
  }

  // Code stolen from Bitcoin class
  private static String getAddressFromPrivateKey(String strPrivateKey) {
    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    return Bitcoin.publicKeyToAddress(publicKey);
  }
}