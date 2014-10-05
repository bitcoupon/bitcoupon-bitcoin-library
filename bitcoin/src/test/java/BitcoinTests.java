import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bitcoupon.BitCoupon;
import bitcoupon.Transaction;
import bitcoupon.TransactionList;

/**
 * Created by Patrick on 01.10.2014.
 */
public class BitcoinTests {

  @Test
  public void test_TransactionJsonConvertion() {
    Transaction trans = BitCoupon.generateCreationTransaction("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT");
    String json = Transaction.toJson(trans);
    Transaction fromJson = Transaction.fromJson(json);
    System.out.println(json);

    Transaction trans2 = BitCoupon.generateCreationTransaction("5K4gQUNnxuJe1gtbCp4qrGysRXVdGE9jZW1vJZ1jdFzV6W93QDP");
    Transaction trans3 = BitCoupon.generateCreationTransaction("5JcK7bvAFjCwTcERJsbRetGkwmqA8BuydVFRffrQj3LjuqWsTy5");
    Transaction trans4 = BitCoupon.generateCreationTransaction("5JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA");

    List<Transaction> transactionList = new ArrayList<>();
    transactionList.add(trans2);
    transactionList.add(trans3);
    transactionList.add(trans4);

    String jsonTransactionList = TransactionList.toJson(transactionList);
    System.out.println(jsonTransactionList);
    TransactionList listFromJson = TransactionList.fromJson(jsonTransactionList);
  }

  @Test
  public void test_TransactionCreation() {
    Map<String, Boolean> privateKeys = new HashMap<>();
    privateKeys.put("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT", true);
    privateKeys.put("5K4gQUNnxuJe1gtbCp4qrGysRXVdGE9jZW1vJZ1jdFzV6W93QDP", true);
    privateKeys.put("5JcK7bvAFjCwTcERJsbRetGkwmqA8BuydVFRffrQj3LjuqWsTy5", true);
    privateKeys.put("5JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA", true);
    privateKeys.put("9JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA", false);
    privateKeys.put("JcG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA", false);
    privateKeys.put(
        "JcasdddddddddddddddddddddddddddddddddddddddddddddddddddddddddG67FmXQtfjEJJd6To8988WqH7X5byXQPqbCYjh9RS27bdNMA",
        false);
    privateKeys.put("sadasdasdasd", false);
    privateKeys.put("0", false);
    privateKeys.put("1", false);
    privateKeys.put(null, false);
    privateKeys.put(String.valueOf(new Integer(1).hashCode()), false);
    for (String key : privateKeys.keySet()) {
      Boolean value = privateKeys.get(key);
      boolean valid = false;
      try {
        Transaction validTransaction = BitCoupon.generateCreationTransaction(key);
        valid = true;
      } catch (IllegalArgumentException e) {
        valid = false;
      }
      org.junit.Assert.assertEquals(valid, value);
      System.out.println("" + valid + value);
    }
  }
}
