package com.lifecyclehealth.lifecyclehealth.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsItemFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsListFragment;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SurveyActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

   // private SurveyDetailsModel surveyDetailsModel;
   public static List<SurveyDetailsModel> surveyDetails;
    public static final int VIEW_TYPE_RADIO = 1;
    public static final String VIEW_RADIO = "Radio";
    private Context context ;
    MainActivity mainActivity;
    String Stringcode;
    int status ;
    View v;
    private ColorCode colorCode;
    SurveyDetailsModel surveyDetailsModel ;
    RadioGroup radioGroup;


    public SurveyActivityAdapter(List<SurveyDetailsModel> surveyDetails,Context context){
        this.surveyDetails = surveyDetails ;
        this.context = context;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HolderRadioButton) {
            HolderRadioButton holderRadioButton = (HolderRadioButton) holder;
            holderRadioButton.bindData(surveyDetailsModel);
        }


    }

    @Override
    public int getItemViewType(int position) {
        // printLog("ViewType:-At" + position + "||" + surveyModelList.get(position).getViewType());
        //return surveyModelList.get(position).getViewType();
        surveyDetailsModel = surveyDetails.get(position);
        return surveyDetails.get(position).getQuestionModel().getTypeOfAnswer();

    }

    @Override
    public int getItemCount() {
      surveyDetails.size();
        return 0;
    }

    private void setupView(View view) {
        //  try {
        String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
        colorCode = new Gson().fromJson(resposne, ColorCode.class);
        String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
        String Stringcoden = "";
        String hashcode = "";

        if (demo == null) {
            hashcode = "Green";
            Stringcode = "259b24";
        } else if (demo != null) {
            String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
            hashcode = arr[0].trim();
            Stringcode = arr[1].trim();
          /*  }
            else*/
            if (hashcode.equals("Black") && Stringcode.length() < 6) {
                Stringcode = "333333";
            }
        }
        //     }catch (Exception e){e.printStackTrace();}
        TextView TextViewName = (TextView) view.findViewById(R.id.surveyName);
        TextView TextViewQuestion = (TextView) view.findViewById(R.id.questionTv);
        TextViewName.setTextColor(Color.parseColor("#" + Stringcode));

    }

        /******************************************************** RADIO BUTTON STARTS*******************************************************************/
