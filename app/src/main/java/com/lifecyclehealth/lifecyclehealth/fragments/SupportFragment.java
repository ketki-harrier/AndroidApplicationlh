package com.lifecyclehealth.lifecyclehealth.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.image.ChoosePhoto;
import com.lifecyclehealth.lifecyclehealth.image.FileUtil;
import com.lifecyclehealth.lifecyclehealth.model.ChangePasswordResponse;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.ProfileGetResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_PROFILE_GETDATA;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_PROFILE_GETIMAGE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SUPPORT;


public class SupportFragment extends BaseFragmentWithOptions {
    private static final String STARTING_TEXT = "heading";
    MainActivity mainActivity;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private Uri fileUri;
    private ColorCode colorCode;
    String Stringcode;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editEmail;
    EditText editSubject;
    EditText editTextMsg;
    EditText editTextImagePath;
    String name, fileType;

    public SupportFragment() {
    }

    public static SupportFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(STARTING_TEXT, text);
        SupportFragment supportFragment = new SupportFragment();
        supportFragment.setArguments(args);

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
      //  try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            colorCode = new Gson().fromJson(resposne, ColorCode.class);
            String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
            String Stringcode = "";
            String hashcode = "";

            if(demo == null){
                hashcode = "Green";
                Stringcode = "259b24";
            }
            else if(demo !=null) {
                String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                hashcode = arr[0].trim();
                Stringcode = arr[1].trim();
           /* }
            else*/
                if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    Stringcode = "333333";
                }
            }
     //   }catch (Exception e){e.printStackTrace();}

         editTextFirstName = view.findViewById(R.id.editTextFirstName);
         editTextLastName = view.findViewById(R.id.editTextLastName);
         editEmail = view.findViewById(R.id.editEmail);
         editSubject = view.findViewById(R.id.editSubject);
         editTextMsg = view.findViewById(R.id.editTextMsg);
         editTextImagePath = view.findViewById(R.id.editTextImagePath);

        Button btnsendemail = view.findViewById(R.id.btnsendemail);
        Button btntakePhoto = view.findViewById(R.id.btntakePhoto);
        Button btncancel = view.findViewById(R.id.btncancel);
        btnsendemail.setBackgroundColor(Color.parseColor("#"+Stringcode));
        btntakePhoto.setBackgroundColor(Color.parseColor("#"+Stringcode));

        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setColorFilter(Color.parseColor("#"+Stringcode));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        btntakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.with(getContext()).track("update Profile Picture", new Properties().putValue("category", "Mobile"));
                showAlertDialog();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPage();
            }
        });

        btnsendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextFirstName.getText().toString().trim().equalsIgnoreCase("")){
                    showDialogWithOkButton("Please enter First Name.");
                }else if (editTextLastName.getText().toString().trim().equalsIgnoreCase("")){
                    showDialogWithOkButton("Please enter Last Name.");
                }else if (editEmail.getText().toString().trim().equalsIgnoreCase("")){
                    showDialogWithOkButton("Please enter email.");
                }else if(editSubject.getText().toString().trim().equalsIgnoreCase("")){
                    showDialogWithOkButton("Please enter subject.");
                }else if(editTextMsg.getText().toString().trim().equalsIgnoreCase("")) {
                    showDialogWithOkButton("Please enter message.");
                }/*else if(editTextImagePath.getText().toString().trim().equalsIgnoreCase("")) {
                    showDialogWithOkButton("Please select image.");
                }*/ else {
                    SendFeedBack();
                }

            }
        });

        TextView back = (TextView) view.findViewById(R.id.back);
        back.setTextColor(Color.parseColor("#"+Stringcode));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
        back.setText(getArguments().getString(STARTING_TEXT));
        getProfile_ImageData("data");

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
            if (name != null){
                editTextImagePath.setText(name);
            }
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
            if (name != null){
                editTextImagePath.setText(name);
            }
            fileType = getMimeType(name);
            printLog("name" + name);

            cropImage(cameraPictureUrl, cropPictureUrl);
        } catch (IOException e) {
            e.printStackTrace();

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
                //imageView2.setImageURI(getCropImageUrl());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), getCropImageUrl());
                    //submitProfileData(bitmap);
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

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void SendFeedBack() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                JSONObject params = new JSONObject();
                params.put("FirstName", editTextFirstName.getText().toString());
                params.put("LastName", editTextLastName.getText().toString());
                params.put("EmailID", editEmail.getText().toString());
                params.put("Subject", editSubject.getText().toString());
                params.put("Message", editTextMsg.getText().toString());

                mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_SUPPORT, params, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Send code:" + response);
                        if (response != null) {
                            try {
                                String status = response.getString("status");
                                if (status.equals("0")){
                                    editSubject.setText("");
                                    editTextMsg.setText("");
                                    showDialogWithOkButton(response.getString("message"));
                                }else {
                                    showDialogWithOkButton(response.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                        showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
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
                                        editEmail.setText(profileGetResponse.getUser().getUser_details().getUsername());
                                        //editTextMobileNo.setText(profileGetResponse.getUser().getUser_details().getMobilePhone());
                                        //editTextPreferredName.setText(profileGetResponse.getUser().getUser_details().getPreferred_Name());
                                        //editTextTitle.setText(profileGetResponse.getUser().getUser_details().getName_Title());
                                        //editTextSuffix.setText(profileGetResponse.getUser().getUser_details().getName_Suffix());
                                        //getProfile_ImageData("image");
                                    } else {
                                        //showDialogWithOkButton(meetListDTO.getMessage());
                                    }
                                } else
                                    showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
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
            } catch (Exception e) {
                showProgressDialog(false);
            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private void backPage() {
        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("SupportFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}