package com.lifecyclehealth.lifecyclehealth.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.adapters.IntroSliderAdapter;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback1;
import com.lifecyclehealth.lifecyclehealth.dto.CarousalDTO;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CAROUSAL;


public class SliderActivity extends AppCompatActivity {

    LinearLayout sliderDotspanel;
    Button next, prev, gotIt;
    private int dotscount;
    private ImageView[] dots;
    IntroSliderAdapter adapter;
    ViewPager viewPager;
    private int currentPage;
    private Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    NetworkRequestUtil requestUtil;
 /*   private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousal);

        viewPager  = findViewById(R.id.view_pager);
      //  IntroSliderAdapter adapter = new IntroSliderAdapter(this, imageUrls);
     //   viewPager.setAdapter(adapter);



        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        gotIt = findViewById(R.id.gotIt);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);


       /* dotscount = adapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));*/


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
            }
        });

       /* viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager.getCurrentItem()== 0){
                    prev.setVisibility(View.GONE);
                }else {
                    prev.setVisibility(View.VISIBLE);
                }

                if (viewPager.getCurrentItem()== imageUrls.length-1){
                    next.setVisibility(View.GONE);
                    gotIt.setVisibility(View.VISIBLE);
                }else {
                    next.setVisibility(View.VISIBLE);
                    gotIt.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

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
                             adapter = new IntroSliderAdapter( getApplicationContext(),imgUrls);
                            viewPager.setAdapter(adapter);
                            /*After setting the adapter use the timer */
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == imgUrls.size()) {
                                        currentPage = 0;
                                    }
                                    viewPager.setCurrentItem(currentPage++, true);
                                }
                            };

                            timer = new Timer(); // This will create a new Thread
                            timer.schedule(new TimerTask() { // task to be scheduled
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, DELAY_MS, PERIOD_MS);

                        } /*else {
                            cardFirstCarousel.setVisibility(View.GONE);
                        }*/
                    }
                }

            }


            @Override
            public void onError(VolleyError error) {

            }
        });
    }

}
