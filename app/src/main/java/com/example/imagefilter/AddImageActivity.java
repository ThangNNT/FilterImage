package com.example.imagefilter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagefilter.databinding.ActivityAddImageBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AddImageActivity extends AppCompatActivity {
    public static String IMAGE_URI = "IMAGE_URI";
    public static String IMAGE_RESULT = "IMAGE_RESULT";
    private ActivityAddImageBinding binding;
    private DraggableTextView currentDeleteText;

    private FilterItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupView();
    }

    private void setupView() {
        binding.ivImage.setDrawingCacheEnabled(true);
        String uriString = getIntent().getStringExtra(IMAGE_URI);
        if (uriString != null) {
            try {
                Uri uri = Uri.parse(uriString);
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Drawable drawable = Drawable.createFromStream(inputStream, uri.toString());
                binding.ivImage.setImageDrawable(drawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        binding.btnAddText.setOnClickListener((v)-> {
            AddTextDialog dialog = new AddTextDialog(this);
            dialog.setListener((text, color)->{
                DraggableTextView draggableTextView = initDraggableTextView(text, color);
                binding.layoutImage.addView(draggableTextView);
            });
            dialog.show();
        });
        binding.layoutDelete.setOnClickListener((v)->{
            if (currentDeleteText!=null){
                binding.layoutImage.removeView(currentDeleteText);
                currentDeleteText = null;
                v.setVisibility(View.GONE);
            }
        });

        binding.ivFilter.setOnClickListener((v)->{
            RecyclerView rv = binding.rvFilters;
            if (rv.getVisibility() == View.GONE){
                rv.setVisibility(View.VISIBLE);
            }
            else {
                rv.setVisibility(View.GONE);
            }
        });

        binding.btnBack.setOnClickListener((v)->{
            finish();
        });

        binding.btnOk.setOnClickListener((v)->{
            combineImageAndReturn();
        });
        binding.rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        this.adapter = new FilterItemAdapter(uriString,
                Arrays.asList(
                        new FilterItem("Gray", 0f, 1f, 1f, 1f),
                        new FilterItem("Cold", 1f, 1f, 0.5f, 1f),
                        new FilterItem("Warm", 1f, 1f, 2f, 1f)
                ),
                (item -> {
                    ImageFilterView filterView = binding.ivImage;
                    filterView.setSaturation(item.getSaturation());
                    filterView.setContrast(item.getContrast());
                    filterView.setWarmth(item.getWarmth());
                    filterView.setBrightness(item.getBrightness());

                }));
        binding.rvFilters.setAdapter(this.adapter);
    }

    private DraggableTextView initDraggableTextView(String text, int color) {
        DraggableTextView draggableTextView = new DraggableTextView(this);
        draggableTextView.setText(text);
        draggableTextView.setTextColor(color);
        draggableTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        draggableTextView.setOnDoubleClickListener(view -> {
            binding.layoutDelete.setVisibility(View.VISIBLE);
            currentDeleteText = draggableTextView;
        });
        return draggableTextView;
    }

    private void combineImageAndReturn(){
        Bitmap bitmap =
                binding.ivImage.getDrawingCache();

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // resource bitmaps are immutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        for (int i = 0; i < binding.layoutImage.getChildCount(); i++) {
            View child = binding.layoutImage.getChildAt(i);
            if (child instanceof DraggableTextView){
                child.draw(canvas);
            }
        }

        Intent intent = new Intent(this, PostActivity.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            Uri uri = Utils.saveBitmap(this, bitmap, Bitmap.CompressFormat.JPEG, "image/jpeg", "combine"+System.currentTimeMillis()+ ".jpg");
            intent.putExtra(IMAGE_RESULT, uri);
            setResult(RESULT_OK, intent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finish();
    }
}