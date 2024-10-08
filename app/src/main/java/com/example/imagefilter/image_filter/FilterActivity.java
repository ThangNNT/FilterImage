package com.example.imagefilter.image_filter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imagefilter.R;
import com.example.imagefilter.databinding.ActivityFilterBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This Activity is only used for testing purposes. <br/>
 * You can set MainActivity as the launch screen to see how changes in parameters affect the image.
 */
public class FilterActivity extends AppCompatActivity {
    private ActivityFilterBinding binding;
    public static String IMAGE_URI_EXTRA = "IMAGE_URI_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupView();
    }
    private void setupView(){
        Uri uri = getIntent().getParcelableExtra(IMAGE_URI_EXTRA);
        binding.ivImage.setAltImageResource(R.drawable.heart);
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Drawable drawable = Drawable.createFromStream(inputStream, uri.toString() );
            binding.ivImage.setImageDrawable(drawable);
        } catch (FileNotFoundException e) {
        }
        binding.ivImage.requestLayout();
        binding.ivImage.setImagePanX(2);
        binding.sliderSaturation.setValues(10f);
        binding.sliderBrightness.setValues(10f);
        binding.sliderWarmth.setValues(10f);
        binding.sliderContrast.setValues(10f);
     //   binding.sliderCrossfade.setValues(0f);
        binding.sliderSaturation.addOnChangeListener((slider, value, fromUser) -> {
            float realValue = value/10f;
            binding.ivImage.setSaturation(realValue);
        });
        binding.sliderBrightness.addOnChangeListener((slider, value, fromUser) -> {
            float realValue = value/10f;
            binding.ivImage.setBrightness(realValue);
        });
        binding.sliderWarmth.addOnChangeListener((slider, value, fromUser) -> {
            float realValue = value/10f;
            binding.ivImage.setWarmth(realValue);
        });
        binding.sliderContrast.addOnChangeListener((slider, value, fromUser) -> {
            float realValue = value/10f;
            binding.ivImage.setContrast(realValue);
        });
        binding.sliderCrossfade.addOnChangeListener((slider, value, fromUser) -> {
            float realValue = value/10f;
            binding.ivImage.setCrossfade(realValue);
        });
    }

}