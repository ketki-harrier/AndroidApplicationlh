package com.lifecyclehealth.lifecyclehealth.upload_download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionThreeFragment;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitResponse;
import com.lifecyclehealth.lifecyclehealth.multipart.VolleyMultipartRequest;
import com.lifecyclehealth.lifecyclehealth.utils.MultipartRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER_FILE;

/**
 * Created by vaibhavi on 08-09-2017.
 */

public class UploadService extends IntentService {

    //MainActivity mainActivity;
    public MultipartRequest multipartRequest;
    private static final String TAG = UploadService.class.getName();

    public UploadService() {
        super(TAG);
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    String fileName, filePath, fileType, ResponseId, QuestionId;

    @Override
    protected void onHandleIntent(Intent intent) {
        // mainActivity = (MainActivity) getApplicationContext();
        multipartRequest = new MultipartRequest(getApplicationContext());
        printLog("OnHandler:" + intent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Upload")
                .setProgress(0, 0, true)
                .setContentText("Uploading File")
                .setDefaults(android.app.Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(false);
        notificationManager.notify(0, notificationBuilder.build());

        ResponseId = intent.getStringExtra("ResponseId");
        QuestionId = intent.getStringExtra("QuestionId");
        fileName = intent.getStringExtra("fileName");
        filePath = intent.getStringExtra("filePath");
        fileType = intent.getStringExtra("fileType");
        printLog("QuestionId" + QuestionId);
        printLog("ResponseId:" + ResponseId);

        if (ResponseId != null && QuestionId != null)
            // downloadFile(ResponseId, QuestionId);
            uploadDataToServer();
    }


    private void uploadDataToServer() {
        printLog("Download start");
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("ResponseId", ResponseId);
            params.put("QuestionId", QuestionId);
            params.put("OptionId", "0");
            params.put("Score", "0");
            params.put("Type_Of_Section", "3");
            params.put("Descriptive_Answer", "");
            params.put("Attachment_Name", fileName);

            Map<String, VolleyMultipartRequest.DataPart> paramDataPart = new HashMap<>();

            printLog("fileType" + fileType);

            paramDataPart.put("upload", new VolleyMultipartRequest.DataPart(fileName, SurveyOptionThreeFragment.byteData, fileType));

            multipartRequest.postDataMultipartSecure(BASE_URL + URL_SURVEY_SUBMIT_ANSWER_FILE, params, paramDataPart, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    if (response != null) {
                        printLog("Response:survey submit" + response);
                        uploadComplete();
                        final SurveySubmitResponse submitResponse = new Gson().fromJson(response.toString(), SurveySubmitResponse.class);
                        if (submitResponse.getStatus().equals(STATUS_SUCCESS)) {
                        }
                    }
                }

                @Override
                public void onError(VolleyError error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadComplete() {
        // SurveyOptionThreeFragment.isUploading=false;
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("File Uploaded");
        // notificationBuilder.setContentIntent(pIntent);
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }


    private void printLog(String message) {
        Log.e(TAG, message + " ");
    }
}
