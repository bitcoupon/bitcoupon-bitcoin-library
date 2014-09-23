package no.ntnu.bitcoupon.fragments;

import android.app.Fragment;

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
}
