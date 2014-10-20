package bitcoupon.transaction;

import com.google.gson.Gson;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.DERSequenceGenerator;
import org.spongycastle.asn1.DLSequence;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import bitcoupon.Bitcoin;

public class Transaction {

  private final long transactionId;
  private final List<Creation> creations;
  private final List<Input> inputs;
  private final List<Output> outputs;

  public Transaction(List<Creation> creations, List<Input> inputs, List<Output> outputs) {
    this.transactionId = 0;
    this.creations = creations;
    this.inputs = inputs;
    this.outputs = outputs;
  }

  public long getTransactionId() {
    return transactionId;
  }

  public List<Creation> getCreations() {
    return creations;
  }

  public List<Input> getInputs() {
    return inputs;
  }

  public List<Output> getOutputs() {
    return outputs;
  }

  public byte[] getBytes() {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    byte[] bCreationsSize = Bitcoin.intToByteArray(creations.size());
    baos.write(bCreationsSize, 0, bCreationsSize.length);
    for (Creation creation : creations) {
      byte[] bCreation = creation.getBytes();
      byte[] bCreationLength = Bitcoin.intToByteArray(bCreation.length);
      baos.write(bCreationLength, 0, bCreationLength.length);
      baos.write(bCreation, 0, bCreation.length);
    }

    byte[] bInputsSize = Bitcoin.intToByteArray(inputs.size());
    baos.write(bInputsSize, 0, bInputsSize.length);
    for (Input input : inputs) {
      byte[] bInput = input.getBytes();
      byte[] bInputLength = Bitcoin.intToByteArray(bInput.length);
      baos.write(bInputLength, 0, bInputLength.length);
      baos.write(bInput, 0, bInput.length);
    }

    byte[] bOutputsSize = Bitcoin.intToByteArray(outputs.size());
    baos.write(bOutputsSize, 0, bOutputsSize.length);
    for (Output output : outputs) {
      byte[] bOutput = output.getBytes();
      byte[] bOutputLength = Bitcoin.intToByteArray(bOutput.length);
      baos.write(bOutputLength, 0, bOutputLength.length);
      baos.write(bOutput, 0, bOutput.length);
    }

    return baos.toByteArray();

  }

