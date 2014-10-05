package bitcoupon;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Patrick on 30.09.2014.
 */
public class TransactionHistory implements Iterable<Transaction> {


  private final List<Transaction> transactionList;

  public TransactionHistory(List<Transaction> transactionList) {
    this.transactionList = transactionList;
  }


  private List<Transaction> getList() {
    return transactionList;
  }

  //  Transactionlist looks like this in json
//  {
//    "transactionList":[
//    {
//      "transactionId":0,
//        "creations":[
//      {
//        "creationId":0,
//          "creatorAddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "amount":1,
//          "signature":"3045022100c07bfdecb9ffe7406ea21730562dbf7797bcfd7e786e954727b0a5c8eaf70d110220556615caafcb7fbcfc262cf2f9dc79a258312639a0c1d09f298afdb4ca0fbfd9 047825706d44fc680a454aa4071b4df24c2087f2e48ab2d31e06fa58511b2872bd313c984a3dde16732cd9b995833ca41ecf1f1c5e1d57607134080b0d8fddcb72"
//      }
//      ],
//      "inputs":[
//
//      ],
//      "outputs":[
//      {
//        "outputId":0,
//          "creatorAddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "amount":1,
//          "address":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "inputId":0
//      }
//      ]
//    },
//    {
//      "transactionId":0,
//        "creations":[
//      {
//        "creationId":0,
//          "creatorAddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "amount":1,
//          "signature":"3044022031773631692a41ae1c3f66f8c92001b4b70a36e685f23d75b3a47aa39d06b02d02201cacb7582dd45fb44a2d80670f4777aef8495c4128653a262a4e309e14f4d3be 047825706d44fc680a454aa4071b4df24c2087f2e48ab2d31e06fa58511b2872bd313c984a3dde16732cd9b995833ca41ecf1f1c5e1d57607134080b0d8fddcb72"
//      }
//      ],
//      "inputs":[
//
//      ],
//      "outputs":[
//      {
//        "outputId":0,
//          "creatorAddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "amount":1,
//          "address":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "inputId":0
//      }
//      ]
//    },
//    {
//      "transactionId":0,
//        "creations":[
//      {
//        "creationId":0,
//          "creatorAddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "amount":1,
//          "signature":"3045022041a6bc88ad57c5a3a6980915ff6dc6abc8e03b78a0b7f8bb62c0ba45559fa560022100cd2a6aed2b144b281a597e514bd2a44fcfff12ad59163fee7ae8c2c60f5de1ec 047825706d44fc680a454aa4071b4df24c2087f2e48ab2d31e06fa58511b2872bd313c984a3dde16732cd9b995833ca41ecf1f1c5e1d57607134080b0d8fddcb72"
//      }
//      ],
//      "inputs":[
//
//      ],
//      "outputs":[
//      {
//        "outputId":0,
//          "creatorAddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "amount":1,
//          "address":"d7b3e15eefbb19945b2671025c846ba18164abce",
//          "inputId":0
//      }
//      ]
//    }
//    ]
//  }
  public static String toJson(List<Transaction> transactionList) {
    return new Gson().toJson(new TransactionHistory(transactionList), TransactionHistory.class);
  }

  public static TransactionHistory fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, TransactionHistory.class);
  }

  public static TransactionHistory fromJson(String json) {
    return new Gson().fromJson(json, TransactionHistory.class);
  }

  @Override
  public Iterator<Transaction> iterator() {
    return getList().iterator();
  }

  public int size() {
    return transactionList.size();
  }

  public Transaction get(int i) {
    return transactionList.get(i);
  }
}
