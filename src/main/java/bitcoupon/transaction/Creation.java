package bitcoupon.transaction;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import bitcoupon.Bitcoin;

/**
 * This class is used by transactions to specify coupons that are created.
 */
public class Creation {

  /**
   * Id of this creation. This is set by the server when the creation is added to the database.
   */
  private final long creationId;
  /**
   * The address of the creator of the coupons. This address needs to match the signature of the creation.
   */
  private final String creatorAddress;
  /**
   * The payload of the coupons that are created.
   */
  private final String payload;
  /**
   * The number of coupons that are created.
   */
  private final int amount;
  /**
   * Signature verifying that it is the owner of the address creatorAddress who are creating the coupons.
   */
  private String signature;

  /**
   * Constructor of a creation.
   *
   * @param creatorAddress Address of the creator of the coupons.
   * @param payload        Payload of the coupons that are created.
   * @param amount         Number of coupons that are created.
   */
  public Creation(String creatorAddress, String payload, int amount) {
    // Should this be changed to 'assert creatorAddress != null'?
    if (creatorAddress == null) {
      throw new IllegalArgumentException("Creator Address can't be null!");
    }
    // Should this be changed to 'assert payload != null'?
    if (payload == null) {
      throw new IllegalArgumentException("Payload can't be null?");
    }
    // Should this be changed to 'assert amount < 0'?
    if (amount < 0) {
      throw new IllegalArgumentException("Amount can't be negative!");
    }
    this.creationId = 0;
    this.creatorAddress = creatorAddress;
    this.payload = payload;
    this.amount = amount;
    this.signature = "";
  }

  /**
   * This method is used to get a byte array representation of a creation. Transaction uses this to get a byte array
   * representation for the entire transaction. This representation of a transaction is used when signing a
   * transaction.
   *
   * @return Byte array representation of this creation.
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
      // Return byte array
      return baos.toByteArray();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public long getCreationId() {
    return creationId;
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

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getSignature() {
    return signature;
  }

}
