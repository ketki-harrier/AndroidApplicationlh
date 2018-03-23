package com.lifecyclehealth.lifecyclehealth.upload_download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.utils.InputStreamVolleyRequest;
import com.lifecyclehealth.lifecyclehealth.utils.MultipartRequest;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by vaibhavi on 08-09-2017.
 */

public class DownloadService extends IntentService implements Response.Listener<byte[]>, Response.ErrorListener{

    //MainActivity mainActivity;
    public MultipartRequest multipartRequest;
    private static final String TAG = DownloadService.class.getName();

    public DownloadService() {
        super(TAG);
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    String url,fileName;

    @Override
    protected void onHandleIntent(Intent intent) {
        // mainActivity = (MainActivity) getApplicationContext();
        multipartRequest = new MultipartRequest(getApplicationContext());
        printLog("OnHandler:" + intent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0,0,true)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setDefaults(android.app.Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(false);
        notificationManager.notify(0, notificationBuilder.build());


        url = intent.getStringExtra("url");
        fileName = intent.getStringExtra("fileName");
        printLog("url" + url);

        if (url != null)
            downloadFile();
    }


    public void downloadFile() {
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, url, DownloadService.this, DownloadService.this, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(),
                new HurlStack());
        mRequestQueue.add(request);
    }


    @Override
    public void onResponse(byte[] response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (response != null) {
                try {
                    InputStream input = new ByteArrayInputStream(response);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), fileName);
                    map.put("resume_path", file.toString());
                    // file_show.createNewFile();
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                    byte data[] = new byte[1024];

                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                    printLog("download successful");
                    //Toast.makeText(getApplicationContext(), "download successful", Toast.LENGTH_SHORT).show();
                    onDownloadComplete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE. ERROR:: " + error.getMessage());
    }

    private void onDownloadComplete(File file) {

        Intent intent = getIntentForOpeningFile(file);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("File Downloaded");
        notificationBuilder.setContentIntent(pIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    public Intent getIntentForOpeningFile(File url) {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file_show you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file_show
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file_show
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file_show
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file_show
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file_show
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file_show
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file_show
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file_show
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file_show
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file_show
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file_show
            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void printLog(String message) {
        Log.e(TAG, message + " ");
    }
}
