package com.lifecyclehealth.lifecyclehealth.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;



public class SavedInstanceFragment extends Fragment {

    private static final String TAG = "SavedInstanceFragment";
    private Bundle mInstanceBundle = null;

    public SavedInstanceFragment() { // This will only be called once be cause of setRetainInstance()
        super();
        setRetainInstance( true );
    }



    public SavedInstanceFragment pushData( Bundle instanceState )
    {
        if ( this.mInstanceBundle == null ) {
            this.mInstanceBundle = instanceState;
        }
        else
        {
            this.mInstanceBundle.putAll( instanceState );
        }
        return this;
    }

    public Bundle popData()
    {
        Bundle out = this.mInstanceBundle;
        this.mInstanceBundle = null;
        return out;
    }

    public static final SavedInstanceFragment getInstance(FragmentManager fragmentManager )
    {
        SavedInstanceFragment out = (SavedInstanceFragment) fragmentManager.findFragmentByTag( TAG );

        if ( out == null )
        {
            out = new SavedInstanceFragment();
            try {
                fragmentManager.beginTransaction().add(out, TAG).commit();
            }catch (Exception e){e.printStackTrace();}
        }
        return out;
    }

}
