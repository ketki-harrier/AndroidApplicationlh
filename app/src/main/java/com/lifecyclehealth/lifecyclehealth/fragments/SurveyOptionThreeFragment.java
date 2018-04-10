package com.lifecyclehealth.lifecyclehealth.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.CustomViewPager;
import com.lifecyclehealth.lifecyclehealth.adapters.SurveyPagerAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.ChangePasswordResponse;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitResponse;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitVideoResponse;
import com.lifecyclehealth.lifecyclehealth.multipart.VolleyMultipartRequest;
import com.lifecyclehealth.lifecyclehealth.universal_video.UniversalMediaController;
import com.lifecyclehealth.lifecyclehealth.universal_video.UniversalVideoView;
import com.lifecyclehealth.lifecyclehealth.upload_download.DownloadService;
import com.lifecyclehealth.lifecyclehealth.upload_download.UploadService;
import com.lifecyclehealth.lifecyclehealth.utils.InputStreamVolleyRequest;
import com.lifecyclehealth.lifecyclehealth.utils.TouchImageView;


import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_REMOVE_IMAGE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER_FILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyOptionThreeFragment extends BaseFragmentWithOptions implements View.OnClickListener {


    private static final String SURVEY_EXTRAS_TWO_TYPE = "type_two_survey_extras_data";
    private SurveyDetailsModel surveyDetailsModel;
    MainActivity mainActivity;
    InputStreamVolleyRequest request;
    Button btnSelectFile, btnChangeFile, btnUploadImage, btnRemove;
    ImageView imageViewRemove;
    ProgressBar imageView;
    EditText editputName, editchangeName, editRemoveName;
    LinearLayout selectFirstTimeLinear, uploadFileLinear, removeFileLinear;
    private static final int FILE_SELECT_CODE = 201;
    private static final int CAPTURE_CODE = 202;
    public static String fileType, fileName;
    public static byte[] byteData;


    public static SurveyOptionThreeFragment newInstance(String data, int position) {
        SurveyOptionThreeFragment threeFragment = new SurveyOptionThreeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_EXTRAS_TWO_TYPE, data);
        threeFragment.setArguments(bundle);
        return threeFragment;
    }


    @Override
    String getFragmentTag() {
        return "SurveyOptionThreeFragment";
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey_option_three, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        surveyDetailsModel = new Gson().fromJson(getArguments().getString(SURVEY_EXTRAS_TWO_TYPE), SurveyDetailsModel.class);
        setupView(view);
    }

    private void setupView(final View view) {

        TextView TextViewName = (TextView) view.findViewById(R.id.surveyName);
        TextView TextViewQuestion = (TextView) view.findViewById(R.id.questionTv);
        //String text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
        String text;
        if (surveyDetailsModel.getQuestionModel().isRequired()) {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
        } else {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "?";
        }
        TextView TextViewForName = (TextView) view.findViewById(R.id.surveyForName);
        if (MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT)) {
            TextViewForName.setVisibility(View.GONE);
        } else {
            TextViewForName.setVisibility(View.VISIBLE);
            TextViewForName.setText("Survey related to patient:" + surveyDetailsModel.getFirstName() + " " + surveyDetailsModel.getLastName());
        }

        TextViewQuestion.setText(Html.fromHtml(text));
        TextViewName.setText(surveyDetailsModel.getName());

        selectFirstTimeLinear = (LinearLayout) view.findViewById(R.id.selectFirstTimeLinear);
        uploadFileLinear = (LinearLayout) view.findViewById(R.id.uploadFileLinear);
        removeFileLinear = (LinearLayout) view.findViewById(R.id.removeFileLinear);
        imageView = (ProgressBar) view.findViewById(R.id.imageView);
        imageViewRemove = (ImageView) view.findViewById(R.id.imageViewRemove);
        editputName = (EditText) view.findViewById(R.id.editputName);
        editchangeName = (EditText) view.findViewById(R.id.editchangeName);
        editRemoveName = (EditText) view.findViewById(R.id.editRemoveName);
        btnUploadImage = (Button) view.findViewById(R.id.btnUploadImage);
        btnSelectFile = (Button) view.findViewById(R.id.btnSelectFile);
        btnChangeFile = (Button) view.findViewById(R.id.btnChangeFile);
        btnRemove = (Button) view.findViewById(R.id.btnRemove);
        btnSelectFile.setOnClickListener(this);
        btnChangeFile.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
        imageView.setVisibility(View.GONE);

        required();


       /* if (isUploading) {
            uploadFileLinear.setVisibility(View.VISIBLE);
            selectFirstTimeLinear.setVisibility(View.GONE);
            removeFileLinear.setVisibility(View.GONE);
            editchangeName.setText(fileName);
            //imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.downloadgif));
            imageView.setVisibility(View.VISIBLE);

        } else {*/

    }
    // }

    private void required() {
        final boolean isRequiredEditText = surveyDetailsModel.getQuestionModel().isRequired();
        if (isRequiredEditText) {
            if (surveyDetailsModel.getQuestionModel().getAttachment_Url() != null)
                if (!surveyDetailsModel.getQuestionModel().getAttachment_Url().trim().equals("")) {
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                } else {
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                }
        }


        if (surveyDetailsModel.getQuestionModel().getAttachment_Url() == null || (surveyDetailsModel.getQuestionModel().getAttachment_Url().trim().equals(""))) {
            uploadFileLinear.setVisibility(View.GONE);
            selectFirstTimeLinear.setVisibility(View.VISIBLE);
            removeFileLinear.setVisibility(View.GONE);
            editputName.setText("");
        } else {
            uploadFileLinear.setVisibility(View.GONE);
            selectFirstTimeLinear.setVisibility(View.GONE);
            removeFileLinear.setVisibility(View.VISIBLE);
            editRemoveName.setText(surveyDetailsModel.getQuestionModel().getAttachment_Name());

            if (surveyDetailsModel.getQuestionModel().getAttachment_Type().contains("video")) {
                imageViewRemove.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.play));
            } else if (surveyDetailsModel.getQuestionModel().getAttachment_Type().contains("pdf")) {
                imageViewRemove.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.file_show));
            } else if (surveyDetailsModel.getQuestionModel().getAttachment_Type().contains("image")) {
                imageViewRemove.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.image));
            }
            imageViewRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (surveyDetailsModel.getQuestionModel().getAttachment_Type().contains("video")) {
                        showVideo();
                    } else if (surveyDetailsModel.getQuestionModel().getAttachment_Type().contains("pdf")) {
                        showPDF();
                    } else if (surveyDetailsModel.getQuestionModel().getAttachment_Type().contains("image")) {
                        showImage();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSelectFile: {
                byteData = null;
                //dialogForImageOption();
                dialogForIMAGE_PDFOption();
                break;
            }
            case R.id.btnUploadImage: {
                //isUploading = true;
                //imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.downloadgif));
                if (editchangeName.getText().toString().trim().equals("")) {
                    showDialogWithOkButton("Enter file name");
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    startUpload();
                }
                break;
            }
            case R.id.btnChangeFile: {
                //dialogForImageOption();
                dialogForIMAGE_PDFOption();
                break;
            }
            case R.id.btnRemove: {
                removeSurveyImage();
                break;
            }

        }
    }


    private void startUpload() {
       /* Intent intent = new Intent(mainActivity, UploadService.class);
        intent.putExtra("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
        intent.putExtra("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
        intent.putExtra("fileName", editchangeName.getText().toString());
        intent.putExtra("fileType", fileType);
        intent.putExtra("filePath", fileType);
        mainActivity.startService(intent);*/
        uploadDataToServer();
    }


    private void uploadDataToServer() {
        //showProgressDialog(true);
        printLog("Upload start");
        SurveyDetailsItemFragment.disableScrollViewPager();
        final String Attachment_Name = editchangeName.getText().toString();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
            params.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
            params.put("OptionId", "0");
            params.put("Score", "0");
            params.put("Type_Of_Section", surveyDetailsModel.getQuestionModel().getTypeOfAnswer() + "");
            params.put("Descriptive_Answer", "");
            params.put("Attachment_Name", editchangeName.getText().toString());

            Map<String, VolleyMultipartRequest.DataPart> paramDataPart = new HashMap<>();

            paramDataPart.put("upload", new VolleyMultipartRequest.DataPart(editchangeName.getText().toString(), byteData, fileType));

            mainActivity.multipartRequest.postDataMultipartSecure(BASE_URL + URL_SURVEY_SUBMIT_ANSWER_FILE, params, paramDataPart, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    if (response != null) {
                        //showProgressDialog(false);
                        printLog("Response:survey submit" + response);

                        final SurveySubmitVideoResponse submitResponse = new Gson().fromJson(response.toString(), SurveySubmitVideoResponse.class);
                        if (submitResponse.getStatus().equals(STATUS_SUCCESS)) {
                            SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                            SurveyDetailsItemFragment.scrollViewPager();
                            surveyDetailsModel.getQuestionModel().setAttachment_Url(submitResponse.getAttachment_Url());
                            surveyDetailsModel.getQuestionModel().setAttachment_Type(submitResponse.getAttachment_Type());
                            surveyDetailsModel.getQuestionModel().setAttachment_Name(Attachment_Name);
                            SurveyPagerAdapter.surveyDetails.get(SurveyPagerAdapter.surveyPosition).setQuestionModel(surveyDetailsModel.getQuestionModel());
                            //editputName.setText(Attachment_Name);
                            editRemoveName.setText(Attachment_Name);
                            uploadFileLinear.setVisibility(View.GONE);
                            selectFirstTimeLinear.setVisibility(View.GONE);
                            removeFileLinear.setVisibility(View.VISIBLE);
                            required();
                        }
                    }
                }

                @Override
                public void onError(VolleyError error) {
                }
            });
        } catch (Exception e) {
        }
    }


    private void startDownload() {
        Intent intent = new Intent(mainActivity, DownloadService.class);
        intent.putExtra("url", surveyDetailsModel.getQuestionModel().getAttachment_Url());
        intent.putExtra("fileName", surveyDetailsModel.getQuestionModel().getAttachment_Name());
        mainActivity.startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
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
        try {


            Cursor cursor = resolver.query(uri, null, null, null, null);
            cursor.moveToFirst();
            int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (nameIndex >= 0) {
                return cursor.getString(nameIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void dialogForIMAGE_PDFOption() {
        byteData = null;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Choose File");
        alertDialogBuilder
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        contentSelectionIntent.setType("*/*");
                        Intent[] intentArray = {takePictureIntent, takeVideoIntent};
                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose an action");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                        startActivityForResult(Intent.createChooser(chooserIntent, "image"), CAPTURE_CODE);


                      /*  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAPTURE_CODE);*/

                        //video/*, image/*,pdf/*
                    }
                }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                //intent1.setType("video/*, image/*,pdf/*");
                intent1.setType("*/*");
                startActivityForResult(Intent.createChooser(intent1, "Open with ..."), FILE_SELECT_CODE);


                //video/*, image/*,pdf/*
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void dialogForImageOption() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Choose File");
        alertDialogBuilder
                .setPositiveButton("File", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                        intent1.setType("*/*");
                        startActivityForResult(Intent.createChooser(intent1, "Open with ..."), FILE_SELECT_CODE);
                        //video/*, image/*,pdf/*
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {

            if (resultCode == -1) {

                uploadFileLinear.setVisibility(View.VISIBLE);
                selectFirstTimeLinear.setVisibility(View.GONE);
                removeFileLinear.setVisibility(View.GONE);

                Uri uri = data.getData();
                String name = getContentName(mainActivity.getContentResolver(), uri);
                editchangeName.setText(name.substring(0, name.lastIndexOf('.')));
                fileName = name;
                fileType = getMimeType(name);
                printLog("fileType" + fileType);
                // fileName = name;
                try {
                    InputStream iStream = mainActivity.getContentResolver().openInputStream(uri);
                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int len = 0;
                    while ((len = iStream.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, len);
                    }
                    byteData = byteBuffer.toByteArray();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAPTURE_CODE) {
            if (resultCode == -1) {

                uploadFileLinear.setVisibility(View.VISIBLE);
                selectFirstTimeLinear.setVisibility(View.GONE);
                removeFileLinear.setVisibility(View.GONE);

                try {
                    fileType = "image/jpeg";
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteData = stream.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (byteData == null) {
                    Uri uri = data.getData();
                    editchangeName.setText("");
                    String name = getContentName(mainActivity.getContentResolver(), uri);
                    fileName = name;
                    fileType = getMimeType(name);
                    printLog("fileType" + fileType);
                    // fileName = name;
                    try {
                        InputStream iStream = mainActivity.getContentResolver().openInputStream(uri);
                        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                        int bufferSize = 1024;
                        byte[] buffer = new byte[bufferSize];
                        int len = 0;
                        while ((len = iStream.read(buffer)) != -1) {
                            byteBuffer.write(buffer, 0, len);
                        }
                        byteData = byteBuffer.toByteArray();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == 0) {
            Toast.makeText(getContext(), "User cancelled file ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Sorry! Failed to upload video", Toast.LENGTH_SHORT).show();
        }
    }


    private void removeSurveyImage() {
        if (isConnectedToNetwork(mainActivity)) {
            try {
                showProgressDialog(true);
                HashMap<String, String> params = new HashMap<>();
                params.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                params.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                params.put("Type_Of_Section", "3");

                HashMap<String, HashMap<String, String>> hashMapRequest = new HashMap<>();
                hashMapRequest.put("SurveyDetails", params);
                JSONObject requestJson = new JSONObject(new Gson().toJson(hashMapRequest));

                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_SURVEY_REMOVE_IMAGE, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (response != null) {
                            showProgressDialog(false);
                            printLog("Response:survey submit" + response);
                            ChangePasswordResponse surveyPlanDto = new Gson().fromJson(response.toString(), ChangePasswordResponse.class);
                            if (surveyPlanDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                surveyDetailsModel.getQuestionModel().setAttachment_Url("");
                                surveyDetailsModel.getQuestionModel().setAttachment_Type("");
                                surveyDetailsModel.getQuestionModel().setAttachment_Name("");
                                SurveyPagerAdapter.surveyDetails.get(SurveyPagerAdapter.surveyPosition).setQuestionModel(surveyDetailsModel.getQuestionModel());


                                uploadFileLinear.setVisibility(View.GONE);
                                selectFirstTimeLinear.setVisibility(View.VISIBLE);
                                removeFileLinear.setVisibility(View.GONE);
                                editputName.setText("");
                                required();

                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
            } catch (Exception e) {
            }
        } else {
            showNoNetworkMessage();
        }
    }

    private int cachedHeight;
    private boolean isFullscreen;

    View mVideoLayout;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    private void showVideo() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.survey_three_show);

        mVideoView = (UniversalVideoView) dialog.findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) dialog.findViewById(R.id.media_controller);
        mVideoLayout = dialog.findViewById(R.id.video_layout);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                printLog("onCompletion");
            }
        });
        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFull) {
            /*    isFullscreen = isFull;
                if (isFullscreen) {
                    try {
                        ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        mVideoLayout.setLayoutParams(layoutParams);
                        //GONE the unconcerned views to leave room for video and controller
                        mBottomLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = cachedHeight;
                    mVideoLayout.setLayoutParams(layoutParams);
                    mBottomLayout.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                printLog("onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                printLog("onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                printLog("onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                printLog("onBufferingEnd UniversalVideoView callback");
            }

        });
        dialog.show();
    }


    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;
    TouchImageView imageViewLoad;
    Bitmap imageBitmap = null;

    private void showImage() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        LayoutInflater li = LayoutInflater.from(getContext());
        final View myView = li.inflate(R.layout.survey_three_show_image, null);
        imageBitmap = null;
        //dialog.setContentView(R.layout.survey_three_show_image);
        dialog.setContentView(myView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        SGD = new ScaleGestureDetector(getContext(), new ScaleListener());
        touchListener(myView);
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        imageViewLoad = (TouchImageView) dialog.findViewById(R.id.image);
        final ImageView printPdf = (ImageView) dialog.findViewById(R.id.printImage);
        printPdf.setVisibility(View.GONE);

        com.android.volley.toolbox.ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

        imageLoader.get(surveyDetailsModel.getQuestionModel().getAttachment_Url(), new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Log", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imageBitmap = response.getBitmap();
                    imageViewLoad.setImageBitmap(response.getBitmap());
                    printPdf.setVisibility(View.VISIBLE);
                }
            }
        });
        printPdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
               /* PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
                PrintDocumentAdapter adapter = webview.createPrintDocumentAdapter();
                printManager.print(getString(R.string.app_name), adapter, null);*/

               if (imageBitmap!=null) {
                   printDocumentImage(imageBitmap, surveyDetailsModel.getQuestionModel().getAttachment_Name());
               }
            }
        });

    }

    private void touchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imageViewLoad.setScaleType(ImageView.ScaleType.MATRIX);
                SGD.onTouchEvent(event);
                return true;
            }
        });
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));
            matrix.setScale(scale, scale);
            imageViewLoad.setImageMatrix(matrix);
            return true;
        }

    }


    private void showPDF() {

        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.survey_three_show_pdf);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();


        final WebView webview = (WebView) dialog.findViewById(R.id.webView);
        final ProgressBar progressbar = (ProgressBar) dialog.findViewById(R.id.progressBar);

        final ImageView printPdf = (ImageView) dialog.findViewById(R.id.printPdf);
        printPdf.setVisibility(View.GONE);

        //String filename = "http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + surveyDetailsModel.getQuestionModel().getAttachment_Url());
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setSupportZoom(true);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progressbar.setVisibility(View.GONE);
                printPdf.setVisibility(View.VISIBLE);
            }
        });


        printPdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
               /* PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
                PrintDocumentAdapter adapter = webview.createPrintDocumentAdapter();
                printManager.print(getString(R.string.app_name), adapter, null);*/

                printDocument(webview, surveyDetailsModel.getQuestionModel().getAttachment_Name());

            }
        });

    }

    public void printDocument(WebView webView, String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);

            //noinspection deprecation
            PrintDocumentAdapter printDocumentAdapter = webView.createPrintDocumentAdapter();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                printDocumentAdapter = webView.createPrintDocumentAdapter(title);

            String documentName = getActivity().getString(R.string.app_name) + " - " + title;
            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
            PrintJob printJob = printManager.print(documentName, printDocumentAdapter, attributes);
            //PrintJob printJob = printManager.print(documentName, printDocumentAdapter, new PrintAttributes.Builder().build());
            List<PrintJob> printJobs = printManager.getPrintJobs();

            printJobs.add(printJob);
        } else {
            Toast.makeText(getContext(), "Not supported", Toast.LENGTH_SHORT).show();
        }
    }


    public void printDocumentImage(Bitmap webView, String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            String documentName = getActivity().getString(R.string.app_name) + " - " + title;
            PrintHelper printHelper = new PrintHelper(getContext());
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            printHelper.printBitmap(documentName, webView);

        } else {
            Toast.makeText(getContext(), "Not supported", Toast.LENGTH_SHORT).show();
            //showToast(mContext.getString(R.string.mytools_printing_not_supported), 1);
        }
    }
/*    private void createWebPrintJob(WebView webView) {
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
        PdfPrint pdfPrint = new PdfPrint(attributes);
        pdfPrint.print(webView.createPrintDocumentAdapter(jobName), path, "output_" + System.currentTimeMillis() + ".pdf");
    }*/

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(surveyDetailsModel.getQuestionModel().getAttachment_Url(), getContext());
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });
    }

}
