package no.ntnu.bitcoupon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.models.Coupon;

/**
 * Created by Patrick on 22.09.2014.
 *
 * The coupon list adapter holds the actual coupons. The coupon list fragment uses this adapter as a model, and whenever
 * the adapter is updated, the list should also automatically update the view in the app.
 */
public class CouponListAdapter extends BaseAdapter {

  private static final String TAG = CouponListAdapter.class.getSimpleName();
  private final LayoutInflater mInflater;
  private final List<Coupon> items;

  public CouponListAdapter(Activity activity) {
    mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    items = new CopyOnWriteArrayList<Coupon>();
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public Coupon getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return createViewFromResource(position, convertView, parent, R.layout.coupon_item);
  }

  private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
    View view;
    if (convertView == null) {
      view = mInflater.inflate(resource, parent, false);
    } else {
      view = convertView;
    }

    TextView id = (TextView) view.findViewById(R.id.tv_coupon_id);
    TextView title = (TextView) view.findViewById(R.id.tv_coupon_title);
    TextView description = (TextView) view.findViewById(R.id.tv_coupon_description);
    TextView modified = (TextView) view.findViewById(R.id.tv_coupon_modified);
    TextView created = (TextView) view.findViewById(R.id.tv_coupon_created);

    Coupon coupon = getItem(position);
    id.setText(coupon.getId());
    title.setText(coupon.getTitle());
    created.setText(coupon.getCreated().toString());
    modified.setText(coupon.getModified().toString());
    description.setText(coupon.getDescription());

    if (description.getText().toString().trim().length() <= 0) {
      description.setVisibility(View.GONE);
    }

    return view;
  }


  public void add(Coupon dummy) {
    items.add(dummy);
  }

  public void remove(Coupon coupon) {
    items.remove(coupon);
  }

  public void clear() {
    items.clear();
  }
}
