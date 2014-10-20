package bitcoupon.transaction;

import java.io.ByteArrayOutputStream;

import bitcoupon.Bitcoin;

/**
 * This class is used by transactions to refer to existing coupons that are sent.
 */
public class Input {

  /**
   * Id of this input. This is set by the server when the input is added to the database.
   */
  private final long inputId;
  /**
   * The id of the output that this input is referring to.
   */
  private final long referredOutput;
  /**
   * Signature verifying that it is the user who is the receiver of the coupons in referredOutput that is using the
   * coupons.
   */
  private String signature;

  /**
   * Constructor of an input.
   *
   * @param referredOutput Id of the output that this input is referring to.
   */
  public Input(long referredOutput) {
    this.inputId = 0;
    this.referredOutput = referredOutput;
    this.signature = "";
  }

  /**
   * This method is used to get a byte array representation of an input. Transaction uses this to get a byte array
   * representation for the entire transaction. This representation of a transaction is used when signing a
   * transaction.
   *
   * @return Byte array representation of this input.
   */
  public byte[] getBytes() {
    // Initiate byte array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // Add referredOutput to byte array
    byte[] bReferredOutput = Bitcoin.longToByteArray(referredOutput);
    baos.write(bReferredOutput, 0, bReferredOutput.length);
    // Return byte array
    return baos.toByteArray();
  }

  public long getInputId() {
    return inputId;
  }

  public long getReferredOutput() {
    return referredOutput;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getSignature() {
    return signature;
  }

}
