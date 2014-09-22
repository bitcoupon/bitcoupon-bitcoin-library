package no.ntnu.bitcoupon.activities;

import android.os.Bundle;

import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.fragments.CouponFragment;
import no.ntnu.bitcoupon.fragments.CouponListFragment;
import no.ntnu.bitcoupon.listeners.CouponListFragmentListener;
import no.ntnu.bitcoupon.models.Coupon;


public class MainActivity extends BaseActivity implements CouponListFragmentListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction().add(R.id.container, CouponListFragment.newInstance()).commit();
    }
  }

  @Override
  public void onCouponClicked(Coupon coupon) {
    coupon.setModified();
    getFragmentManager().beginTransaction().replace(R.id.container, CouponFragment.newInstance(coupon)).addToBackStack(CouponFragment.TAG).commit();
  }
}
