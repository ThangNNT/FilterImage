package com.example.imagefilter.image_filter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imagefilter.databinding.ActivityPostBinding;

public class PostActivity extends AppCompatActivity {
    private ActivityPostBinding binding;

    private final ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    assert result.getData() != null;
                    Uri uri = result.getData().getParcelableExtra(AddImageActivity.IMAGE_RESULT);
                    binding.ivImage.setImageURI(uri);
                    binding.ivImage.setVisibility(View.VISIBLE);
                }
            });

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Intent intent = new Intent(this, AddImageActivity.class);
                    intent.putExtra(AddImageActivity.IMAGE_URI, uri);
                    startActivityIntent.launch(intent);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupView();
    }
    private void setupView(){
        binding.tvAddImage.setOnClickListener((v)->{
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

}