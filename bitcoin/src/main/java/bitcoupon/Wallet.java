package bitcoupon;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.Set;

public class Wallet {

  private String privateKey;
  private String publicKey;

  Wallet(String privateKey, String publicKey) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  Transaction createCoupons(String subType, int amount) {
    try {
      ArrayList<Creation> creations = new ArrayList<Creation>();
      ArrayList<Input> inputs = new ArrayList<Input>();
      ArrayList<Output> outputs = new ArrayList<Output>();

      byte[] bPublicKey = Hex.decodeHex(publicKey.toCharArray());
      byte[] bHashedPublicKey = Bitcoin.hash160(bPublicKey);
      String hashedPublicKey = Hex.encodeHexString(bHashedPublicKey);
      Creation creation = new Creation(hashedPublicKey, subType, amount);
      creations.add(creation);
      Output output = new Output(hashedPublicKey + " - " + subType, amount, hashedPublicKey);
      outputs.add(output);

      Transaction transaction = new Transaction(creations, inputs, outputs);
      transaction.signTransaction(privateKey, publicKey);
      return transaction;

    } catch (DecoderException e) {
      e.printStackTrace();
      return null;
    }
  }

  Set<Coupon> getCoupons() {
    return null;
  }

  private Set<Transaction> getTransactions(String publicKey) {
    return null;
  }

}
