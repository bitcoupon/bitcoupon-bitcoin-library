package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import java.io.BufferedReader;

import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.network.Network;

/**
 * Created by Patrick on 22.09.2014.
 */
public class Coupon {

  public static final String COUPON_JSON = "coupon_id";
  private static final String TAG = Coupon.class.getSimpleName();
  private final String couponAddress;
  private String title;
  private String description;
  private String id;
  private long created;
  private long modified;

  public Coupon(String couponAddress) {
    this.couponAddress = couponAddress;
    this.created = System.currentTimeMillis();
  }

  public String getCouponAddress() {
    return couponAddress;
  }

  public String getDescription() {
    return description;
  }

  public DateTime getCreated() {
    return new DateTime(created);
  }

  public DateTime getModified() {
    return new DateTime(modified);
  }

  public void setModified() {
    modified = new DateTime().getMillis();
  }

  public String getTitle() {
    return title;
  }

  public String getId() {
    return id;
  }


  public static Coupon createDummy() {
    Coupon dummy = new Coupon("1Kau4L6BM1h6QzLYubq1qWrQSjWdZFQgMb");
    dummy.id = String.valueOf((int) (Math.random() * 1000));
    dummy.description = "This is the dummy coupons' description!";
    dummy.title = "Dummy coupon";
    return dummy;
  }

  public void postInBackground(final CouponCallback<Coupon> callback) {
    Network.postCoupon(callback, Coupon.this);

  }

  public void spendInBackground(final CouponCallback<Coupon> callback) {
    Network.spendCoupon(callback, Coupon.this, id);
  }

  public static Coupon fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, Coupon.class);
  }


  public static Coupon fromJson(String json) {
    return new Gson().fromJson(json, Coupon.class);
  }

  public static String toJson(Coupon item) {
    return new Gson().toJson(item, Coupon.class);
  }

  @Override
  public String toString() {
    return "ID: " + getId() //
           + " Title: " + getTitle() //
           + " Created: " + getCreated() //
        ;
  }


  // Due to the object being serializable, override equals and hashcode to make sure an object remains the same before and after the serialization
  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31). // two random primes
        append(id.toCharArray()).
        toHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Coupon)) {
      return false;
    }
    return id.equals(((Coupon) o).getId());
  }

}
