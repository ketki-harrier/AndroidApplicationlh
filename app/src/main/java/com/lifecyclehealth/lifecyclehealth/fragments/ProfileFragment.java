package com.lifecyclehealth.lifecyclehealth.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.CheckUser;
import com.lifecyclehealth.lifecyclehealth.dto.SubmitProfileImageDTO;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintUtil;
import com.lifecyclehealth.lifecyclehealth.image.ChoosePhoto;
import com.lifecyclehealth.lifecyclehealth.image.FileUtil;
import com.lifecyclehealth.lifecyclehealth.model.ChangePasswordResponse;
import com.lifecyclehealth.lifecyclehealth.model.ProfileGetImageResponse;
import com.lifecyclehealth.lifecyclehealth.model.ProfileGetResponse;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.GalleryUtil;
import com.lifecyclehealth.lifecyclehealth.utils.LegacyCompatFileProvider;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOCAL_DB_TOUCH;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_NAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_NAMECAREGIVER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHANGE_TOUCH_STATE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHECK_IS_USER_TOUCH_ACCEPT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_PROFILE_GETDATA;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_PROFILE_GETIMAGE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_PROFILE_SETIMAGE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

public class ProfileFragment extends BaseFragmentWithOptions implements FingerPrintCallback, FingerPrintUtil.NextActivityCallback {

    private MainActivity mainActivity;
    private CircularImageView imageView2;
    EditText editTextPreferredName, editTextFirstName, editTextLastName, editTextEmail, editTextMobileNo, editTextTitle, editTextSuffix;
    Switch switch1;
    Button changePassword, btnCaregiver,btnCaregiverFor;
    ImageView imageButton;
    TextView categiverHeading;
    String name, fileType;
    boolean isPatient;
    private static String SHARED_PROVIDER_AUTHORITY;
    private FingerPrintUtil fingerPrintUtil;
    // private static final String SHARED_PROVIDER_AUTHORITY =AchievementsDetails.this.getApplicationContext().getPackageName() + "."


