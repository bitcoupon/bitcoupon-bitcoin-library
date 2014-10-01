import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    List<Transaction> transactionList = new ArrayList<Transaction>();
    transactionList.add(trans2);
    transactionList.add(trans3);
    transactionList.add(trans4);

    String jsonTransactionList = TransactionList.toJson(transactionList);
    System.out.println(jsonTransactionList);
    List<Transaction> listFromJson = TransactionList.fromJson(jsonTransactionList).getList();
  }
}
