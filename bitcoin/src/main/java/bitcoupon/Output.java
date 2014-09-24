package bitcoupon;

public class Output {

  private long outputId;
  private String couponType;
  private int amount;
  private String scriptPubKey;
  private long inputId;

  Output(long outputId, String couponType, int amount, String scriptPubKey, long inputId) {
    this.outputId = outputId;
    this.couponType = couponType;
    this.amount = amount;
    this.scriptPubKey = scriptPubKey;
    this.inputId = inputId;
  }

}
