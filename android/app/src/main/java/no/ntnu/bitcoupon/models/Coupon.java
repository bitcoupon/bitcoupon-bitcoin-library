package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

/**
 * Created by Patrick on 22.09.2014.
 */
public class Coupon {

  public static final String COUPON_JSON = "coupon_id";
  private static final String TAG = Coupon.class.getSimpleName();
  private final int couponNumber;
  private final String couponAddress;
  private String title;
  private String description;
  private String id;
  private long created;
  private long modified;

  public Coupon(int couponNumber, String couponAddress) {
    this.couponNumber = couponNumber;
    this.couponAddress = couponAddress;
    this.created = System.currentTimeMillis();
  }

  public String getCouponAddress() {
    return couponAddress;
  }

  public String getDescription() {
    return couponAddress;
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
    return String.valueOf(couponNumber);
  }

  public String getId() {
    return id;
  }


  public static Coupon createDummy() {
    Coupon dummy = new Coupon(999, "1Kau4L6BM1h6QzLYubq1qWrQSjWdZFQgMb");
    dummy.id = String.valueOf((int) (Math.random() * 1000));
    dummy.description = "This is the dummy coupons' description!";
    dummy.title = "Dummy coupon";
    return dummy;
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
