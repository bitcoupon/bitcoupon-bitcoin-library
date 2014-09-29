package no.ntnu.bitcoupon.callbacks;

/**
 * Created by Patrick on 23.09.2014.
 */
public interface CouponCallback<T> {

  void onComplete(int statusCode, T coupon);

  void onFail(int statusCode);

}
