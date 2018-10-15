package com.lifecyclehealth.lifecyclehealth.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.model.SurveyReportResponse;
import com.lifecyclehealth.lifecyclehealth.universal_video.UniversalMediaController;
import com.lifecyclehealth.lifecyclehealth.universal_video.UniversalVideoView;
import com.lifecyclehealth.lifecyclehealth.utils.VerticalSeekBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 24-01-2018.
 */

public class SurveyReportsAdapter extends RecyclerView.Adapter {

    List<SurveyReportResponse.QuestionModel> questionModelList;
    Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_survey_report_zero, parent, false);
                return new ZeroViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_survey_report_zero, parent, false);
                return new ZeroViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_survey_report_two, parent, false);
                return new TwoViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_survey_report_three, parent, false);
                return new ThreeViewHolder(view);
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_survey_report_zero, parent, false);
                return new ThreeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (questionModelList.get(position).getTypeOfAnswer()) {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 0;
            default:
                return 0;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final SurveyReportResponse.QuestionModel questionModel = questionModelList.get(position);
        if (questionModel != null) {
            switch (questionModel.getTypeOfAnswer()) {
                case 0: {
                    String text;
                    if (questionModel.isRequired()) {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
                    } else {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?";
                    }
                    ((ZeroViewHolder) holder).question.setText((position + 1) + ". " + Html.fromHtml(text));
                    ((ZeroViewHolder) holder).answer.setText("Answer : " + questionModel.getSubjectiveAnswer());

                    break;
                }
                case 1: {
                    String text;
                    if (questionModel.isRequired()) {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
                    } else {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?";
                    }
                    ((ZeroViewHolder) holder).question.setText((position + 1) + ". " + Html.fromHtml(text));
                    ((ZeroViewHolder) holder).answer.setText("Answer : " + questionModel.getDescriptiveAnswer());

                    break;
                }
                case 2: {
                    String text;
                    if (questionModel.isRequired()) {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
                    } else {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?";
                    }
                    ((TwoViewHolder) holder).question.setText((position + 1) + ". " + Html.fromHtml(text));

                    if (questionModel.getWidget_Type().trim().equals("10cm Visual Analogue Scale")) {

                        for (int i = 0; i < questionModel.getQuestionOptions().size(); i++) {
                            ((TwoViewHolder) holder).arrayMaxProgress.add(questionModel.getQuestionOptions().get(i).getQuestion_Option_Value_To());
                            ((TwoViewHolder) holder).arrayMinProgress.add(questionModel.getQuestionOptions().get(i).getQuestion_Option_Value_From());
                        }
                        if (questionModel.getWidget_Orientation().trim().equals("Vertical")) {
                            ((TwoViewHolder) holder).horizontal_bar.setVisibility(View.GONE);
                            ((TwoViewHolder) holder).vertical_bar.setVisibility(View.VISIBLE);
                            ((TwoViewHolder) holder).seekBarVertical.setMax(questionModel.getMax_Value());
                            ((TwoViewHolder) holder).vertical_start_value.setText(questionModel.getQuestionOptions().get(0).getName());
                            int size = questionModel.getQuestionOptions().size();
                            ((TwoViewHolder) holder).vertical_end_value.setText(questionModel.getQuestionOptions().get(size - 1).getName());
                            if (questionModel.getScoreValue() != null) {
                                int score = Integer.parseInt(questionModel.getScoreValue());
                                questionModelList.get(position).setSeekProgress(score);
                                ((TwoViewHolder) holder).seekBarVertical.setProgress(score);
                            } else {
                                questionModelList.get(position).setSeekProgress(0);
                                ((TwoViewHolder) holder).seekBarVertical.setProgress(0);
                            }

                            ((TwoViewHolder) holder).seekBarVertical.setProgress(questionModel.getSeekProgress());

                        } else {
                            ((TwoViewHolder) holder).vertical_bar.setVisibility(View.GONE);
                            ((TwoViewHolder) holder).horizontal_bar.setVisibility(View.VISIBLE);

                            ((TwoViewHolder) holder).seek_bar_horizontal.setMax(questionModel.getMax_Value());
                            ((TwoViewHolder) holder).horizontal_end_value.setText(questionModel.getQuestionOptions().get(0).getName());
                            int size = questionModel.getQuestionOptions().size();
                            ((TwoViewHolder) holder).horizontal_start_value.setText(questionModel.getQuestionOptions().get(size - 1).getName());
                            if (questionModel.getScoreValue() != null) {
                                int score = Integer.parseInt(questionModel.getScoreValue());
                                questionModelList.get(position).setSeekProgress(score);
                                ((TwoViewHolder) holder).seek_bar_horizontal.setProgress(score);
                            } else {
                                questionModelList.get(position).setSeekProgress(0);
                                ((TwoViewHolder) holder).seek_bar_horizontal.setProgress(0);
                            }
                            ((TwoViewHolder) holder).seek_bar_horizontal.setProgress(questionModel.getSeekProgress());

                        }
                    }
                    break;
                }
                case 3: {

                    ((ThreeViewHolder) holder).editRemoveName.setText(questionModel.getAttachment_Name());

                    if (questionModel.getAttachment_Type().contains("video")) {
                        ((ThreeViewHolder) holder).imageViewRemove.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.play));
                    } else if (questionModel.getAttachment_Type().contains("pdf")) {
                        ((ThreeViewHolder) holder).imageViewRemove.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.file_show));
                    } else if (questionModel.getAttachment_Type().contains("image")) {
                        ((ThreeViewHolder) holder).imageViewRemove.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image));
                    }
                    ((ThreeViewHolder) holder).imageViewRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (questionModel.getAttachment_Type().contains("video")) {
                                showVideo(questionModel.getAttachment_Url());
                            } else if (questionModel.getAttachment_Type().contains("pdf")) {
                                showPDF(questionModel.getAttachment_Url(),questionModel.getAttachment_Name());
                            } else if (questionModel.getAttachment_Type().contains("image")) {
                                showImage(questionModel.getAttachment_Url());
                            }
                        }
                    });
                    break;
                }

                case 4: {
                    String text;
                    if (questionModel.isRequired()) {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
                    } else {
                        text = "<font color=#000000>" + questionModel.getDescription() + "?";
                    }
                    ((ZeroViewHolder) holder).question.setText((position + 1) + ". " + Html.fromHtml(text));
                    ((ZeroViewHolder) holder).answer.setText("Answer : " + questionModel.getSubjectiveAnswer());

                    break;
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }


    public SurveyReportsAdapter(List<SurveyReportResponse.QuestionModel> questionModelList, Context context) {
        this.questionModelList = questionModelList;
        this.context = context;
    }


    public static class ZeroViewHolder extends RecyclerView.ViewHolder {
        TextView question, answer;

        public ZeroViewHolder(View itemView) {
            super(itemView);
            this.question = (TextView) itemView.findViewById(R.id.question);
            this.answer = (TextView) itemView.findViewById(R.id.answer);
        }
    }


    public static class TwoViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout vertical_bar, horizontal_bar;
        TextView vertical_start_value, vertical_end_value, horizontal_start_value, horizontal_end_value, question;
        VerticalSeekBar seekBarVertical;
        SeekBar seek_bar_horizontal;
        ArrayList<Integer> arrayMinProgress = new ArrayList<>();
        ArrayList<Integer> arrayMaxProgress = new ArrayList<>();

        public TwoViewHolder(View itemView) {
            super(itemView);
            this.question = (TextView) itemView.findViewById(R.id.question);
            seek_bar_horizontal = (SeekBar) itemView.findViewById(R.id.seek_bar_horizontal);
            vertical_start_value = (TextView) itemView.findViewById(R.id.vertical_start_value);
            vertical_end_value = (TextView) itemView.findViewById(R.id.vertical_end_value);
            horizontal_start_value = (TextView) itemView.findViewById(R.id.horizontal_start_value);
            horizontal_end_value = (TextView) itemView.findViewById(R.id.horizontal_end_value);
            seekBarVertical = (VerticalSeekBar) itemView.findViewById(R.id.seekBarVertical);
            vertical_bar = (RelativeLayout) itemView.findViewById(R.id.vertical_bar);
            horizontal_bar = (RelativeLayout) itemView.findViewById(R.id.horizontal_bar);
            seek_bar_horizontal.setEnabled(false);
            seek_bar_horizontal.setClickable(false);
            seek_bar_horizontal.setFocusable(false);
            seekBarVertical.setClickable(false);
            seekBarVertical.setFocusable(false);
            seekBarVertical.setEnabled(false);

        }
    }

    public static class ThreeViewHolder extends RecyclerView.ViewHolder {
        EditText editRemoveName;
        ImageView imageViewRemove;

        public ThreeViewHolder(View itemView) {
            super(itemView);
            this.editRemoveName = (EditText) itemView.findViewById(R.id.editRemoveName);
            this.imageViewRemove = (ImageView) itemView.findViewById(R.id.imageViewRemove);
        }
    }



    private int cachedHeight;
    private boolean isFullscreen;

    View mVideoLayout;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    private void showVideo(String url) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.survey_three_show);

        mVideoView = (UniversalVideoView) dialog.findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) dialog.findViewById(R.id.media_controller);
        mVideoLayout = dialog.findViewById(R.id.video_layout);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize(url);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //printLog("onCompletion");
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
                //printLog("onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                //printLog("onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                //printLog("onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                //printLog("onBufferingEnd UniversalVideoView callback");
            }

        });
        dialog.show();
    }

    private void setVideoAreaSize(final String url) {
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
                mVideoView.setVideoPath(url, context);
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });
    }


    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;
    ImageView imageViewLoad;

    private void showImage(String url) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        LayoutInflater li = LayoutInflater.from(context);
        final View myView = li.inflate(R.layout.survey_three_show_image, null);

        //dialog.setContentView(R.layout.survey_three_show_image);
        dialog.setContentView(myView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        SGD = new ScaleGestureDetector(context, new ScaleListener());
        touchListener(myView);
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        imageViewLoad = (ImageView) dialog.findViewById(R.id.image);
        com.android.volley.toolbox.ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

        imageLoader.get(url, new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Log", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imageViewLoad.setImageBitmap(response.getBitmap());
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


    private void showPDF(String url, final String name) {

        Dialog dialog = new Dialog(context);
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
        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
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
                printDocument(webview, name);

            }
        });
    }

    public void printDocument(WebView webView, String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);

            //noinspection deprecation
            PrintDocumentAdapter printDocumentAdapter = webView.createPrintDocumentAdapter();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                printDocumentAdapter = webView.createPrintDocumentAdapter(title);

            String documentName = context.getString(R.string.app_name) + " - " + title;
            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
            PrintJob printJob = printManager.print(documentName, printDocumentAdapter, attributes);
            //PrintJob printJob = printManager.print(documentName, printDocumentAdapter, new PrintAttributes.Builder().build());
            List<PrintJob> printJobs = printManager.getPrintJobs();

            printJobs.add(printJob);
        } else {
            Toast.makeText(context, "Not supported", Toast.LENGTH_SHORT).show();
            //showToast(mContext.getString(R.string.mytools_printing_not_supported), 1);
        }
    }

}
