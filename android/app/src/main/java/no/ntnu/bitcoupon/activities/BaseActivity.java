package no.ntnu.bitcoupon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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
}
