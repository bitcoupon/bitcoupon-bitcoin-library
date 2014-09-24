package bitcoupon;

import java.util.ArrayList;

public class Transaction {

  private long transactionId;
  private ArrayList<Creation> creations;
  private ArrayList<Input> inputs;
  private ArrayList<Output> outputs;

  Transaction(long transactionId, ArrayList<Creation> creations, ArrayList<Input> inputs, ArrayList<Output> outputs) {
    this.transactionId = transactionId;
    this.creations = creations;
    this.inputs = inputs;
    this.outputs = outputs;
  }

  void signTransaction(String privateKey, String publicKey) {

  }

}
