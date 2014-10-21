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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

  public boolean verifySignatures(OutputHistory outputHistory) {
    for (Creation creation : creations) {
      if (!verifyCreationSignature(creation)) {
        return false;
      }
    }
    for (Input input : inputs) {
      if (!verifyInputSignature(input, outputHistory)) {
        return false;
      }
    }
    return true;
  }

  private boolean verifyCreationSignature(Creation creation) {
    byte[] hashedTransaction = Bitcoin.hash256(getBytes());
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
    return true;
  }

  private boolean verifyInputSignature(Input input, OutputHistory outputHistory) {
    byte[] hashedTransaction = Bitcoin.hash256(getBytes());
    HashMap<Long, Output> outputHistoryMap = new HashMap<>();
    for (Output output : outputHistory.getOutputList()) {
      outputHistoryMap.put(output.getOutputId(), output);
    }
    Output referredOutput = outputHistoryMap.get(input.getReferredOutput());
    if (referredOutput == null) {
      return false;
    }
    String receiverAddress = referredOutput.getReceiverAddress();
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
    return true;
  }

  public boolean verifyConsistency(OutputHistory outputHistory) {
    List<Coupon> availableCoupons = new LinkedList<Coupon>();
    for (Creation creation : creations) {
      if (creation.getAmount() <= 0) {
        return false;
      }
      for (int i = 0; i < creation.getAmount(); i++) {
        availableCoupons.add(new Coupon(creation.getCreatorAddress(), creation.getPayload()));
      }
    }
    Map<Long, Output> outputHistoryMap = new HashMap<Long, Output>();
    for (Output output : outputHistory.getOutputList()) {
      outputHistoryMap.put(output.getOutputId(), output);
    }
    for (Input input : inputs) {
      Output referredOutput = outputHistoryMap.get(input.getReferredOutput());
      if (referredOutput == null || referredOutput.getReferringInput() != 0) {
        return false;
      }
      for (int i = 0; i < referredOutput.getAmount(); i++) {
        availableCoupons.add(new Coupon(referredOutput.getCreatorAddress(), referredOutput.getPayload()));
      }
    }
    for (Output output : outputs) {
      if (output.getAmount() <= 0) {
        return false;
      }
      for (int i = 0; i < output.getAmount(); i++) {
        if (!availableCoupons.remove(new Coupon(output.getCreatorAddress(), output.getPayload()))) {
          return false;
        }
      }
    }
    return true;
  }

  public static Transaction fromJson(String transactionJson) {
    return new Gson().fromJson(transactionJson, Transaction.class);
  }

  public static String toJson(Transaction transaction) {
    return new Gson().toJson(transaction, Transaction.class);
  }

}
