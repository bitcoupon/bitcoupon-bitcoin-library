package bitcoupon.transaction;

public class CouponOwner {

  private final Coupon coupon;
  private final String ownerAddress;

  public CouponOwner(Coupon coupon, String ownerAddress) {
    this.coupon = coupon;
    this.ownerAddress = ownerAddress;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public String getOwnerAddress() {
    return ownerAddress;
  }

}
