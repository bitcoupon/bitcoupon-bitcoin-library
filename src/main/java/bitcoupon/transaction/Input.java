package bitcoupon.transaction;

import java.io.ByteArrayOutputStream;

import bitcoupon.Bitcoin;

public class Input {

  private final long inputId;
  private final long outputId;
  private String signature;

  public Input(long outputId) {
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

  long getInputId() {
    return inputId;
  }

  void setSignature(String signature) {
    this.signature = signature;
  }

  String getSignature() {
    return signature;
  }

}
