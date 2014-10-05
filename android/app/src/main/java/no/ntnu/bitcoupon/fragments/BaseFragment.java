package no.ntnu.bitcoupon.fragments;

import android.app.Fragment;
import android.content.DialogInterface;

import no.ntnu.bitcoupon.activities.BaseActivity;

/**
 * Created by Patrick on 22.09.2014.
 *
 * * The main reason for the BaseFragment is to supply helper methods common to all fragments in the app.
 *
 * It also makes it easy to change the underlying Fragment implementation to one from the support library or similar, at
 * a later stage.
 */
public abstract class BaseFragment extends Fragment {

  public BaseActivity getBaseActivity() {
    return (BaseActivity) getActivity();
  }

  protected void setLoading(boolean b) {
    getBaseActivity().setLoading(b);
  }

  protected void displayToast(String message) {
    getBaseActivity().displayToast(message);
  }

  protected void displayErrorDialog(final String title, final String message) {
    getBaseActivity().displayErrorDialog(title, message);
  }


  protected void displayPromptDialog(final String title, final String question,
                                     final DialogInterface.OnClickListener dialogClickListener) {
    getBaseActivity().displayPromptDialog(title, question, dialogClickListener);
  }

  public void displayInputDialog(final String title, final String desc,
                                 final DialogInterface.OnClickListener listener) {
    getBaseActivity().displayInputDialog(title, desc, listener);
  }

  public String getInputText() {
    return getBaseActivity().getInputText();
  }
}
