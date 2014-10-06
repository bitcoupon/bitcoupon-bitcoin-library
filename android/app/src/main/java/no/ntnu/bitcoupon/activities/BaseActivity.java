package no.ntnu.bitcoupon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Patrick on 22.09.2014.
 *
 * The main reason for the BaseActivity is to supply helper methods common to all activities in the app.
 *
 * It also makes it easy to change the underlying activity implementation to one from the support library or similar, at
 * a later stage.
 */
public abstract class BaseActivity extends Activity {

  private static final String TAG = BaseActivity.class.getSimpleName();

  private int runningJobs;
  private EditText input;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    super.onCreate(savedInstanceState);
  }

  public void setLoading(final boolean loading) {
    if (loading) {
      ++runningJobs;
    } else {
      --runningJobs;
    }
    Log.d(TAG, loading ? " started" : " finished - Running jobs left: " + runningJobs);
    updateProgressbarState();
  }

  private void updateProgressbarState() {
    boolean shouldLoad = runningJobs > 0;
    setProgressBarIndeterminateVisibility(shouldLoad);
  }


  public void displayToast(final String toast) {
    Toast.makeText(BaseActivity.this, toast, Toast.LENGTH_SHORT).show();
  }

  public void displayErrorDialog(final String title, final String message) {

    AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
    builder.setMessage(message)//
        .setTitle(title) //
        .setPositiveButton(android.R.string.ok, null)//
        .create()//
        .show();//
  }


  public void displayPromptDialog(final String title, final String question,
                                  final DialogInterface.OnClickListener dialogClickListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
    builder.setTitle(title) //
        .setMessage(question)//
        .setPositiveButton(android.R.string.yes, dialogClickListener)//
        .setNegativeButton(android.R.string.no, dialogClickListener)//
        .create() //
        .show();
  }

  /**
   * Helper method to display an input dialog
   * @param title
   * @param desc
   * @param listener
   */
  public void displayInputDialog(final String title, final String desc,
                                 final DialogInterface.OnClickListener listener) {
    if (input != null) {
      // empty the input field from the last dialog
      input.setText("");
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    input = new EditText(this);
    builder.setView(input);
    builder.setTitle(title) //
        .setMessage(desc)//
        .setPositiveButton(android.R.string.yes, listener) //
        .create() //
        .show();
    showKeyboard(input);
    input.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
          hideKeyboard(input);
          return true;
        }
        return false;
      }
    });
  }

  /**
   * Helper method to display the software keyboard
   *
   * @param edit - the EditText field that should trigger the keyboard popup
   */
  public void showKeyboard(EditText edit) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);

    edit.dispatchTouchEvent(
        MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
    edit.dispatchTouchEvent(
        MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));

  }

  /**
   * Helper method to dismiss the software keyboard
   *
   * @param edit - the EditText field that should trigger the keyboard popup
   */
  public void hideKeyboard(EditText edit) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
  }

  public String getInputText() {
    return input.getText().toString().trim();
  }
}
