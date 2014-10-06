package no.ntnu.bitcoupon.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.List;

import bitcoupon.BitCoupon;
import bitcoupon.transaction.Transaction;
import bitcoupon.transaction.TransactionHistory;
import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.adapters.CouponListAdapter;
import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.listeners.CouponListFragmentListener;
import no.ntnu.bitcoupon.models.Coupon;
import no.ntnu.bitcoupon.network.Network;

/**
 * The CouponListFragment holds and maintains a list of coupons.
 */
public class CouponListFragment extends BaseFragment implements AbsListView.OnItemClickListener {


  private static final String TAG = CouponListFragment.class.getSimpleName();
  private no.ntnu.bitcoupon.listeners.CouponListFragmentListener mListener;
  private AbsListView couponList;
  private CouponListAdapter couponAdapter;
  private PullToRefreshLayout mPullToRefreshLayout;

  public static CouponListFragment newInstance() {
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
    fetchAll();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_coupon_list, container, false);
    initializeRefreshOnDrag(view);

    View fetchAllButton = view.findViewById(R.id.b_fetch_all);

    View.OnClickListener fetchAllButtonListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        fetchAll();
      }
    };
    // set the button listeners
    fetchAllButton.setOnClickListener(fetchAllButtonListener);
    // Set the adapter
    couponList = (ListView) view.findViewById(R.id.coupon_list);
    couponList.setAdapter(couponAdapter);
    couponList.setEmptyView(view.findViewById(R.id.empty));

    // Set OnItemClickListener so we can be notified on item clicks
    couponList.setOnItemClickListener(this);

    return view;
  }

  public void fetchAll() {
    setLoading(true);
    Network.fetchTransactionHistory(new CouponCallback<TransactionHistory>() {
      @Override
      public void onSuccess(int statusCode, TransactionHistory transactionHistory) {
        List<String> addresses = BitCoupon.getCreatorAddresses(Network.PRIVATE_KEY, transactionHistory);
        couponAdapter.clear();
        int index = 1;
        for (String couponAddress : addresses) {
          couponAdapter.add(new Coupon(index++, couponAddress));
        }
        couponAdapter.notifyDataSetChanged();

        setLoading(false);
        displayToast("Received " + addresses.size() + " coupons from the server!");
        mPullToRefreshLayout.setRefreshComplete();
      }

      @Override
      public void onFail(int statusCode) {
        Log.v(TAG, "fetch failed: " + statusCode);
        setLoading(false);
        displayToast("Error fetching: " + statusCode);
        mPullToRefreshLayout.setRefreshComplete();
      }
    });

  }

  private void initializeRefreshOnDrag(View rootView) {
    // Now find the PullToRefreshLayout to setup
    mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);

    // Now setup the PullToRefreshLayout
    ActionBarPullToRefresh.from(getActivity()).options(Options.create().refreshOnUp(true).build())
        // Mark All Children as pullable
        .allChildrenArePullable()
            // Set a OnRefreshListener
        .listener(new OnRefreshListener() {
          @Override
          public void onRefreshStarted(View view) {
            fetchAll();
          }
        })
            // Finally commit the setup to our PullToRefreshLayout
        .setup(mPullToRefreshLayout);
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

  public void removeCoupon(Coupon coupon) {
    couponAdapter.remove(coupon);
    couponAdapter.notifyDataSetChanged();
  }

  public void removeCoupon(Transaction response) {

  }
}
