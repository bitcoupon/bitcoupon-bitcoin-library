package bitcoupon;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class Creation {

  private long creationId;
  private String creatorAddress;
  private int amount;
  private String signature;

  Creation(String creatorAddress, int amount) {
    this.creationId = 0;
    this.creatorAddress = creatorAddress;
    this.amount = amount;
    this.signature = "";
  }

  byte[] getBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] bCreationId = Bitcoin.longToByteArray(creationId);
      baos.write(bCreationId, 0, bCreationId.length);

      byte[] bScriptPubKey = creatorAddress.getBytes("UTF-8");
      byte[] bScriptPubKeyLength = Bitcoin.intToByteArray(bScriptPubKey.length);
      baos.write(bScriptPubKeyLength, 0, bScriptPubKeyLength.length);
      baos.write(bScriptPubKey, 0, bScriptPubKey.length);

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
