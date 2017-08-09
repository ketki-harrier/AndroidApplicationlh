package com.harrier.lifecyclehealth.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.fragments.MeetFragment;
import com.harrier.lifecyclehealth.fragments.MessageFragment;
import com.harrier.lifecyclehealth.fragments.MoreOptionsFragment;
import com.harrier.lifecyclehealth.fragments.SurveyDetailsListFragment;
import com.harrier.lifecyclehealth.fragments.SurveyFragment;
import com.harrier.lifecyclehealth.fragments.TreatmentFragment;
import com.harrier.lifecyclehealth.model.PatientSurveyItem;
import com.harrier.lifecyclehealth.utils.NetworkRequestUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;


public class MainActivity extends BaseActivity {

    //private List<SurveyModel> modelList;
    public NetworkRequestUtil networkRequestUtil;

    @Override
    String getTag() {
        return "MainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        networkRequestUtil = new NetworkRequestUtil(this);
      //  modelList = getSurveyDummyList();
        setupBottomBar();
    }


    /* Set up bottom Navigation Bar*/
    private void setupBottomBar() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                openFragment(tabId);
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                printLog("Tab Id:" + tabId);
                if (tabId == R.id.tab_more) {
                    openFragment(tabId);
                } else if (tabId == R.id.tab_survey && isDetailsViewVisible()) {
                    backFromSurveyDetailsView();
                }


            }
        });
    }
   /*// Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    private void openFragment(int tabId) {
        Fragment fragment = null;

        switch (tabId) {
            case R.id.tab_survey:
                fragment = new SurveyFragment();
                break;
            case R.id.tab_treatment:
                fragment = new TreatmentFragment();
                break;
            case R.id.tab_message:
                fragment = new MessageFragment();
                break;
            case R.id.tab_meet:
                fragment = new MeetFragment();
                break;
            case R.id.tab_more:
                fragment = new MoreOptionsFragment();
                break;
        }
        if (fragment != null)
            replaceFragment(fragment);

    }

    /* gives survey details view is showing or not*/
    private boolean isDetailsViewVisible() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("com.harrier.lifecyclehealth.fragments.SurveyDetailsListFragment");
        return (fragment != null && fragment.isVisible());
    }

    /* for changing fragments on click of bottom bar*/
    protected void replaceFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    /* Change fragment for survey Details view*/
    public void changeToSurveyDetailsView(String data, int position, String title) {
        Fragment fragment = SurveyDetailsListFragment.newInstance(data, title, position);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    /* Back from Survey toolbar button*/
    public void backFromSurveyDetailsView() {
        Fragment fragment = new SurveyFragment();
        String fragmentTag = fragment.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.left_to_right_anim, R.anim.right_to_left_animation)
                .replace(R.id.content_main, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    /******************************HELPER Method Creation*********************************/
    public List<PatientSurveyItem> getListConverted(String data) {

        TypeToken<List<PatientSurveyItem>> token = new TypeToken<List<PatientSurveyItem>>() {
        };
        return new Gson().fromJson(data, token.getType());
    }

    //public String getDummyDataData() {
     //   return new Gson().toJson(modelList);
   // }


    /******************************HELPER Method Creation ENDS HERE*********************************/


    /**********************************************DUMMY DATA CREATION****************************************************/
//    private List<SurveyModel> getSurveyDummyList() {
//        List<SurveyModel> list = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            //String surveyName, String name, List<QuestionModel> questionModel
//            SurveyModel surveyModel = new SurveyModel("SurveyName" + i, "Name" + i, getDummyQuestionsList(4));
//            list.add(surveyModel);
//        }
//
//        return list;
//    }

//    /*
//    option 1=EditText
//    option 0=radioButton;
//    */
//    private List<QuestionModel> getDummyQuestionsList(int size) {
//        List<QuestionModel> list = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            int TypeOfAns, optionNumber;
//            if (isOdd(i)) {
//                TypeOfAns = 1;
//                optionNumber = 1;
//
//            } else {
//                TypeOfAns = 0;
//                optionNumber = 3;
//            }
//            //String patientSurveyId, String description, int serialNumber, boolean isRequired, int typeOfAnswer, List<QuestionOptions> questionOptions
//            QuestionModel questionModel = new QuestionModel("patientSurveyId" + i, "Description" + i, i, true, TypeOfAns, getDummyQuestionOptionList(optionNumber));
//            list.add(questionModel);
//        }
//        return list;
//    }

//    private List<QuestionOptions> getDummyQuestionOptionList(int size) {
//        List<QuestionOptions> list = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            //String optionId, String name, int optionValue, int serialNumber
//            QuestionOptions questionOptions = new QuestionOptions("optionId" + i, "Name" + i, 1, i);
//            list.add(questionOptions);
//        }
//        return list;
//    }

    private boolean isOdd(int val) {
        return (val & 0x01) != 0;
    }

    /**********************************************DUMMY DATA CREATION ENDS****************************************************/

    @Override
    protected void onResume() {
        super.onResume();
    }


}
