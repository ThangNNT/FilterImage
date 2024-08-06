package com.example.imagefilter;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import com.example.imagefilter.databinding.ActivityMainBinding;

import java.io.IOException;

/**
 *  * This Activity is only used for testing purposes.
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupView(){
        binding.ivImage.setDrawingCacheEnabled(true);
        binding.btnOk.setOnClickListener((v)-> combineImage());
        binding.btnFilter.setOnClickListener((v)-> {
            combineImage();
            try {
                Uri uri = Utils.saveBitmap(this, bitmap, Bitmap.CompressFormat.JPEG, "image/jpeg", "combine"+System.currentTimeMillis()+ ".jpg");
                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra(FilterActivity.IMAGE_URI_EXTRA, uri);
                startActivity(intent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void combineImage(){
        DraggableTextView textView = binding.tvTest;
        Bitmap bitmap =
              binding.ivImage.getDrawingCache();

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // resource bitmaps are immutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        textView.draw(canvas);
        binding.ivResult.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }
}