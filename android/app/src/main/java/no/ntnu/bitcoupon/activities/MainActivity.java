package no.ntnu.bitcoupon.activities;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import bitcoupon.BitCoupon;
import bitcoupon.transaction.Transaction;
import bitcoupon.transaction.TransactionHistory;
import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.fragments.CouponFragment;
import no.ntnu.bitcoupon.fragments.CouponListFragment;
import no.ntnu.bitcoupon.listeners.CouponFragmentListener;
import no.ntnu.bitcoupon.listeners.CouponListFragmentListener;
import no.ntnu.bitcoupon.models.Coupon;
import no.ntnu.bitcoupon.network.Network;


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
    setLoading(true);
    Network.fetchTransactionHistory(new CouponCallback<TransactionHistory>() {
      @Override
      public void onComplete(int statusCode, TransactionHistory transactionHistory) {
        // Fetch the creator addesses. This is the "id" for the coupon, more or less
        List<String> creatorAddresses = BitCoupon.getCreatorAddresses(Network.PRIVATE_KEY,  //
                                                                      transactionHistory);
        String creatorAddress = null;
        for (String couponAddress : creatorAddresses) {
          if (couponAddress.equalsIgnoreCase(coupon.getCouponAddress())) {
            creatorAddress = couponAddress;
            break;
          }
        }

        // if no coupon with that address was found, display an error and return
        if (creatorAddress == null) {
          displayToast("Invalid coupon!");
          getFragmentManager().popBackStack();
          return;
        }

        // Generate the send transaction object
        Transaction transaction = BitCoupon.generateSendTransaction(Network.PRIVATE_KEY,  //
                                                                    creatorAddress, //
                                                                    Network.CREATOR_ADDRESS,  //
                                                                    transactionHistory);

        Network.spendCoupon(new CouponCallback<Transaction>() {
          @Override
          public void onComplete(int statusCode, Transaction response) {
            displayToast("Transaction: " + response.toString() + " spent!");
            getFragmentManager().popBackStack();
            couponListFragment.removeCoupon(response);
            couponListFragment.fetchAll();
          }

          @Override
          public void onFail(int statusCode) {
            displayToast("Failed to spend coupon with id " + coupon.getId());
            getFragmentManager().popBackStack();
          }

        }, transaction);
        setLoading(false);

      }

      @Override
      public void onFail(int statusCode) {
        displayToast("Failed to spend coupon with id " + coupon.getId());
        setLoading(false);
      }
    });
  }
}
