package bitcoupon;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class Input {

  private long inputId;
  private long outputId;
  private String signature;

  Input(long outputId) {
    this.inputId = 0;
    this.outputId = outputId;
    this.signature = "";
  }

  byte[] getBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] bInputId = Bitcoin.longToByteArray(inputId);
      baos.write(bInputId, 0, bInputId.length);

      byte[] bOutputId = Bitcoin.longToByteArray(outputId);
      baos.write(bOutputId, 0, bOutputId.length);

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
