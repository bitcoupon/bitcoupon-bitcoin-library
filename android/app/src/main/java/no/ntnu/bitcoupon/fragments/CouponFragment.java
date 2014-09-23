package no.ntnu.bitcoupon.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.models.Coupon;

/**
 * Created by Patrick on 22.09.2014.
 *
 * The CouponFragment holds information about a specific coupon
 */
public class CouponFragment extends BaseFragment {

  public static final String TAG = CouponFragment.class.getSimpleName();

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
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_coupon, container, false);

    TextView title = (TextView) view.findViewById(R.id.tv_coupon_title);
    TextView id = (TextView) view.findViewById(R.id.tv_coupon_id);
    TextView description = (TextView) view.findViewById(R.id.tv_coupon_description);
    TextView modified = (TextView) view.findViewById(R.id.tv_coupon_modified);
    TextView created = (TextView) view.findViewById(R.id.tv_coupon_created);

    Coupon coupon = Coupon.fromJson(getArguments().getString(Coupon.COUPON_JSON));
    id.setText("ID: " + coupon.getId());
    title.setText(coupon.getTitle());
    created.setText("Created: " + coupon.getCreated());
    description.setText(coupon.getDescription());
    title.setText(coupon.getTitle());
    modified.setText("Modified:  " + coupon.getModified());
    return view;
  }
}

