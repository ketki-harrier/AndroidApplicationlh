package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lifecyclehealth.lifecyclehealth.activities.Carousal;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private List<Object> imageUrls, buttonActions;
    private final List<Target> targets = new ArrayList<>();
    Context context;
    //private Carousal context;

    public ImageSliderAdapter(List<Object> imageUrls, Context context) {
        this.imageUrls = imageUrls;
     //   this.buttonActions = buttonActions;
       this.context = context;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                targets.remove(this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                targets.remove(this);
            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        targets.add(target);
        imageView.setTag(target);
        if (imageUrls != null && imageUrls.get(position) != null) {
            container.addView(imageView);

            Glide.with(context)
                    .asBitmap()
                    .load(imageUrls.get(position).toString())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                        }
                    });
   /*         Picasso.with(context).load(imageUrls.get(position).toString())
                    : imageUrls.get(position)).resize(480, 620)
                    .into(target);*/
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                 /*   if (buttonActions.size() != 0 && buttonActions.get(position) != null)*/ {
                        String urlToBeLoaded = String.valueOf(buttonActions.get(position));

                        //Conditional Check for redicrection to specific screen
                        if (urlToBeLoaded.contains("steproutes")) {

                        } /*else if (urlToBeLoaded.contains("weight_tracker")) {
                            context.replaceFragment(WeightTrackerFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("food_swap")) {
                            context.replaceFragment(FoodSwapFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("achievements/new_achievement")) {
                            context.replaceFragment(AchievementsFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("Step_Assess")) {

                        } else if (urlToBeLoaded.contains("Steping_Stones/goals")) {

                        } else if (urlToBeLoaded.contains("challenges")) {
                            context.replaceFragment(FragmentChallenges.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("New_Activity")) {
                            context.startLogActivity(LogActivityDynamic.LOG_ACTIVITY);
                        } else if (urlToBeLoaded.contains("contests/contest_winners/723fcee0-c157-11e8-8080-808080808080")) {
                            context.replaceFragment(ContestFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("leaderboard")) {
                            context.replaceFragment(LeaderBoardFragmentNew.getInstanceWithBackSpaceToHome(true));
                        } else if (urlToBeLoaded.contains("hall_of_fame")) {
                            context.replaceFragment(FragmentHallOfFrame.getInstanceForDirectHome());
                        } *//*else if (urlToBeLoaded.contains("Race")) {
                            context.replaceFragment(MyRaceResultFragment.getInstanceForDirectHome());
                        }*//* else if (urlToBeLoaded.contains("Race") || urlToBeLoaded.contains("race")) {
                            context.replaceFragment(MyRaceResultFragment.getInstanceForDirectHome());
                        }else if (urlToBeLoaded.contains("activity")) {
                            context.startLogActivity(LogActivityDynamic.LOG_ACTIVITY);
                        } else if (urlToBeLoaded.contains("reward")) {
                            context.replaceFragment(ContestFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("star_stepathletes1_1")) {

                        } else if (urlToBeLoaded.contains("StepAssess/star_stepathletes_winner") || urlToBeLoaded.contains("StepAssess/star_stepathletes")) {
                            context.replaceFragment(StarStepathlistFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("Community")) {
                            context.replaceFragment(CommunityFragment.getInstanceForDirectHome(0));
                        } else if (urlToBeLoaded.contains("Steping_Stones")) {

                        } else if (urlToBeLoaded.contains("Post_Health")) {

                        } else if (urlToBeLoaded.contains("event/myEvent")) {

                        } else if (urlToBeLoaded.contains("Health_Survey")) {

                        } else if (urlToBeLoaded.contains("race#")) {
                            context.replaceFragment(MyRaceResultFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("Race_Achievment")) {
                            context.replaceFragment(MyRaceResultFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("group/myGroup")) {
                            context.startActivity(new Intent(context, CreateGroupActivity.class));
                        } else if (urlToBeLoaded.contains("contests/participate")) {
                            context.replaceFragment(ContestFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("contests")) {
                            context.replaceFragment(ContestFragment.getInstanceForDirectHome());
                        } else if (urlToBeLoaded.contains("group/index")) {
                            context.replaceFragment(CommunityFragment.getInstanceForDirectHome(1));
                        } */else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(urlToBeLoaded));
                            context.startActivity(browserIntent);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        });

         SimpleTarget targetnew = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                // do something with the bitmap
                // set it to an ImageView
                imageView.setImageBitmap(bitmap);
            }
        };
        return imageView;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}