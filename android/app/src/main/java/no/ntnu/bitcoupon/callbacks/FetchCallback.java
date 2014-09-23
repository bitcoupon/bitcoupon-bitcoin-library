package no.ntnu.bitcoupon.callbacks;

import no.ntnu.bitcoupon.models.Coupon;

/**
 * Created by Patrick on 23.09.2014.
 */
public interface FetchCallback {

  void onComplete(int statusCode, Coupon coupon);

  void onFail(int statusCode);

}
