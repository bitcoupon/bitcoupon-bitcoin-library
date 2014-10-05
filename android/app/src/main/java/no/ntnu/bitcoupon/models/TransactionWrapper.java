package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import java.io.BufferedReader;

import bitcoupon.transaction.Transaction;


/**
 * Created by Patrick on 05.10.2014.
 */
public class TransactionWrapper {

  final Transaction transaction;

  public TransactionWrapper(Transaction transaction) {
    this.transaction = transaction;
  }

  public static TransactionWrapper fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, TransactionWrapper.class);
  }


  public static TransactionWrapper fromJson(String json) {
    return new Gson().fromJson(json, TransactionWrapper.class);
  }

  public static String toJson(TransactionWrapper item) {
    return new Gson().toJson(item, TransactionWrapper.class);
  }
}
