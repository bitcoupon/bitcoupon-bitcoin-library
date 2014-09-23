package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.bitcoupon.callbacks.FetchAllCallback;
import no.ntnu.bitcoupon.callbacks.FetchCallback;

/**
 * Created by Patrick on 22.09.2014.
 */
public class Coupon {

  public static final String COUPON_JSON = "coupon_id";
  private static final String TAG = Coupon.class.getSimpleName();
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

  public static void fetchAllCoupons(final FetchAllCallback callback) {
    new AsyncTask<Void, Void, CouponList>() {
      @Override
      protected CouponList doInBackground(Void... params) {
        String url = "http://bitcoupon.no-ip.org:3002/backend/coupons/";
        HttpResponse response = null;
        try {
          HttpGet request = new HttpGet(new URI(url));
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

  public static void fetchCouponById(String id, final FetchCallback callback) {
    /**
     * Creates a default http client, executes a GET to the URL, and returns the response
     */
    new AsyncTask<String, Void, Coupon>() {
      @Override
      protected Coupon doInBackground(String... params) {
        String id = params[0];
        String url = "http://bitcoupon.no-ip.org:3002/backend/coupon/" + id;
        HttpResponse response = null;
        try {
          HttpGet request = new HttpGet(new URI(url));
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
}
