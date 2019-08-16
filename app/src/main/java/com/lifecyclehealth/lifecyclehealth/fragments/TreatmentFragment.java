package com.lifecyclehealth.lifecyclehealth.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;

public class TreatmentFragment extends BaseFragmentWithOptions {
    private static final String STARTING_TEXT = "Four Buttons Bottom Navigation";
    MainActivity mainActivity;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 201;
    private Uri fileUri;


    public static SupportFragment newInstance() {
        //  Bundle args = new Bundle();
        // args.putString(STARTING_TEXT, text);

        SupportFragment supportFragment = new SupportFragment();
        //supportFragment.setArguments(args);

        return supportFragment;
    }

    @Override
    String getFragmentTag() {
        return "SupportFragment";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       /* TextView TextView = new TextView(getActivity());
        TextView.setText(getArguments().getString(STARTING_TEXT));

        return TextView;*/

        return inflater.inflate(R.layout.fragment_sample, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        backPage();

    }


    private void backPage() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    //Toast.makeText(getContext(),"back_arrow key pressed",Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    fm.popBackStack("SupportFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;

                }

                return false;
            }
        });

    }

}