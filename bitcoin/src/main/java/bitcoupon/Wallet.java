package bitcoupon;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Set;

import javax.print.DocFlavor;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Wallet {

  private String privateKey;
  private String publicKey;

  Wallet(String privateKey, String publicKey) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  Transaction createCoupons(String subType, int amount) {

    ArrayList<Creation> creations = new ArrayList<Creation>();
    ArrayList<Input> inputs = new ArrayList<Input>();
    ArrayList<Output> outputs = new ArrayList<Output>();

    String address = Bitcoin.getAddress(publicKey);
    Creation creation = new Creation(0, address, subType, amount);
    creations.add(creation);
    Output output = new Output(0, address + " - " + subType, amount, address, 0);
    outputs.add(output);

    Transaction transaction = new Transaction(0,creations, inputs, outputs);
    transaction.signTransaction(privateKey, publicKey);
    return transaction;

  }

  Set<Coupon> getCoupons() {
    return null;
  }

  private Set<Transaction> getTransactions(String publicKey) {
    return null;
  }

}
