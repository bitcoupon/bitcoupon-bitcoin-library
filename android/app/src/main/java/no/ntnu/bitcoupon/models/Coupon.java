package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import no.ntnu.bitcoupon.callbacks.FetchCallback;

/**
 * Created by Patrick on 22.09.2014.
 */
public class Coupon {

  public static final String COUPON_JSON = "coupon_id";
  private static final String TAG = Coupon.class.getSimpleName();
  private static final String API_ROOT = "http://bitcoupon.no-ip.org:3002/backend/";
  private static final String API_COUPONS = "coupons/";
  private static final String PUBLIC_KEY = "123123/";
  private static final String API_COUPON = "coupon/";
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

  public static void fetchAllCoupons(final FetchCallback<List<Coupon>> callback) {
    new AsyncTask<Void, Void, CouponList>() {
      @Override
      protected CouponList doInBackground(Void... params) {
        String url = API_ROOT + API_COUPONS;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpGet request = new HttpGet(new URI(url));
          request.addHeader(getRequestTokenHeader());
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(request);

        } catch (URISyntaxException e) {
          Log.e(TAG, "URISyntaxException", e);
        } catch (IOException e) {
          Log.e(TAG, "IOException", e);
        }
        return CouponList.fromJson(getReader(response));
      }

      @Override
      protected void onPostExecute(CouponList coupons) {
        callback.onComplete(0, coupons.getList());
      }
    }.execute();

  }

  public static Header getRequestTokenHeader() {
    Header header = new BasicHeader("Token", PUBLIC_KEY);
    header.toString();
    return header;
  }

  public static void fetchCouponById(String id, final FetchCallback<Coupon> callback) {
    /**
     * Creates a default http client, executes a GET to the URL, and returns the response
     */
    new AsyncTask<String, Void, Coupon>() {
      @Override
      protected Coupon doInBackground(String... params) {
        String id = params[0];
        String url = API_ROOT + API_COUPON + id;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpGet request = new HttpGet(new URI(url));
          request.addHeader(getRequestTokenHeader());
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(request);


        } catch (URISyntaxException e) {
          Log.e(TAG, "URISyntaxException", e);
        } catch (IOException e) {
          Log.e(TAG, "IOException", e);
        }
        return Coupon.fromJson(getReader(response));
      }

      @Override
      protected void onPostExecute(Coupon coupon) {
        callback.onComplete(0, coupon);
      }
    }.execute(id);

  }

  private static Coupon fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, Coupon.class);
  }

  private static BufferedReader getReader(HttpResponse response) {
    BufferedReader reader = null;
    try {
      InputStream is = response.getEntity().getContent();
      reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    } catch (IOException e) {
      Log.e(TAG, "IOException", e);
    }
    return reader;
  }

  @Override
  public String toString() {
    return "ID: " + getId() //
           + " Title: " + getTitle() //
           + " Created: " + getCreated() //
        ;
  }
  public void spend() {

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
