package bitcoupon.transaction;

public class Coupon {

  private final String creatorAddress;
  private final String payload;

  public Coupon(String creatorAddress, String payload) {
    this.creatorAddress = creatorAddress;
    this.payload = payload;
  }

  public String getCreatorAddress() {
    return creatorAddress;
  }

  public String getPayload() {
    return payload;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Coupon) {
      Coupon other = (Coupon) obj;
      return creatorAddress.equals(other.creatorAddress) && payload.equals(other.payload);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return creatorAddress.hashCode() + payload.hashCode();
  }

}
