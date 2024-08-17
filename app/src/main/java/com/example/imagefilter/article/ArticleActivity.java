package com.example.imagefilter.article;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.imagefilter.article.dialog.AddAttachmentImageDialog;
import com.example.imagefilter.article.view.Attachable;
import com.example.imagefilter.article.view.AttachmentImage;
import com.example.imagefilter.article.view.CodeBlockView;
import com.example.imagefilter.article.view.DividerView;
import com.example.imagefilter.databinding.ActivityArticleBinding;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUp();
    }

    private void setUp() {
//        binding.edt.setText("Helllo");
//        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
//        int start = 0;
//        int end = 2;
//        int flag = Spannable.SPAN_INCLUSIVE_INCLUSIVE;
//        binding.edt.getText().setSpan(boldSpan, start, end, flag);
        //binding.atImage.setImageUrl("https://ckbox.cloud/c05dc7b9f5550792f90b/assets/QQk6k6MuFhhP/images/1080.webp");
        binding.tvBold.setOnClickListener((v) -> {
            binding.edt.setBoldEnable(true);
        });
        binding.tvPreview.setOnClickListener((v) -> {
            startActivity(PreviewActivity.newIntent(this, getHtml()));
        });
        binding.layoutAttachment.ivImage.setOnClickListener((v) -> {
            showAddAttachmentImageDialog();
        });
        binding.layoutAttachment.ivCodeBlock.setOnClickListener((v)->{
            CodeBlockView codeBlockView = new CodeBlockView(this);
            codeBlockView.setOnUpdateClickListener(view -> binding.layoutArticle.removeView(view));
            binding.layoutArticle.addView(codeBlockView);
        });
        binding.layoutAttachment.ivDivider.setOnClickListener((v)->{
            DividerView dividerView = new DividerView((this));
            dividerView.setOnUpdateClickListener( view -> binding.layoutArticle.removeView(view));
            binding.layoutArticle.addView(dividerView);
        });
    }

    private String getHtml(){
        int childCount = binding.layoutArticle.getChildCount();
        StringBuilder htmlResult = new StringBuilder();
        for (int i = 0; i < childCount; i++) {
            View child = binding.layoutArticle.getChildAt(i);
            if (child instanceof Attachable){
                String html = ((Attachable) child).getHtml();
                htmlResult.append(html);
            }
        }
        return htmlResult.toString();
    }

    private void showAddAttachmentImageDialog(){
        AddAttachmentImageDialog dialog = new AddAttachmentImageDialog(this);
        dialog.setListener((data)->{
            AttachmentImage attachmentImage = new AttachmentImage((this));
            attachmentImage.setOnUpdateClickListener(new AttachmentImage.OnUpdateClickListener() {
                @Override
                public void onDeleted(AttachmentImage view) {
                    ViewGroup parent = (ViewGroup) view.getParent();
                    parent.removeView(view);
                }

                @Override
                public void onEdit(AttachmentImage view) {
                    showEditAttachmentImageDialog(view);
                }
            });
            attachmentImage.setImageUrl(data.getUrl());
            binding.layoutArticle.addView(attachmentImage);
        });
        dialog.show();
    }

    private void showEditAttachmentImageDialog(AttachmentImage attachmentImage){
        AddAttachmentImageDialog dialog = new AddAttachmentImageDialog(this);
        dialog.setListener((data)-> attachmentImage.setImageUrl(data.getUrl()));
        dialog.show();
    }
}