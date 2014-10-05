package bitcoupon.transaction;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import bitcoupon.Bitcoin;

public class Output {

  private final long outputId;
  private final String creatorAddress;
  private final int amount;
  private final String address;
  private final long inputId;

  public Output(String creatorAddress, int amount, String address) {
    this.outputId = 0;
    this.creatorAddress = creatorAddress;
    this.amount = amount;
    this.address = address;
    this.inputId = 0;
  }

  public byte[] getBytes() {
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

  public long getOutputId() {
    return outputId;
  }


  public long getInputId() {
    return inputId;
  }

  public String getCreatorAddress() {
    return creatorAddress;
  }

  public int getAmount() {
    return amount;
  }

  public String getAddress() {
    return address;
  }


}
