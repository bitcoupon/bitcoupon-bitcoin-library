package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.List;

/**
 * Created by Patrick on 23.09.2014.
 */
public class CouponList {
  List<Coupon> coupons;

  public static CouponList fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, CouponList.class);
  }

  public List<Coupon> getList() {
    return coupons;
  }
}

