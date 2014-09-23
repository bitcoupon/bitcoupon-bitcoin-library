package no.ntnu.bitcoupon.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.adapters.CouponListAdapter;
import no.ntnu.bitcoupon.callbacks.FetchAllCallback;
import no.ntnu.bitcoupon.listeners.CouponListFragmentListener;
import no.ntnu.bitcoupon.models.Coupon;

/**
 * The CouponListFragment holds and maintains a list of coupons.
 */
public class CouponListFragment extends BaseFragment implements AbsListView.OnItemClickListener {


  private static final String TAG = CouponListFragment.class.getSimpleName();
  private no.ntnu.bitcoupon.listeners.CouponListFragmentListener mListener;
  private AbsListView couponList;
  private CouponListAdapter couponAdapter;

  public static Fragment newInstance() {
    CouponListFragment fragment = new CouponListFragment();
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation
   * changes).
   */
  public CouponListFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      // set arguments if there are any
    }

    couponAdapter = new CouponListAdapter(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_coupon_list, container, false);

    View generateButton = view.findViewById(R.id.b_generate);
    generateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Coupon.fetchAllCoupons(new FetchAllCallback() {
          @Override
          public void onComplete(int statusCode, List<Coupon> coupons) {
            for (Coupon coupon : coupons) {
              couponAdapter.add(coupon);
              Log.v(TAG, "fetch complete: " + statusCode);
            }
            couponAdapter.notifyDataSetChanged();
          }

          @Override
          public void onFail(int statusCode) {
            Log.v(TAG, "fetch failed: " + statusCode);
          }
        });
//        Coupon.fetchCouponById("2", new FetchCallback() {
//          @Override
//          public void onComplete(int statusCode, Coupon coupon) {
//            Log.v(TAG, "fetch complete: " + statusCode);
//            couponAdapter.add(coupon);
//            couponAdapter.notifyDataSetChanged();
//          }
//
//          @Override
//          public void onFail(int statusCode) {
//            Log.v(TAG, "fetch failed: " + statusCode);
//          }
//        });
      }
    });
    // Set the adapter
    couponList = (ListView) view.findViewById(R.id.coupon_list);
    couponList.setAdapter(couponAdapter);
    couponList.setEmptyView(view.findViewById(R.id.empty));

    // Set OnItemClickListener so we can be notified on item clicks
    couponList.setOnItemClickListener(this);

    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (no.ntnu.bitcoupon.listeners.CouponListFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement " + CouponListFragmentListener.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (null != mListener) {
      mListener.onCouponClicked(couponAdapter.getItem(position));
    }
  }
}
