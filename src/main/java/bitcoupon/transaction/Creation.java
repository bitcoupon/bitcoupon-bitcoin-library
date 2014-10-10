package bitcoupon.transaction;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import bitcoupon.Bitcoin;

/**
 * Creation is the class that handles all methods that has to do with the generateCreationTransaction.
 */
public class Creation {

  /**
   * creationId is the unique ID of the transaction in the database
   */
  private final long creationId;
  /**
   *creatorAddress is the address of the creator
   */
  private final String creatorAddress;
  /**
   * amount is the amount of coupons in the transaction
   */
  private final int amount;
  /**
   * signature is the digital signature of the creator. Virtually unforgeable.
   */
  private String signature;

  /**
   * The constructor of the Creation class.
   * @param creatorAddress: Address of the creator. Can't be null.
   * @param amount: The amount of coupons in the transaction. Can't be negative
   */
  public Creation(String creatorAddress, int amount) {

    // Should this be changed to 'assert creatorAddress != null'?
    if (creatorAddress == null) {
      throw new IllegalArgumentException("Creator Address can't be null!");
    }

    // Should this be changed to 'assert amount < 0'?
    if (amount < 0) {
      throw new IllegalArgumentException("Amount can't be negative!");
    }

    this.creationId = 0;
    this.amount = amount;
    this.creatorAddress = creatorAddress;
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
