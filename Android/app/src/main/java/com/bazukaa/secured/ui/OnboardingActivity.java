package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bazukaa.secured.R;
import com.bazukaa.secured.adapters.OnboardingAdapter;
import com.bazukaa.secured.models.OnboardingItem;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    // Shared preferences
    public static final String SHARED_PREFERENCE = "sharedPrefs";
    public static final String FIRST_LAUNCH = "first launch";

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private Button buttonOnboardingAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicator);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);

        setupOnboardingItems();

        final ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewpager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });
        buttonOnboardingAction.setOnClickListener(v -> {
            if(onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem()+1);
            }else{
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(FIRST_LAUNCH, false);
                editor.apply();
                startActivity(new Intent(getApplicationContext(), PasswordActivity.class));
                finish();
            }
        });
    }
    private void setupOnboardingItems(){
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem item1 = new OnboardingItem();
        item1.setTitle("Security");
        item1.setDescription("Generate safe and secure passwords.\nStore and delete them anytime you want.");
        item1.setImage(R.drawable.o1);
        item1.setIndexImage(R.drawable.i1);

        OnboardingItem item2 = new OnboardingItem();
        item2.setTitle("Robust");
        item2.setDescription("SecureD requires no internet connection and runs offline.");
        item2.setImage(R.drawable.o2);
        item2.setIndexImage(R.drawable.i2);

        OnboardingItem item3 = new OnboardingItem();
        item3.setTitle("Fingerprint Auth");
        item3.setDescription("Fingerprint authentication can be enabled for additional security.");
        item3.setImage(R.drawable.o3);
        item3.setIndexImage(R.drawable.i3);

        OnboardingItem item4 = new OnboardingItem();
        item4.setTitle("Theme");
        item4.setDescription("Switch between light and dark mode easily");
        item4.setImage(R.drawable.o4);
        item4.setIndexImage(R.drawable.i4);

        onboardingItems.add(item1);
        onboardingItems.add(item2);
        onboardingItems.add(item3);
        onboardingItems.add(item4);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }
    private void setOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }
    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for(int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            }else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if(index == onboardingAdapter.getItemCount() - 1){
            buttonOnboardingAction.setText("Start");
        }else{
            buttonOnboardingAction.setText("Next");
        }
    }
}