/*    public class HolderRadioButton extends RecyclerView.ViewHolder {
        private TextView quesTextView;
        private RadioGroup radioGroup;
        private ImageView quesIcon;


        public HolderRadioButton(View itemView) {
            super(itemView);
            quesTextView = (TextView) itemView.findViewById(R.id.quesTextView);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radioGroup);
            quesIcon = (ImageView) itemView.findViewById(R.id.quesIcon);
        }


        LinkedHashMap<String, String> radioButtonMap = getOptionMap(validResponse);
            for(
        int row = 0;
        row< 1;row++)

        {
            RadioGroup ll = new RadioGroup(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            int idConstant = 0;
            for (final Map.Entry entry : radioButtonMap.entrySet()) {
                idConstant++;
                AppCompatRadioButton rdbtn = new AppCompatRadioButton(context);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 5, 0, 5);
                rdbtn.setLayoutParams(layoutParams);
                rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
                rdbtn.setSupportButtonTintList(getColorList());
                rdbtn.setHighlightColor(ContextCompat.getColor(context, R.color.colorPrimary));
                rdbtn.setId((row * 2) + idConstant);
                rdbtn.setText((String) entry.getValue());
                rdbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        surveyModel.getQuestion().setUserTempAns((String) entry.getKey());
                    }
                });
                ((ViewGroup) radioGroup).addView(rdbtn);
                if (ansHashmapOnline.containsKey(String.valueOf(surveyModel.getQuestionNumber()))) {
                    String keyFromService = ansHashmapOnline.get(String.valueOf(surveyModel.getQuestionNumber()));
                    //  printLog("OnlinePosition:-" + keyFromService);
                    String key = (String) entry.getKey();
                    if (key.equalsIgnoreCase(keyFromService)) {
                        rdbtn.setChecked(true);
                        surveyModel.getQuestion().setUserTempAns((String) entry.getKey());
                    }

                }
                if (isValid(surveyModel.getQuestion().getUserTempAns())) {
                    String userAns = surveyModel.getQuestion().getUserTempAns();
                    if (isValid(userAns) && userAns.equalsIgnoreCase((String) entry.getKey())) {
                        rdbtn.setChecked(true);
                    }
                }
            }

        }
            if(surveyModel.isErrorOccured())

        {
            radioGroup.clearAnimation();
            radioGroup.setAnimation(shake);
        }
    }*/


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              if (viewType == VIEW_TYPE_RADIO) {
                return new HolderRadioButton(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_survey_option_zero, parent, false));
            }
            return null;
        }


        @SuppressLint("RestrictedApi")

        public class HolderRadioButton extends RecyclerView.ViewHolder {
            //  private void addRadioButtons() {

            public HolderRadioButton(View itemView) {
                super(itemView);
                //  quesTextView = (TextView) itemView.findViewById(R.id.quesTextView);
                radioGroup  = itemView.findViewById(R.id.radioGroup);
                // quesIcon = (ImageView) itemView.findViewById(R.id.quesIcon);
            }

            public void bindData(final SurveyDetailsModel surveyDetailsModel) {



            final int number = surveyDetailsModel.getQuestionModel().getQuestionOptions().size();
            //    String bool =  surveyDetailsModel.getQuestionModel().isRequired();
            for(int row = 0; row< 1;row++)

            {
                final RadioGroup ll = new RadioGroup(context);
                ll.setOrientation(LinearLayout.VERTICAL);

                for (int i = 1; i <= number; i++) {
                    String optionsToadd = surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getName();
                    final AppCompatRadioButton rdbtn = new AppCompatRadioButton(mainActivity);
                    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 10, 0, 5);
                    rdbtn.setLayoutParams(layoutParams);
                    rdbtn.setPadding(5, 0, 0, 0);
                    rdbtn.setTextColor(ContextCompat.getColor(mainActivity, R.color.black));
                    //   Typeface font = Typeface.createFromAsset(Context.getAssets(), "fonts/Roboto-Regular.ttf");
                    //  rdbtn.setTypeface(font);

                    rdbtn.setId(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId());
                    rdbtn.setText(optionsToadd);
                    if (SurveyDetailsListFragment.isToDo) {
                        rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
                        rdbtn.setSupportButtonTintList(getColorList());
                        //rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                        rdbtn.setHighlightColor(Color.parseColor("#" + Stringcode));
                    } else {
                        rdbtn.setBackgroundResource(R.drawable.deselector_radiobutton);
                        rdbtn.setSupportButtonTintList(getDeColorList());
                        rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.black));
                        rdbtn.setFocusable(false);
                        rdbtn.setClickable(false);
                        rdbtn.setCursorVisible(false);
                    }

                    if (surveyDetailsModel.getQuestionModel().getAnswerId() == surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId()) {
                        rdbtn.setChecked(true);
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                    }

                    rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                int id = buttonView.getId();
                                HashMap<String, String> requestParameter = new HashMap<String, String>();
                                for (int loop = 1; loop <= surveyDetailsModel.getQuestionModel().getQuestionOptions().size(); loop++) {
                                    if (surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionId() == id) {
                                        requestParameter.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                                        requestParameter.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                                        requestParameter.put("OptionId", id + "");
                                        requestParameter.put("Score", surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionValue() + "");
                                    }
                                }
                                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                                SurveyDetailsItemFragment.scrollViewPager();
                                status = 1;
                                if (SurveyDetailsListFragment.isToDo) {
                                    // submitSelectedAnswerOfSurvey(requestParameter);
                                }
                            } else {

                            }
                        }
                    });
                    radioGroup.addView(rdbtn);
                }
            }
        }
        }

        private ColorStateList getColorList() {
            return new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    //new int[]{ContextCompat.getColor(mainActivity, R.color.colorPrimary)
                    new int[]{Color.parseColor("#" + Stringcode)}
            );
        }

        private ColorStateList getDeColorList() {
            return new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{ContextCompat.getColor(mainActivity, R.color.black)}
            );
        }


        /* decode radio button Phase*/
        private LinkedHashMap<String, String> getOptionMap(String validResponse) {
            LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
            try {
                JSONObject jsonObject = new JSONObject(validResponse);
                /*  printLog("Object:" + jsonObject.toString());*/
                JSONArray jsonArrayOptions = jsonObject.getJSONArray("option");
                /* printLog("Array" + jsonArrayOptions.toString());*/
                if (jsonArrayOptions != null) {
                    for (int i = 0; i < jsonArrayOptions.length(); i++) {
                        JSONObject objects = jsonArrayOptions.getJSONObject(i);
                        Iterator key = objects.keys();
                        while (key.hasNext()) {
                            String k = (String) key.next();
                            optionMap.put(k, objects.get(k).toString());
                          /*  System.out.println("Key : " + k + ", value : "
                                    + objects.get(k).toString());*/
                        }
                        // System.out.println(objects.toString());
                        System.out.println("-----------");
                    }
                    /*          printLog(optionMap);*/
                }
            } catch (
                    JSONException e) {
                e.printStackTrace();

            }
            return optionMap;
        }

    }







