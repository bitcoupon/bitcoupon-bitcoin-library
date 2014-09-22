package no.ntnu.bitcoupon.listeners;

import no.ntnu.bitcoupon.models.Coupon;

/**
 * Created by Patrick on 22.09.2014.
 *
 * Interface for anyone who wants to handle what the CouponListFragment does.
 *
 * Typically implemented by the mother activity, to handle communication between fragments.
 */
public interface CouponListFragmentListener {

  void onCouponClicked(Coupon id);
}
