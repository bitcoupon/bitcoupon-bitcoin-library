package bitcoupon.transaction;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import bitcoupon.Bitcoin;

public class Creation {

  private final long creationId;
  private final String creatorAddress;
  private final int amount;
  private String signature;

  public Creation(String creatorAddress, int amount) {
    this.creationId = 0;
    this.creatorAddress = creatorAddress;
    this.amount = amount;
    this.signature = "";
  }

  byte[] getBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] bCreatorAddress = creatorAddress.getBytes("UTF-8");
      byte[] bCreatorAddressLength = Bitcoin.intToByteArray(bCreatorAddress.length);
      baos.write(bCreatorAddressLength, 0, bCreatorAddressLength.length);
      baos.write(bCreatorAddress, 0, bCreatorAddress.length);

      byte[] bAmount = Bitcoin.intToByteArray(amount);
      baos.write(bAmount, 0, bAmount.length);

      return baos.toByteArray();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  String getCreatorAddress() {
    return creatorAddress;
  }

  int getAmount() {
    return amount;
  }

  void setSignature(String signature) {
    this.signature = signature;
  }

  String getSignature() {
    return signature;
  }

}
