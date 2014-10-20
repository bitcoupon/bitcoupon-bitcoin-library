package bitcoupon.transaction;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import bitcoupon.Bitcoin;

/**
 * This class is used by transactions to specify to whom coupons are sent.
 */
public class Output {

  /**
   * Id of this output. This is set by the server when the output is added to the database.
   */
  private final long outputId;
  /**
   * The address of the creator of the coupons that are sent.
   */
  private final String creatorAddress;
  /**
   * The payload of the coupons that are sent.
   */
  private final String payload;
  /**
   * The number of coupons that are sent.
   */
  private final int amount;
  /**
   * The address to which the coupons are sent.
   */
  private final String receiverAddress;
  /**
   * The id of the input that is referring to this output. If this is 0 then no input is referring to this output which
   * means that the output is unspent.
   */
  private final long referringInput;

  /**
   * Constructor of an output.
   *
   * @param creatorAddress  Address of the creator of the coupons that are sent.
   * @param payload         Payload of the coupons that are sent.
   * @param amount          Number of coupons that are sent.
   * @param receiverAddress Address to which the coupons are sent.
   */
  public Output(String creatorAddress, String payload, int amount, String receiverAddress) {
    this.outputId = 0;
    this.creatorAddress = creatorAddress;
    this.payload = payload;
    this.amount = amount;
    this.receiverAddress = receiverAddress;
    this.referringInput = 0;
  }

  /**
   * This method is used to get a byte array representation of an output. Transaction uses this to get a byte array
   * representation for the entire transaction. This representation of a transaction is used when signing a
   * transaction.
   *
   * @return Byte array representation of this output.
   */
  public byte[] getBytes() {
    try {
      // Initiate byte array
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      // Add creatorAddress to byte array
      byte[] bCreatorAddress = creatorAddress.getBytes("UTF-8");
      byte[] bCreatorAddressLength = Bitcoin.intToByteArray(bCreatorAddress.length);
      baos.write(bCreatorAddressLength, 0, bCreatorAddressLength.length);
      baos.write(bCreatorAddress, 0, bCreatorAddress.length);
      // Add payload to byte array
      byte[] bPayload = payload.getBytes("UTF-8");
      byte[] bPayloadLength = Bitcoin.intToByteArray(bPayload.length);
      baos.write(bPayloadLength, 0, bPayloadLength.length);
      baos.write(bPayload, 0, bPayload.length);
      // Add amount to byte array
      byte[] bAmount = Bitcoin.intToByteArray(amount);
      baos.write(bAmount, 0, bAmount.length);
      // Add receiverAddress to byte array
      byte[] bReceiverAddress = receiverAddress.getBytes("UTF-8");
      byte[] bReceiverAddressLength = Bitcoin.intToByteArray(bReceiverAddress.length);
      baos.write(bReceiverAddressLength, 0, bReceiverAddressLength.length);
      baos.write(bReceiverAddress, 0, bReceiverAddress.length);
      // Return byte array
      return baos.toByteArray();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public long getOutputId() {
    return outputId;
  }

  public String getCreatorAddress() {
    return creatorAddress;
  }

  public String getPayload() {
    return payload;
  }

  public int getAmount() {
    return amount;
  }

  public String getReceiverAddress() {
    return receiverAddress;
  }

  public long getReferringInput() {
    return referringInput;
  }

}
