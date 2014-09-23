package no.ntnu.bitcoupon.callbacks;

import java.util.List;

import no.ntnu.bitcoupon.models.Coupon;

/**
 * Created by Patrick on 23.09.2014.
 */
public interface FetchAllCallback {

  void onComplete(int statusCode, List<Coupon> coupons);

  void onFail(int statusCode);

}
