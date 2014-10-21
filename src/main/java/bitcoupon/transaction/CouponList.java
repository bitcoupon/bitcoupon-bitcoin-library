package bitcoupon.transaction;

import com.google.gson.Gson;

import java.util.List;

public class CouponList {

  private final List<Coupon> coupons;

  public CouponList(List<Coupon> coupons) {
    this.coupons = coupons;
  }

  public List<Coupon> getCoupons() {
    return coupons;
  }

  public static CouponList fromJson(String couponListJson) {
    return new Gson().fromJson(couponListJson, CouponList.class);
  }

  public static String toJson(CouponList couponList) {
    return new Gson().toJson(couponList, CouponList.class);
  }

}
