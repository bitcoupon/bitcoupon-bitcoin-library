package no.ntnu.bitcoupon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

  public void displayToast(final String toast) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(BaseActivity.this, toast, Toast.LENGTH_LONG).show();
      }
    });
  }

  public void displayErrorDialog(final String title, final String message) {

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setMessage(message)//
            .setTitle(title) //
            .setPositiveButton(android.R.string.ok, null)//
            .create()//
            .show();//
      }
    });
  }

  public void displayPromptDialog(final String title, final String question,
                                  final DialogInterface.OnClickListener dialogClickListener) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle(title) //
            .setMessage(question)//
            .setPositiveButton(android.R.string.yes, dialogClickListener)//
            .setNegativeButton(android.R.string.no, dialogClickListener)//
            .create() //
            .show();
      }
    });
  }
}
