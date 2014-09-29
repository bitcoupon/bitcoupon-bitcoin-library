package no.ntnu.bitcoupon.activities;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.fragments.CouponFragment;
import no.ntnu.bitcoupon.fragments.CouponListFragment;
import no.ntnu.bitcoupon.listeners.CouponFragmentListener;
import no.ntnu.bitcoupon.listeners.CouponListFragmentListener;
import no.ntnu.bitcoupon.models.Coupon;


/**
 * The MainActivity acts as an entry point for the application, and holds the fragments. It should handle all
 * communication between the fragments relevant to this activity.
 *
 * For the time being this is limited to the coupon couponListFragment and the list of coupons.
 */
public class MainActivity extends BaseActivity implements CouponListFragmentListener, CouponFragmentListener {

  private CouponListFragment couponListFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Crashlytics.start(this);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      couponListFragment = CouponListFragment.newInstance();
      getFragmentManager().beginTransaction().add(R.id.container, couponListFragment).commit();
    }
  }

  @Override
  public void onCouponClicked(Coupon coupon) {
    coupon.setModified();
    getFragmentManager().beginTransaction().replace(R.id.container, CouponFragment.newInstance(coupon))
        .addToBackStack(CouponFragment.TAG).commit();
  }

  @Override
  public void spendCoupon(final Coupon coupon) {
    coupon.spendInBackground(new CouponCallback<Coupon>() {
      @Override
      public void onComplete(int statusCode, Coupon coupon) {
        displayToast("Coupon with id " + coupon.getId() + " spent!");
        getFragmentManager().popBackStack();
        couponListFragment.removeCoupon(coupon);
      }

      @Override
      public void onFail(int statusCode) {
        displayToast("Failed to spend coupon with id " + coupon.getId());
        getFragmentManager().popBackStack();
      }
    });
  }
}
