package bitcoupon;

import org.spongycastle.asn1.x500.style.RFC4519Style;

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
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    byte[] bOutputId = Bitcoin.longToByteArray(outputId);
    baos.write(bOutputId, 0, bOutputId.length);

    return baos.toByteArray();
  }

  long getOutputId() {
    return outputId;
  }

  void setSignature(String signature) {
    this.signature = signature;
  }

  String getSignature() {
    return signature;
  }

}