  public boolean signTransaction(BigInteger privateKey) {
    ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(privateKey, Bitcoin.EC_PARAMS);
    ECDSASigner signer = new ECDSASigner();
    signer.init(true, privateKeyParams);
    byte[] hashedTransaction = Bitcoin.hash256(getBytes());
    BigInteger[] rawSignature = signer.generateSignature(hashedTransaction);
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(72);
      DERSequenceGenerator derGen = new DERSequenceGenerator(baos);
      derGen.addObject(new ASN1Integer(rawSignature[0]));
      derGen.addObject(new ASN1Integer(rawSignature[1]));
      derGen.close();
      byte[] ecdsaSignature = baos.toByteArray();
      byte[] publicKey = Bitcoin.generatePublicKey(privateKey);
      String signature = Bitcoin.encodeBase58(ecdsaSignature) + " " + Bitcoin.encodeBase58(publicKey);
      for (Creation creation : creations) {
        creation.setSignature(signature);
      }
      for (Input input : inputs) {
        input.setSignature(signature);
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  // This method checks that all signatures in this transactions are correct
  // The transaction history is used to check which signatures are needed to use outputs from previous transactions
  public boolean verifySignatures(OutputHistory outputHistory) {

    // Get a hash of this transaction
    // This is the data that has been signed when the transaction was created
    byte[] hashedTransaction = Bitcoin.hash256(getBytes());

    // Check that the signatures of the creations are correct
    for (Creation creation : creations) {
      String creatorAddress = creation.getCreatorAddress();
      String signature = creation.getSignature();
      byte[] ecdsaSignature = Bitcoin.decodeBase58(signature.split(" ")[0]);
      byte[] publicKey = Bitcoin.decodeBase58(signature.split(" ")[1]);
      if (!Bitcoin.publicKeyToAddress(publicKey).equals(creatorAddress)) {
        return false;
      }
      ECPublicKeyParameters
          publicKeyParams =
          new ECPublicKeyParameters(Bitcoin.EC_PARAMS.getCurve().decodePoint(publicKey), Bitcoin.EC_PARAMS);
      ECDSASigner signer = new ECDSASigner();
      signer.init(false, publicKeyParams);
      try {
        ASN1InputStream derSigStream = new ASN1InputStream(ecdsaSignature);
        DLSequence seq = (DLSequence) derSigStream.readObject();
        BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getPositiveValue();
        BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getPositiveValue();
        derSigStream.close();
        if (!signer.verifySignature(hashedTransaction, r, s)) {
          return false;
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }

    // Put all outputs in the output history in a hash map for fast access
    HashMap<Long, Output> outputHistoryMap = new HashMap<>();
    for (Output output : outputHistory.getOutputList()) {
      outputHistoryMap.put(output.getOutputId(), output);
    }

    // Check that the signatures og the inputs are correct
    for (Input input : inputs) {
      long referredOutput = input.getReferredOutput();
      String receiverAddress = outputHistoryMap.get(referredOutput).getReceiverAddress();
      String signature = input.getSignature();
      byte[] ecdsaSignature = Bitcoin.decodeBase58(signature.split(" ")[0]);
      byte[] publicKey = Bitcoin.decodeBase58(signature.split(" ")[1]);
      if (!Bitcoin.publicKeyToAddress(publicKey).equals(receiverAddress)) {
        return false;
      }
      ECPublicKeyParameters
          publicKeyParams =
          new ECPublicKeyParameters(Bitcoin.EC_PARAMS.getCurve().decodePoint(publicKey), Bitcoin.EC_PARAMS);
      ECDSASigner signer = new ECDSASigner();
      signer.init(false, publicKeyParams);
      try {
        ASN1InputStream derSigStream = new ASN1InputStream(ecdsaSignature);
        DLSequence seq = (DLSequence) derSigStream.readObject();
        BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getPositiveValue();
        BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getPositiveValue();
        derSigStream.close();
        if (!signer.verifySignature(hashedTransaction, r, s)) {
          return false;
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }

    return true;
  }

  // This method checks that all outputs of previous transactions that are referred to in this transaction is unspent
  public boolean verifyInput(OutputHistory outputHistory) {

    // Put all outputs in the output history in a hash map for fast access
    HashMap<Long, Output> outputHistoryMap = new HashMap<>();
    for (Output output : outputHistory.getOutputList()) {
      outputHistoryMap.put(output.getOutputId(), output);
    }

    // Check that no referred output is already spent
    for (Input input : inputs) {
      long referredOutput = input.getReferredOutput();
      if (outputHistoryMap.get(referredOutput).getReferringInput() != 0) {
        return false;
      }
    }

    return true;
  }

  // This method checks that this transaction is not using more coupons than is available
  // Transaction history is used to check the number of coupons available via references to previous transactions
  public boolean verifyAmount(OutputHistory outputHistory) {

    // Map for counting coupons available in this transaction
    HashMap<Coupon, Integer> availableCoupons = new HashMap<>();

    // Count the number of coupons created in this transaction
    for (Creation creation : creations) {
      Coupon coupon = new Coupon(creation.getCreatorAddress(), creation.getPayload());
      if (availableCoupons.containsKey(coupon)) {
        int amount = availableCoupons.get(coupon);
        availableCoupons.put(coupon, amount + creation.getAmount());
      } else {
        availableCoupons.put(coupon, creation.getAmount());
      }
    }

    // Put all outputs in the output history in a hash map for fast access
    HashMap<Long, Output> outputHistoryMap = new HashMap<>();
    for (Output output : outputHistory.getOutputList()) {
      outputHistoryMap.put(output.getOutputId(), output);
    }

    // Count the number of existing coupons referred to by this transaction
    for (Input input : inputs) {
      long referredOutput = input.getReferredOutput();
      Coupon
          coupon =
          new Coupon(outputHistoryMap.get(referredOutput).getCreatorAddress(),
                     outputHistoryMap.get(referredOutput).getPayload());
      if (availableCoupons.containsKey(coupon)) {
        int amount = availableCoupons.get(coupon);
        availableCoupons.put(coupon, amount + outputHistoryMap.get(referredOutput).getAmount());
      } else {
        availableCoupons.put(coupon, outputHistoryMap.get(referredOutput).getAmount());
      }
    }

    // Map for counting coupons spent by this transaction
    HashMap<Coupon, Integer> spentCoupons = new HashMap<>();

    // Count the number of coupons spent by this transaction
    for (Output output : outputs) {
      Coupon coupon = new Coupon(output.getCreatorAddress(), output.getPayload());
      if (spentCoupons.containsKey(coupon)) {
        int amount = spentCoupons.get(coupon);
        spentCoupons.put(coupon, amount + output.getAmount());
      } else {
        spentCoupons.put(coupon, output.getAmount());
      }
    }

    // Check that spent coupons does not exceed available coupons for any creatorAddress
    for (Coupon coupon : spentCoupons.keySet()) {
      if (!availableCoupons.containsKey(coupon) || availableCoupons.get(coupon) < spentCoupons.get(coupon)) {
        return false;
      }
    }
    return true;
  }

  public static Transaction fromJson(String transactionJson) {
    return new Gson().fromJson(transactionJson, Transaction.class);
  }

  public static Transaction fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, Transaction.class);
  }

//  The transaction looks like this in json:
//  {
//    "transactionid":0,
//      "creations":[
//    {
//      "creationid":0,
//        "creatoraddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//        "amount":1,
//        "signature":"3046022100841b07fbeda96b5474e9465fbf1a105b6874b2f15479f74f59afb633e01a825a022100cc9a0ff991e7f0c9882057c96986d0fae326a478a6501d03c69e80dadc4ea7e5 047825706d44fc680a454aa4071b4df24c2087f2e48ab2d31e06fa58511b2872bd313c984a3dde16732cd9b995833ca41ecf1f1c5e1d57607134080b0d8fddcb72"
//    }
//    ],
//    "inputs":[
//
//    ],
//    "outputs":[
//    {
//      "outputid":0,
//        "creatoraddress":"d7b3e15eefbb19945b2671025c846ba18164abce",
//        "amount":1,
//        "address":"d7b3e15eefbb19945b2671025c846ba18164abce",
//        "inputid":0
//    }
//    ]
//  }

  public static String toJson(Transaction transaction) {
    return new Gson().toJson(transaction, Transaction.class);
  }

  public long getId() {
    return transactionId;
  }
}
