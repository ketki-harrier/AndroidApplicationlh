package com.lifecyclehealth.lifecyclehealth.activities;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.adapters.ImageSliderAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.IntroSliderAdapter;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback1;
import com.lifecyclehealth.lifecyclehealth.dto.CarousalDTO;
import com.lifecyclehealth.lifecyclehealth.model.MessageDialogResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CAROUSAL;

public class Carousal extends AppCompatActivity {
    NetworkRequestUtil requestUtil;
    private ViewPager pagerCarouselOne;
    private CardView cardFirstCarousel, card_HeaderBanner;
    private int currentPage;
    private Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousal);
        setView();
        //   setCarousel();
    }

    /* Set view*/
    private void setView() {
    //    cardFirstCarousel = findViewById(R.id.card_FirstCarousel);
        //ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
     //   pagerCarouselOne = findViewById(R.id.pager_carouselOne);
        requestUtil = new NetworkRequestUtil(Carousal.this);
        setCarousel();
    }

    private void setCarousel() {
        requestUtil.getDataSecure1(BASE_URL + URL_CAROUSAL, new VolleyCallback1() {
            @Override

            public void onSuccess1(String response) {
                CarousalDTO carousalDTO = new Gson().fromJson(response.toString(), CarousalDTO.class);
                if (carousalDTO != null) {
                    //    printLog("StatisTic:" + statisticDTO);
                    final List<Object> imgUrls = new ArrayList<>(0);
                    //         List<String> buttonActions = new ArrayList<>(0);
                    for (int i = 0; i < carousalDTO.getUrls().size(); i++) {
                        if (carousalDTO.getUrls() != null) {
                            imgUrls.add(carousalDTO.getUrls().get(i));
                        }




                  /*  if (imgUrls.size() != 0) {
                        ImageSliderAdapter imageSliderAdapter1 = new ImageSliderAdapter(imgUrls, getApplicationContext());
                        pagerCarouselOne.setAdapter(imageSliderAdapter1);
                        *//*After setting the adapter use the timer *//*
                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == imgUrls.size()) {
                                    currentPage = 0;
                                }
                                pagerCarouselOne.setCurrentItem(currentPage++, true);
                            }
                        };

                        timer = new Timer(); // This will create a new Thread
                        timer.schedule(new TimerTask() { // task to be scheduled
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, DELAY_MS, PERIOD_MS);

                    } else {
                        cardFirstCarousel.setVisibility(View.GONE);
                    }
                }
            }*/


                   /*     @Override
                        public void onSuccess1 (String response){
                            CarousalDTO carousalDTO = new Gson().fromJson(response.toString(), CarousalDTO.class);
                            if (carousalDTO != null) {
                                //    printLog("StatisTic:" + statisticDTO);
                                final List<Object> imgUrls = new ArrayList<>(0);
                                //         List<String> buttonActions = new ArrayList<>(0);
                                for (int i = 0; i < carousalDTO.getUrls().size(); i++) {
                                    if (carousalDTO.getUrls() != null) {
                                        imgUrls.add(carousalDTO.getUrls().get(i));
                                    }

                                }*/


                        if (imgUrls.size() != 0) {
                            IntroSliderAdapter imageSliderAdapter1 = new IntroSliderAdapter( getApplicationContext(),imgUrls);
                            pagerCarouselOne.setAdapter(imageSliderAdapter1);
                            /*After setting the adapter use the timer */
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == imgUrls.size()) {
                                        currentPage = 0;
                                    }
                                    pagerCarouselOne.setCurrentItem(currentPage++, true);
                                }
                            };

                            timer = new Timer(); // This will create a new Thread
                            timer.schedule(new TimerTask() { // task to be scheduled
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, DELAY_MS, PERIOD_MS);

                        } else {
                            cardFirstCarousel.setVisibility(View.GONE);
                        }
                    }
                }

            }


            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
