package bitcoupon;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class Creation {

  private long creationId;
  private String creatorPublicKey;
  private String subType;
  private int amount;
  private String signature;

  Creation(String creatorPublicKey, String subType, int amount) {
    this.creationId = 0;
    this.creatorPublicKey = creatorPublicKey;
    this.subType = subType;
    this.amount = amount;
    this.signature = "";
  }

  byte[] getBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] bCreationId = Bitcoin.longToByteArray(creationId);
      baos.write(bCreationId, 0, bCreationId.length);

      byte[] bScriptPubKey = creatorPublicKey.getBytes("UTF-8");
      byte[] bScriptPubKeyLength = Bitcoin.intToByteArray(bScriptPubKey.length);
      baos.write(bScriptPubKeyLength, 0, bScriptPubKeyLength.length);
      baos.write(bScriptPubKey, 0, bScriptPubKey.length);

      byte[] bSubType = subType.getBytes("UTF-8");
      byte[] bSubTypeLength = Bitcoin.intToByteArray(bSubType.length);
      baos.write(bSubTypeLength, 0, bSubTypeLength.length);
      baos.write(bSubType, 0, bSubType.length);

      byte[] bAmount = Bitcoin.intToByteArray(amount);
      baos.write(bAmount, 0, bAmount.length);

      byte[] bScriptSig = signature.getBytes("UTF-8");
      byte[] bScriptSigLength = Bitcoin.intToByteArray(bScriptSig.length);
      baos.write(bScriptSigLength, 0, bScriptSigLength.length);
      baos.write(bScriptSig, 0, bScriptSig.length);

      return baos.toByteArray();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  void setScriptSig(String scriptSig) {
    this.signature = scriptSig;
  }

}
