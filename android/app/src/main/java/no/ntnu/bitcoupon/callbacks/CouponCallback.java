package no.ntnu.bitcoupon.callbacks;

/**
 * Callback for network requests from the server. Displays whether the request was successful or not.
 */
public interface CouponCallback<T> {

  /**
   * @param statusCode - HTTP status code received from the server
   * @param response   - An arbitrary object received from the server
   */
  void onSuccess(int statusCode, T response);

  /**
   * @param statusCode - HTTP status code received from the server
   */
  void onFail(int statusCode);

}
