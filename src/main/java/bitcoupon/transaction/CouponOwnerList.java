package bitcoupon.transaction;

import com.google.gson.Gson;

import java.util.List;

public class CouponOwnerList {

  private List<CouponOwner> couponOwners;

  public CouponOwnerList(List<CouponOwner> couponOwners) {
    this.couponOwners = couponOwners;
  }

  public List<CouponOwner> getCouponOwners() {
    return couponOwners;
  }

  public static CouponOwnerList fromJson(String couponOwnerListJson) {
    return new Gson().fromJson(couponOwnerListJson, CouponOwnerList.class);
  }

  public static String toJson(CouponOwnerList couponOwnerList) {
    return new Gson().toJson(couponOwnerList, CouponOwnerList.class);
  }

}
