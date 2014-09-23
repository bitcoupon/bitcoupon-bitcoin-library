package no.ntnu.bitcoupon.activities;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.fragments.CouponFragment;
import no.ntnu.bitcoupon.fragments.CouponListFragment;
import no.ntnu.bitcoupon.listeners.CouponListFragmentListener;
import no.ntnu.bitcoupon.models.Coupon;


/**
 * The MainActivity acts as an entry point for the application, and holds the fragments. It should handle all
 * communication between the fragments relevant to this activity.
 *
 * For the time being this is limited to the coupon fragment and the list of coupons.
 */
public class MainActivity extends BaseActivity implements CouponListFragmentListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Crashlytics.start(this);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction().add(R.id.container, CouponListFragment.newInstance()).commit();
    }
  }

  @Override
  public void onCouponClicked(Coupon coupon) {
    coupon.setModified();
    getFragmentManager().beginTransaction().replace(R.id.container, CouponFragment.newInstance(coupon))
        .addToBackStack(CouponFragment.TAG).commit();
  }
}
