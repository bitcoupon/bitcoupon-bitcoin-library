package no.ntnu.bitcoupon.network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import bitcoupon.Transaction;
import bitcoupon.TransactionHistory;
import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.models.Coupon;
import no.ntnu.bitcoupon.models.CouponList;

/**
 * Created by Patrick on 05.10.2014.
 */
public class Network {

  public static final String PRIVATE_KEY = "5K2RmiXi23ZgLK3QLTkzV2BP5VitbAyLMjYLVqJ2xYy5exSJJh2";
  public static final String CREATOR_ADDRESS = "1Kau4L6BM1h6QzLYubq1qWrQSjWdZFQgMb";
  public static final String API_ROOT = "http://bitcoupon.no-ip.org:3002/backend/";
  public static final String API_COUPONS = "coupons/";
  public static final String API_COUPON = "coupon/";
  private static final String API_TRANSACTION_HISTORY = "transaction_history";
  public static final String TAG = Network.class.getSimpleName();
  private static final String API_VERIFY_TRANSACTION = "verify_transaction";

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

  public static void fetchTransactionHistory(final CouponCallback<TransactionHistory> callback) {
    new AsyncTask<Void, Void, TransactionHistory>() {

      @Override
      protected TransactionHistory doInBackground(Void... params) {
        String url = API_ROOT + API_TRANSACTION_HISTORY;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpGet request = new HttpGet(new URI(url));
          request.addHeader(getRequestTokenHeader());
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(request);

          return TransactionHistory.fromJson(getReader(response));

        } catch (URISyntaxException e) {
          Log.e(TAG, "URISyntaxException", e);
        } catch (IOException e) {
          Log.e(TAG, "IOException", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(TransactionHistory transactionHistory) {
        if (transactionHistory != null) {
          callback.onComplete(0, transactionHistory);
        } else {
          callback.onFail(-1);
        }
      }
    }.execute();
  }

  public static Header getRequestTokenHeader() {
    Header header = new BasicHeader("Token", CREATOR_ADDRESS);
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

  public static BufferedReader getReader(HttpResponse response) throws IOException {
    InputStream is = response.getEntity().getContent();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    return reader;
  }

  public static void spendCoupon(final CouponCallback<Coupon> callback, final Coupon coupon, String id) {
    new AsyncTask<String, Void, Coupon>() {
      @Override
      protected Coupon doInBackground(String... params) {
        String id = params[0];
        String url = API_ROOT + API_COUPON + id;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpDelete delete = new HttpDelete(new URI(url));
          delete.addHeader(getRequestTokenHeader());

          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(delete);

          return coupon;

        } catch (Exception e) {
          Log.e(TAG, "xception", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Coupon coupon) {
        if (coupon == null) {
          callback.onComplete(HttpStatus.SC_OK, coupon);
        } else {
          callback.onFail(HttpStatus.SC_NO_CONTENT);
        }
      }
    }.execute(id);
  }

  public static void postCoupon(final CouponCallback<Coupon> callback, final Coupon coupon) {
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
          post.setEntity(new StringEntity(Coupon.toJson(coupon), "UTF-8"));
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
          callback.onComplete(HttpStatus.SC_OK, coupon);
        } else {
          callback.onFail(HttpStatus.SC_NO_CONTENT);
        }
      }
    }.execute();
  }

  public static void spendCoupon(final CouponCallback<Transaction> callback, final Transaction transaction) {
    new AsyncTask<Void, Void, Transaction>() {
      @Override
      protected Transaction doInBackground(Void... params) {
        String url = API_ROOT + API_VERIFY_TRANSACTION;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpPost post = new HttpPost(new URI(url));
          post.addHeader(getRequestTokenHeader());

          post.addHeader("Content-type", "application/json");
          post.setEntity(new StringEntity(Transaction.toJson(transaction), "UTF-8"));
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(post);

          return Transaction.fromJson(getReader(response));

        } catch (Exception e) {
          Log.e(TAG, "Exception", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Transaction transaction) {
        if (transaction != null) {
          callback.onComplete(HttpStatus.SC_OK, transaction);
        } else {
          callback.onFail(HttpStatus.SC_NO_CONTENT);
        }
      }
    }.execute();

  }
}