    @Override
    String getFragmentTag() {
        return "ProfileFragment";
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SHARED_PROVIDER_AUTHORITY = ProfileFragment.this.getContext().getPackageName() + ".my.package.name.provider";
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file_show for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getBooleanFromSharedPreference(LOCAL_DB_TOUCH)) {
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    backPage();
                    return true;
                }
                return false;
            }
        });
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);
    }

    private void initializeView(View view) {
        Analytics.with(getContext()).screen("My Personal Profile ");
        fingerPrintUtil = new FingerPrintUtil(getActivity(), this, this);
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        imageView2 = (CircularImageView) view.findViewById(R.id.imageView2);
        editTextPreferredName = (EditText) view.findViewById(R.id.editTextPreferredName);
        editTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) view.findViewById(R.id.editTextLastName);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextMobileNo = (EditText) view.findViewById(R.id.editTextMobileNo);
        editTextSuffix = (EditText) view.findViewById(R.id.editTextSuffix);
        editTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
        switch1 = (Switch) view.findViewById(R.id.switch1);
        changePassword = (Button) view.findViewById(R.id.changePassword);
        btnCaregiver = (Button) view.findViewById(R.id.btnCaregiver);
        btnCaregiverFor = (Button) view.findViewById(R.id.btnCaregiverFor);
        changePassword.setOnClickListener(changePasswordClickListener);
        btnCaregiver.setOnClickListener(caregiverClickListener);
        btnCaregiverFor.setOnClickListener(caregiverForClickListener);
        imageButton = (ImageView) view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(editImage);
        categiverHeading = (TextView) view.findViewById(R.id.categiverHeading);
        //checkIsUserTouchScreen();
        if (MyApplication.getInstance().getBooleanFromSharedPreference(LOGIN_NAMECAREGIVER)) {
            categiverHeading.setVisibility(View.VISIBLE);
            try {
                categiverHeading.setText("You are logged in as Caregiver for [" + AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_NAME)) + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            categiverHeading.setVisibility(View.GONE);
        }
        if (isPatient) {
            btnCaregiver.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.GONE);

        } else {
            btnCaregiver.setVisibility(View.GONE);
            imageButton.setVisibility(View.VISIBLE);
            // imageButton.setVisibility(View.GONE);

        }
        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
        TextView back = (TextView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        setTouchSwitch();

        getProfile_ImageData("data");
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (switch1.isChecked()) {
                        switch1.setChecked(false);
                        TouchIDFragment touchIDFragment = new TouchIDFragment();
                        replaceFragment(touchIDFragment);

                        Fragment fragment = new TouchIDFragment();
                        String fragmentTag = fragment.getClass().getName();
                        printLog("tag" + fragmentTag);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                                .replace(R.id.content_main, fragment, fragmentTag)
                                .addToBackStack("TouchIDFragment")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    } else {
                        changeTouchEnable("false");
                    }
              /*  } else {
                    if (switch1.isChecked()) {
                        switch1.setChecked(false);
                    } else {
                        switch1.setChecked(true);
                    }
                    showDialogWithOkButton("This device does not have a TouchID sensor.");
                }*/

            }
        });

        if (MyApplication.getInstance().getBooleanFromSharedPreference(LOCAL_DB_TOUCH)) {
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
        }

    }

    private void setTouchSwitch() {
        try {
            String touchEmailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.TOUCH_EMAIL_ID));
            String emailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID));

            if (touchEmailId.trim().equals("")) {
                switch1.setVisibility(View.VISIBLE);
            } else {
                if (touchEmailId.trim().equals(emailId.trim())) {
                    switch1.setVisibility(View.VISIBLE);
                } else {
                    switch1.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void changeTouchEnable(String touch) {
        showProgressDialog(true);

        if (isConnectedToNetwork(mainActivity)) {
            mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_CHANGE_TOUCH_STATE + touch, null, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    showProgressDialog(false);
                    printLog("ResponseTouch state response" + response);
                    SurveySubmitResponse response1 = new Gson().fromJson(response.toString(), SurveySubmitResponse.class);
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                            switch1.setChecked(false);
                            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, false);
                            try {
                                MyApplication.getInstance().addToSharedPreference(TOUCH_EMAIL_ID, AESHelper.encrypt(seedValue, ""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            switch1.setChecked(true);
                            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, true);
                            showDialogWithOkButton(response1.getMessage());
                        }
                    } else {
                        showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    showProgressDialog(false);
                }
            });
        } else {
            showProgressDialog(false);
            showDialogWithOkButton(getString(R.string.error_no_network));
        }
    }


    private void getProfile_ImageData(final String data) {
        String url;
        if (data.equals("data")) {
            showProgressDialog(true);
            url = BASE_URL + URL_PROFILE_GETDATA;
        } else {
            url = BASE_URL + URL_PROFILE_GETIMAGE;
        }

        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get profile:" + response);
                        if (data.equals("data")) {
                            if (response != null) {
                                ProfileGetResponse profileGetResponse = new Gson().fromJson(response.toString(), ProfileGetResponse.class);
                                if (profileGetResponse != null) {
                                    if (profileGetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                        editTextFirstName.setText(profileGetResponse.getUser().getUser_details().getFirstName());
                                        editTextLastName.setText(profileGetResponse.getUser().getUser_details().getLastName());
                                        editTextEmail.setText(profileGetResponse.getUser().getUser_details().getUsername());
                                        editTextMobileNo.setText(profileGetResponse.getUser().getUser_details().getMobilePhone());
                                        editTextPreferredName.setText(profileGetResponse.getUser().getUser_details().getPreferred_Name());
                                        editTextTitle.setText(profileGetResponse.getUser().getUser_details().getName_Title());
                                        editTextSuffix.setText(profileGetResponse.getUser().getUser_details().getName_Suffix());
                                        getProfile_ImageData("image");
                                    } else {
                                        //showDialogWithOkButton(meetListDTO.getMessage());
                                    }
                                } else
                                    showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                            }
                        } else {
                            ProfileGetImageResponse profileGetResponse = new Gson().fromJson(response.toString(), ProfileGetImageResponse.class);
                            if (profileGetResponse != null) {
                                if (profileGetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    byte[] imageAsBytes = Base64.decode(profileGetResponse.getImageData(), Base64.DEFAULT);

                                    imageView2.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                   /* Bitmap bmp = BitmapFactory.decodeByteArray(profileGetResponse.getImageData(), 0, profileGetResponse.getImageData().length);
                                    imageView2.setImageBitmap(bmp);*/
                                } else {
                                    //showDialogWithOkButton(meetListDTO.getMessage());
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }


    private void backPage() {
  /*      FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("ProfileFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
        getFragmentManager().popBackStack();

    }

    // ChoosePhoto choosePhoto=null;
    private View.OnClickListener changePasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          /*  ChangePassword changePassword = new ChangePassword();
            replaceFragment(changePassword);*/
            Fragment fragment = new ChangePassword();
            String fragmentTag = fragment.getClass().getName();
            printLog("tag" + fragmentTag);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                    .replace(R.id.content_main, fragment, fragmentTag)
                    .addToBackStack("TouchIDFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    };
    private View.OnClickListener caregiverClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProfileCaregiver profileCaregiver = new ProfileCaregiver();
            replaceFragment(profileCaregiver);
        }
    };

    private View.OnClickListener caregiverForClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProfileCaregiverFor profileCaregiver = new ProfileCaregiverFor();
            replaceFragment(profileCaregiver);
        }
    };

    private View.OnClickListener editImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //dialogForImageOption();
            //choosePhoto= new ChoosePhoto(getContext());
            Analytics.with(getContext()).track("update Profile Picture", new Properties().putValue("category", "Mobile"));
            showAlertDialog();
        }
    };


    private void submitProfileData(Bitmap bitmap) {
        showProgressDialog(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        SubmitProfileImageDTO imageDTO = new SubmitProfileImageDTO();
        imageDTO.setContentType(fileType);
        imageDTO.setFileName(name);
        imageDTO.setImageData(base64String);


        if (isConnectedToNetwork(mainActivity)) {
            try {
                final JSONObject requestJson = new JSONObject(new Gson().toJson(imageDTO));
                mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_PROFILE_SETIMAGE, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of set image:" + response);

                        if (response != null) {
                            ChangePasswordResponse profileGetResponse = new Gson().fromJson(response.toString(), ChangePasswordResponse.class);
                            if (profileGetResponse != null) {
                                if (profileGetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    showDialogWithOkButton(profileGetResponse.getMessage());
                                } else {
                                    showDialogWithOkButton(profileGetResponse.getMessage());
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }

    public static int CHOOSE_PHOTO_INTENT = 101;
    public static int SELECTED_IMG_CROP = 102;
    public static int SELECT_PICTURE_CAMERA = 103;
    public static int currentAndroidDeviceVersion = Build.VERSION.SDK_INT;

    private int ASPECT_X = 1;
    private int ASPECT_Y = 1;
    private int OUT_PUT_X = 300;
    private int OUT_PUT_Y = 300;
    private boolean SCALE = true;

    private Uri cropPictureUrl, selectedImageUri = null, cameraUrl = null;

    public void showAlertDialog() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        cameraUrl = FileUtil.getInstance(getContext()).createImageUri();
        //Create any other intents you want
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUrl);


        //Add them to an intent array
        Intent[] intents = new Intent[]{cameraIntent};

        //Create a choose from your first intent then pass in the intent array
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);

        startActivityForResult(chooserIntent, CHOOSE_PHOTO_INTENT);
    }

    public void handleGalleryResult(Intent data) {
        try {
            cropPictureUrl = Uri.fromFile(FileUtil.getInstance(getContext())
                    .createImageTempFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));

            //String realPathFromURI = getRealPathFromURI(getContext(), cropPictureUrl);
            name = cropPictureUrl.toString().substring(cropPictureUrl.toString().lastIndexOf("/") + 1);
            fileType = getMimeType(name);

            printLog("name" + name);
            cropImage(data.getData(), cropPictureUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCameraResult(Uri cameraPictureUrl) {
        try {
            cropPictureUrl = Uri.fromFile(FileUtil.getInstance(getContext())
                    .createImageTempFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
            name = cropPictureUrl.toString().substring(cropPictureUrl.toString().lastIndexOf("/") + 1);
            fileType = getMimeType(name);

            printLog("name" + name);

            cropImage(cameraPictureUrl, cropPictureUrl);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Uri getCameraUri() {
        return cameraUrl;
    }

    public Uri getCropImageUrl() {
        return selectedImageUri;
    }

    private void cropImage(final Uri sourceImage, Uri destinationImage) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            //Utils.showToast(mContext, mContext.getString(R.string.error_cant_select_cropping_app));
            selectedImageUri = sourceImage;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, sourceImage);
            startActivityForResult(intent, SELECTED_IMG_CROP);
            return;
        } else {
            intent.setDataAndType(sourceImage, "image/*");
            intent.putExtra("aspectX", ASPECT_X);
            intent.putExtra("aspectY", ASPECT_Y);
            intent.putExtra("outputY", OUT_PUT_Y);
            intent.putExtra("outputX", OUT_PUT_X);
            intent.putExtra("scale", SCALE);

            //intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationImage);
            selectedImageUri = destinationImage;
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(intent, SELECTED_IMG_CROP);
            } else {
                Intent i = new Intent(intent);
                i.putExtra(Intent.EXTRA_INITIAL_INTENTS, list.toArray(new Parcelable[list.size()]));
                startActivityForResult(intent, SELECTED_IMG_CROP);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePhoto.CHOOSE_PHOTO_INTENT) {
                if (data != null && data.getData() != null) {
                    handleGalleryResult(data);
                } else {
                    handleCameraResult(getCameraUri());
                }
            } else if (requestCode == ChoosePhoto.SELECTED_IMG_CROP) {
                imageView2.setImageURI(getCropImageUrl());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), getCropImageUrl());
                    submitProfileData(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ChoosePhoto.SELECT_PICTURE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                showAlertDialog();
        }
    }


    private final int CAMERA_CAPTURE = 66;
    private final int GALLERY_ACTIVITY_CODE = 200, RESULT_CROP = 400, REQ_CODE_CROP_PHOTO = 14;
    private final int CROP_PIC = 77;


    public void dialogForImageOption() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Choose Image");
        alertDialogBuilder
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                            Uri photoURI = LegacyCompatFileProvider.getUriForFile(getContext(),
                                    SHARED_PROVIDER_AUTHORITY, file);

                            cropCapturedImage(photoURI);
                            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(intent, 1);

                        } catch (ActivityNotFoundException anfe) {
                            Toast toast = Toast.makeText(getActivity(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent gallery_Intent = new Intent(getActivity(), GalleryUtil.class);
                        gallery_Intent.putExtra("type", "image");
                        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);

                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //iveditprofile.setVisibility(View.INVISIBLE);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_ACTIVITY_CODE) {
                if (resultCode == Activity.RESULT_OK) {

                    String picturePath = data.getStringExtra("picturePath");
                    //name = getContentName(mainActivity.getContentResolver(), uri);
                    name = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                    //name=filename.substring(filename.lastIndexOf(".")+1);
                    //name = stripExtension(filename);
                    fileType = getMimeType(name);
                    //perform Crop on the Image Selected from Gallery
                    performCropNew(picturePath);
                }
            } else if (requestCode == RESULT_CROP) {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    //Bitmap photo = extras.getParcelable("data");
                    Bitmap photo = (Bitmap) extras.get("data");
                    imageView2.setImageBitmap(photo);
                    submitProfileData(photo);
                }
            } else if (requestCode == CAMERA_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {

                    //  Bitmap photo = extras.getParcelable("data");
                    Bitmap photo = (Bitmap) extras.get("data");


                    Uri picUri;
                    picUri = data.getData();
                    performCrop(picUri);

                    if (photo != null) {
                        // bitmapImage = photo;
                        imageView2.setImageBitmap(photo);
                        submitProfileData(photo);

                    }
                }
            } else if (requestCode == REQ_CODE_CROP_PHOTO) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    //  Bitmap photo = extras.getParcelable("data");
                    Bitmap photo = (Bitmap) extras.get("data");
                    System.err.println("BItMAp CROPPED " + photo);

                    //img_upload.setImageBitmap(photo);
                    imageView2.setImageBitmap(photo);
                    submitProfileData(photo);

                }
            } else if (resultCode == Activity.RESULT_OK && requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Bitmap photo = extras.getParcelable("data");

                    Bitmap photo = (Bitmap) extras.get("data");
                    System.err.println("Bitmap " + photo);

                    imageView2.setImageBitmap(photo);
                    submitProfileData(photo);

                }
            } else if (requestCode == 1) {
                //create instance of File with same name we created before to get image from storage
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                //Crop the captured image using an other intent
                try {
                    // the user's device may not support cropping
                    // cropCapturedImage(Uri.fromFile(file));
                    Uri photoURI = LegacyCompatFileProvider.getUriForFile(getContext(),
                            SHARED_PROVIDER_AUTHORITY, file);
                    cropCapturedImage(photoURI);
                } catch (ActivityNotFoundException aNFE) {
                    //display an error message if user device doesn't support
                    String errorMessage = "Sorry - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else if (requestCode == 2) {
                //Create an instance of bundle and get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap from extras
                Bitmap thePic = extras.getParcelable("data");
                //set image bitmap to image view
                //img_upload.setImageBitmap(thePic);
                imageView2.setImageBitmap(thePic);
                submitProfileData(thePic);
            }
        }
    }*/

    public void cropCapturedImage(Uri picUri) {
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }

    private void performCropNew(String picUri) {
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            // indicate image type and Uri
            File f = new File(picUri);
            // Uri contentUri = Uri.fromFile(f);
            Uri photoURI = LegacyCompatFileProvider.getUriForFile(getContext(),
                    SHARED_PROVIDER_AUTHORITY, f);
            getContext().grantUriPermission("com.android.camera", photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            cropCapturedImage(photoURI);

            cropIntent.setDataAndType(photoURI, "image");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);


        }
        // respond to users whose devices do not support the crop action
        catch (Exception anfe) {
            anfe.printStackTrace();
            Toast toast = Toast
                    .makeText(getActivity(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void performCrop(Uri picUri) {
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(getActivity(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


    public static String getContentName(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
        if (nameIndex >= 0) {
            return cursor.getString(nameIndex);
        } else {
            return null;
        }
    }

    @Override
    public void onSuccess(FingerprintManagerCompat.AuthenticationResult result) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onMaxAttemptReach() {

    }

    @Override
    public void onCancelDialog() {

    }

    @Override
    public void onUserResponse(int flag) {

    }
}