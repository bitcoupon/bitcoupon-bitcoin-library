package bitcoupon;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class Output {

  private long outputId;
  private String creatorAddress;
  private int amount;
  private String address;
  private long inputId;

  Output(String creatorAddress, int amount, String address) {
    this.outputId = 0;
    this.creatorAddress = creatorAddress;
    this.amount = amount;
    this.address = address;
    this.inputId = 0;
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

      byte[] bAddress = address.getBytes("UTF-8");
      byte[] bAddressLength = Bitcoin.intToByteArray(bAddress.length);
      baos.write(bAddressLength, 0, bAddressLength.length);
      baos.write(bAddress, 0, bAddress.length);

      return baos.toByteArray();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  long getOutputId() {
    return outputId;
  }


  long getInputId() {
    return inputId;
  }

  String getCreatorAddress() {
    return creatorAddress;
  }

  int getAmount() {
    return amount;
  }

  String getAddress() {
    return address;
  }


}
