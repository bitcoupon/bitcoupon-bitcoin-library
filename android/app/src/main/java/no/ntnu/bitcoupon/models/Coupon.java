package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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

import no.ntnu.bitcoupon.callbacks.CouponCallback;

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

  public static void fetchAllCoupons(final CouponCallback<List<Coupon>> callback) {
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

          return CouponList.fromJson(getReader(response));

        } catch (URISyntaxException e) {
          Log.e(TAG, "URISyntaxException", e);
        } catch (IOException e) {
          Log.e(TAG, "IOException", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(CouponList coupons) {
        if (coupons != null) {
          callback.onComplete(0, coupons.getList());
        } else {
          callback.onFail(-1);
        }
      }
    }.execute();

  }

  public void postInBackground(final CouponCallback<Coupon> callback) {
    new AsyncTask<Void, Void, Coupon>() {
      @Override
      protected Coupon doInBackground(Void... params) {
        String url = API_ROOT + API_COUPONS;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpPost post = new HttpPost(new URI(url));
          post.addHeader(getRequestTokenHeader());

          post.addHeader("Content-type", "application/json");
          post.setEntity(new StringEntity(toJson(Coupon.this), "UTF-8"));
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(post);

          return Coupon.fromJson(getReader(response));

        } catch (Exception e) {
          Log.e(TAG, "Exception", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Coupon coupon) {
        if (coupon != null) {
          callback.onComplete(0, coupon);
        } else {
          callback.onFail(-1);
        }
      }
    }.execute();

  }

  public static Header getRequestTokenHeader() {
    Header header = new BasicHeader("Token", PUBLIC_KEY);
    header.toString();
    return header;
  }

  public static void fetchCouponById(String id, final CouponCallback<Coupon> callback) {
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

          return Coupon.fromJson(getReader(response));

        } catch (Exception e) {
          Log.e(TAG, "Exception", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Coupon coupon) {
        if (coupon != null) {
          callback.onComplete(0, coupon);
        } else {
          callback.onFail(-1);
        }
      }
    }.execute(id);

  }

  public void spendInBackground(final CouponCallback<Coupon> callback) {

    new AsyncTask<String, Void, Integer>() {
      @Override
      protected Integer doInBackground(String... params) {
        String id = params[0];
        String url = API_ROOT + API_COUPON + id;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpDelete delete = new HttpDelete(new URI(url));
          delete.addHeader(getRequestTokenHeader());

          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(delete);

          return response.getStatusLine().getStatusCode();

        } catch (Exception e) {
          Log.e(TAG, "xception", e);
        }
        return -1;
      }

      @Override
      protected void onPostExecute(Integer status) {
        callback.onComplete(status, Coupon.this);
      }
    }.execute(id);

  }

  private static Coupon fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, Coupon.class);
  }

  private static BufferedReader getReader(HttpResponse response) throws IOException {
    InputStream is = response.getEntity().getContent();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    return reader;
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
