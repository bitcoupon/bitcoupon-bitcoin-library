package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import org.joda.time.DateTime;

/**
 * Created by Patrick on 22.09.2014.
 */
public class Coupon {

  public static final String COUPON_JSON = "coupon_id";
  private String title;
  private String description;
  private String id;
  private long created;
  private long modified;

  public Coupon() {
    this.created = System.currentTimeMillis();
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

  public static Coupon fromJson(String json) {
    return new Gson().fromJson(json, Coupon.class);
  }

  public static String toJson(Coupon item) {
    return new Gson().toJson(item, Coupon.class);
  }

  public static Coupon createDummy() {
    Coupon dummy = new Coupon();
    dummy.id = String.valueOf((int) (Math.random() * 1000));
    dummy.description = "This is the dummy coupons' description!";
    dummy.title = "Dummy coupon";
    return dummy;
  }

  @Override
  public String toString() {
    return "ID: " + getId() //
           + " Title: " + getTitle() //
           + " Created: " + getCreated() //
        ;
  }
}
