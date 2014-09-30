package bitcoupon;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Patrick on 30.09.2014.
 */
public class TransactionList {


  private List<Transaction> transactionList;

  public static TransactionList fromJson(String json) {
    return new Gson().fromJson(json, TransactionList.class);
  }

  public List<Transaction> getList() {
    return transactionList;
  }

}
