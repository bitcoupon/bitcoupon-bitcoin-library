package bitcoupon;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class Output {

  private long outputId;
  private String couponType;
  private int amount;
  private String address;
  private long inputId;

  Output(String couponType, int amount, String address) {
    this.outputId = 0;
    this.couponType = couponType;
    this.amount = amount;
    this.address = address;
    this.inputId = 0;
  }

  byte[] getBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] bOutputId = Bitcoin.longToByteArray(outputId);
      baos.write(bOutputId, 0, bOutputId.length);

      byte[] bCouponType = couponType.getBytes("UTF-8");
      byte[] bCouponTypeLength = Bitcoin.intToByteArray(bCouponType.length);
      baos.write(bCouponTypeLength, 0, bCouponTypeLength.length);
      baos.write(bCouponType, 0, bCouponType.length);

      byte[] bAmount = Bitcoin.intToByteArray(amount);
      baos.write(bAmount, 0, bAmount.length);

      byte[] bScriptPubKey = address.getBytes("UTF-8");
      byte[] bScriptPubKeyLength = Bitcoin.intToByteArray(bScriptPubKey.length);
      baos.write(bScriptPubKeyLength, 0, bScriptPubKeyLength.length);
      baos.write(bScriptPubKey, 0, bScriptPubKey.length);

      byte[] bInputId = Bitcoin.longToByteArray(inputId);
      baos.write(bInputId, 0, bInputId.length);

      return baos.toByteArray();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

}
