package bitcoupon;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Transaction {

  private long transactionId;
  private ArrayList<Creation> creations;
  private ArrayList<Input> inputs;
  private ArrayList<Output> outputs;

  Transaction(ArrayList<Creation> creations, ArrayList<Input> inputs, ArrayList<Output> outputs) {
    this.transactionId = 0;
    this.creations = creations;
    this.inputs = inputs;
    this.outputs = outputs;
  }

  byte[] getBytes() {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    byte[] bTransactionId = Bitcoin.longToByteArray(transactionId);
    baos.write(bTransactionId, 0, bTransactionId.length);

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

  void signTransaction(String privateKey, String publicKey) {
    byte[] bTransaction = getBytes();
    byte[] bHashedTransaction = Bitcoin.hash256(bTransaction);
    X9ECParameters params = SECNamedCurves.getByName("secp256k1");
    ECDomainParameters ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
    ECDSASigner signer = new ECDSASigner();
  }

}
