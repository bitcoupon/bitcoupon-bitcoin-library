package no.ntnu.bitcoupon.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.listeners.CouponFragmentListener;
import no.ntnu.bitcoupon.models.Coupon;

/**
 * Created by Patrick on 22.09.2014.
 *
 * The CouponFragment holds information about a specific coupon
 */
public class CouponFragment extends BaseFragment {

  public static final String TAG = CouponFragment.class.getSimpleName();
  private CouponFragmentListener mListener;

  public CouponFragment() {
  }

  public static Fragment newInstance(Coupon coupon) {
    CouponFragment fragment = new CouponFragment();
    Bundle args = new Bundle();
    args.putString(Coupon.COUPON_JSON, Coupon.toJson(coupon));
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (CouponFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement " + CouponFragmentListener.class.getSimpleName());
    }
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_coupon, container, false);

    TextView title = (TextView) view.findViewById(R.id.tv_coupon_title);
    TextView id = (TextView) view.findViewById(R.id.tv_coupon_id);
    TextView description = (TextView) view.findViewById(R.id.tv_coupon_description);
    TextView modified = (TextView) view.findViewById(R.id.tv_coupon_modified);
    TextView created = (TextView) view.findViewById(R.id.tv_coupon_created);
    TextView spendButton = (TextView) view.findViewById(R.id.b_spend_coupon);
    spendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        displayPromptDialog("Spend Coupon Confirmation", "Are you sure you want to spend this coupon?",
                            new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                  case DialogInterface.BUTTON_POSITIVE:
                                    Coupon coupon = Coupon.fromJson(getArguments().getString(Coupon.COUPON_JSON));
                                    mListener.onSpendCoupon(coupon);
                                    break;
                                }
                              }
                            });
      }
    });

    Coupon coupon = Coupon.fromJson(getArguments().getString(Coupon.COUPON_JSON));
    id.setText("ID: " + coupon.getId());
    title.setText(coupon.getTitle());
    created.setText("Created: " + coupon.getCreated());
    description.setText(Coupon.toJson(coupon));
    title.setText(coupon.getTitle());
    modified.setText("Modified:  " + coupon.getModified());
    return view;
  }

}

