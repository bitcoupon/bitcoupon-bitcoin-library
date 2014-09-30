package bitcoupon;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Set;

public class Wallet {

  private String strPrivateKey;

  Wallet(String privateKey) {
    this.strPrivateKey = privateKey;
  }

  Transaction createCoupons(String subType, int amount) {

    ArrayList<Creation> creations = new ArrayList<Creation>();
    ArrayList<Input> inputs = new ArrayList<Input>();
    ArrayList<Output> outputs = new ArrayList<Output>();

    BigInteger privateKey = Bitcoin.decodePrivateKey(strPrivateKey);
    byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
    byte[] hashedPublicKey = Bitcoin.hash160(publicKey);
    String strHashedPublicKey = Hex.encodeHexString(hashedPublicKey);

    Creation creation = new Creation(strHashedPublicKey, subType, amount);
    creations.add(creation);
    Output output = new Output(strHashedPublicKey + "-" + subType, amount, strHashedPublicKey);
    outputs.add(output);

    Transaction transaction = new Transaction(creations, inputs, outputs);
    transaction.signTransaction(privateKey);
    return transaction;

  }

  Set<Coupon> getCoupons() {
    return null;
  }

  private Set<Transaction> getTransactions(String publicKey) {
    return null;
  }

}
