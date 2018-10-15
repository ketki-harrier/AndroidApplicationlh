package com.lifecyclehealth.lifecyclehealth.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.lifecyclehealth.lifecyclehealth.R;

public class BaseFragment extends Fragment {

    protected void replaceFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    protected void removeFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(fragment).commit();
    }

    protected void replaceFragmentWithBackStack(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getActivity().getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_main, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        } else {
            Toast.makeText(getActivity(), "Fragment is back-stack : " + fragmentPopped, Toast.LENGTH_SHORT).show();
        }
    }

}