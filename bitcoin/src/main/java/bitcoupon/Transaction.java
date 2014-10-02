package bitcoupon;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Transaction {

  private static final ECDomainParameters EC_PARAMS;

  static {
    X9ECParameters params = SECNamedCurves.getByName("secp256k1");
    EC_PARAMS =
        new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH(), params.getSeed());
  }

  private long transactionId;
  private List<Creation> creations;
  private List<Input> inputs;
  private List<Output> outputs;

  Transaction(List<Creation> creations, List<Input> inputs, List<Output> outputs) {
    this.transactionId = 0;
    this.creations = creations;
    this.inputs = inputs;
    this.outputs = outputs;
  }

  List<Input> getInputs() {
    return inputs;
  }

  List<Output> getOutputs() {
    return outputs;
  }

  byte[] getBytes() {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    byte[] bCreationsSize = Bitcoin.intToByteArray(creations.size());
    baos.write(bCreationsSize, 0, bCreationsSize.length);
    for (int i = 0; i < creations.size(); i++) {
      byte[] bCreation = creations.get(i).getBytes();
      byte[] bCreationLength = Bitcoin.intToByteArray(bCreation.length);
      baos.write(bCreationLength, 0, bCreationLength.length);
      baos.write(bCreation, 0, bCreation.length);
    }

    byte[] bInputsSize = Bitcoin.intToByteArray(inputs.size());
    baos.write(bInputsSize, 0, bInputsSize.length);
    for (int i = 0; i < inputs.size(); i++) {
      byte[] bInput = inputs.get(i).getBytes();
      byte[] bInputLength = Bitcoin.intToByteArray(bInput.length);
      baos.write(bInputLength, 0, bInputLength.length);
      baos.write(bInput, 0, bInput.length);
    }

    byte[] bOutputsSize = Bitcoin.intToByteArray(outputs.size());
    baos.write(bOutputsSize, 0, bOutputsSize.length);
    for (int i = 0; i < outputs.size(); i++) {
      byte[] bOutput = outputs.get(i).getBytes();
      byte[] bOutputLength = Bitcoin.intToByteArray(bOutput.length);
      baos.write(bOutputLength, 0, bOutputLength.length);
      baos.write(bOutput, 0, bOutput.length);
    }

    return baos.toByteArray();

  }

  boolean signTransaction(BigInteger privateKey) {
    ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(privateKey, EC_PARAMS);
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
      for (int i = 0; i < creations.size(); i++) {
        creations.get(i).setSignature(signature);
      }
      for (int i = 0; i < inputs.size(); i++) {
        inputs.get(i).setSignature(signature);
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  // This method checks that all signatures in this transactions are correct
  // The transaction history is used to check which signatures are needed to use outputs from previous transactions
  boolean verifySignatures(List<Transaction> transactionHistory) {

    // Get a hash of this transaction
    // This is the data that has been signed when the transaction was created
    byte[] hashedTransaction = Bitcoin.hash256(getBytes());

    // Check that the signatures og the creations are correct
    for (int i = 0; i < creations.size(); i++) {
      Creation creation = creations.get(i);
      String creatorAddress = creation.getCreatorAddress();
      String signature = creation.getSignature();
      byte[] ecdsaSignature = Bitcoin.decodeBase58(signature.split(" ")[0]);
      byte[] publicKey = Bitcoin.decodeBase58(signature.split(" ")[1]);

      // Check that the public key in the signature matches the address of the creator of the coupons
      if (Bitcoin.publicKeyToAddress(publicKey).equals(creatorAddress)) {
        ECPublicKeyParameters
            publicKeyParams =
            new ECPublicKeyParameters(EC_PARAMS.getCurve().decodePoint(publicKey), EC_PARAMS);
        ECDSASigner signer = new ECDSASigner();
        signer.init(false, publicKeyParams);
        try {
          ASN1InputStream derSigStream = new ASN1InputStream(ecdsaSignature);
          DLSequence seq = (DLSequence) derSigStream.readObject();
          BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getPositiveValue();
          BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getPositiveValue();
          derSigStream.close();

          // Check that the signature is correct
          if (!signer.verifySignature(hashedTransaction, r, s)) {
            return false;
          }
        } catch (IOException e) {
          e.printStackTrace();
          return false;
        }
      } else {
        return false;
      }
    }

    // Put all outputs in the transaction history in a hash map for fast access
    HashMap<Long, Output> outputMap = new HashMap<Long, Output>();
    for (int i = 0; i < transactionHistory.size(); i++) {
      List<Output> outputHistory = transactionHistory.get(i).getOutputs();
      for (int j = 0; j < outputHistory.size(); j++) {
        outputMap.put(outputHistory.get(j).getOutputId(), outputHistory.get(j));
      }
    }

    // Check that the signatures og the inputs are correct
    for (int i = 0; i < inputs.size(); i++) {
      Input input = inputs.get(i);
      long outputId = input.getOutputId();
      Output referredOutput = outputMap.get(outputId);
      String address = referredOutput.getAddress();
      String signature = input.getSignature();
      byte[] ecdsaSignature = Bitcoin.decodeBase58(signature.split(" ")[0]);
      byte[] publicKey = Bitcoin.decodeBase58(signature.split(" ")[1]);

      // Check that the public key in the signature matches the address that the coupons was sent to
      if (Bitcoin.publicKeyToAddress(publicKey).equals(address)) {
        ECPublicKeyParameters
            publicKeyParams =
            new ECPublicKeyParameters(EC_PARAMS.getCurve().decodePoint(publicKey), EC_PARAMS);
        ECDSASigner signer = new ECDSASigner();
        signer.init(false, publicKeyParams);
        try {
          ASN1InputStream derSigStream = new ASN1InputStream(ecdsaSignature);
          DLSequence seq = (DLSequence) derSigStream.readObject();
          BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getPositiveValue();
          BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getPositiveValue();
          derSigStream.close();

          // Check that the signature is correct
          if (!signer.verifySignature(hashedTransaction, r, s)) {
            return false;
          }
        } catch (IOException e) {
          e.printStackTrace();
          return false;
        }
      } else {
        return false;
      }
    }
    return true;
  }

  // This method checks that all outputs of previous transactions that are referred to in this transaction is unspent
  boolean verifyInput(List<Transaction> transactionHistory) {

    // Put all outputs in the transaction history in a hash map for fast access
    HashMap<Long, Output> outputMap = new HashMap<Long, Output>();
    for (int i = 0; i < transactionHistory.size(); i++) {
      List<Output> outputHistory = transactionHistory.get(i).getOutputs();
      for (int j = 0; j < outputHistory.size(); j++) {
        outputMap.put(outputHistory.get(j).getOutputId(), outputHistory.get(j));
      }
    }

    // Check that no referred output is already spent
    for (int i = 0; i < inputs.size(); i++) {
      Input input = inputs.get(i);
      long outputId = input.getOutputId();
      Output referredOutput = outputMap.get(outputId);
      if (referredOutput.getInputId() != 0) {
        return false;
      }
    }
    return true;
  }

  // This method checks that this transaction is not using more coupons than is available
  // Transaction history is used to check the number of coupons available via references to previous transactions
  boolean verifyAmount(List<Transaction> transactionHistory) {

    // Map for counting coupons available in this transaction
    HashMap<String, Integer> availableCoupons = new HashMap<String, Integer>();

    // Count the number of coupons created in this transaction
    for (int i = 0; i < creations.size(); i++) {
      Creation creation = creations.get(i);
      if (availableCoupons.containsKey(creation.getCreatorAddress())) {
        Integer amount = availableCoupons.get(creation.getCreatorAddress());
        availableCoupons.put(creation.getCreatorAddress(), amount + creation.getAmount());
      } else {
        availableCoupons.put(creation.getCreatorAddress(), creation.getAmount());
      }
    }

    // Put all outputs in the transaction history in a hash map for fast access
    HashMap<Long, Output> outputMap = new HashMap<Long, Output>();
    for (int i = 0; i < transactionHistory.size(); i++) {
      List<Output> outputHistory = transactionHistory.get(i).getOutputs();
      for (int j = 0; j < outputHistory.size(); j++) {
        outputMap.put(outputHistory.get(j).getOutputId(), outputHistory.get(j));
      }
    }

    // Count the number of existing coupons referred to by this transaction
    for (int i = 0; i < inputs.size(); i++) {
      Input input = inputs.get(i);
      long outputId = input.getOutputId();
      Output referredOutput = outputMap.get(outputId);
      if (availableCoupons.containsKey(referredOutput.getCreatorAddress())) {
        Integer amount = availableCoupons.get(referredOutput.getCreatorAddress());
        availableCoupons.put(referredOutput.getCreatorAddress(), amount + referredOutput.getAmount());
      } else {
        availableCoupons.put(referredOutput.getCreatorAddress(), referredOutput.getAmount());
      }
    }

    // Map for counting coupons spent by this transaction
    HashMap<String, Integer> spentCoupons = new HashMap<String, Integer>();

    // Count the number of coupons spent by this transaction
    for (int i = 0; i < outputs.size(); i++) {
      Output output = outputs.get(i);
      if (spentCoupons.containsKey(output.getCreatorAddress())) {
        Integer amount = spentCoupons.get(output.getCreatorAddress());
        spentCoupons.put(output.getCreatorAddress(), amount + output.getAmount());
      } else {
        spentCoupons.put(output.getCreatorAddress(), output.getAmount());
      }
    }

    // Check that spent coupons does not exceed available coupons for any creatorAddress
    Iterator<String> itrSpentCoupons = spentCoupons.keySet().iterator();
    while (itrSpentCoupons.hasNext()) {
      String creatorAddress = itrSpentCoupons.next();
      if (!availableCoupons.containsKey(creatorAddress) || availableCoupons.get(creatorAddress) < spentCoupons
          .get(creatorAddress)) {
        return false;
      }
    }
    return true;
  }

  public static Transaction fromJson(String transactionJson) {
    return new Gson().fromJson(transactionJson, Transaction.class);
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

}
