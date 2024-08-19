package com.example.imagefilter.article;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.imagefilter.article.dialog.AddAttachmentImageDialog;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.article.view.Attachable;
import com.example.imagefilter.article.view.AttachmentImage;
import com.example.imagefilter.article.view.CodeBlockView;
import com.example.imagefilter.article.view.DividerView;
import com.example.imagefilter.article.view.HeaderView;
import com.example.imagefilter.article.view.QuoteView;
import com.example.imagefilter.article.view.TextAttachmentView;
import com.example.imagefilter.article.view.UnorderedListView;
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
//        binding.tvBold.setOnClickListener((v) -> {
//            binding.edt.setBoldEnable(true);
//        });
        binding.layoutArticle.addView(new HeaderView((this)));
        binding.tvPreview.setOnClickListener((v) -> {
            startActivity(PreviewActivity.newIntent(this, getHtml()));
        });
        binding.layoutAttachment.tvText.setOnClickListener(v -> {
            TextAttachmentView editText = new TextAttachmentView(this);
            binding.layoutArticle.addView(editText);
            editText.focus();
        });
        binding.layoutAttachment.ivImage.setOnClickListener((v) -> {
            showAddAttachmentImageDialog();
        });
        binding.layoutAttachment.ivCodeBlock.setOnClickListener((v)->{
            CodeBlockView codeBlockView = new CodeBlockView(this);
            codeBlockView.setOnRemoveClickListener(view -> binding.layoutArticle.removeView(view));
            binding.layoutArticle.addView(codeBlockView);
        });
        binding.layoutAttachment.ivDivider.setOnClickListener((v)->{
            DividerView dividerView = new DividerView((this));
            dividerView.setOnRemoveClickListener( view -> binding.layoutArticle.removeView(view));
            binding.layoutArticle.addView(dividerView);
        });
        binding.layoutAttachment.ivQuote.setOnClickListener((v) -> {
            QuoteView quoteView = new QuoteView(this);
            quoteView.setOnRemoveClickListener(view -> binding.layoutArticle.removeView(view));
            binding.layoutArticle.addView(quoteView);
        });
        binding.layoutAttachment.ivList.setOnClickListener((v) -> {
            addUnorderedListView(null, -1);
        });
    }

    private String getHtml() {
        int childCount = binding.layoutArticle.getChildCount();
        StringBuilder htmlResult = new StringBuilder();
        for (int i = 0; i < childCount; i++) {
            View child = binding.layoutArticle.getChildAt(i);
            if (child instanceof UnorderedListView) {
                View prevView = null;
                int prevIndex = i - 1;
                if (prevIndex >= 0) prevView = binding.layoutArticle.getChildAt(prevIndex);
                // If it's the first list item, you need to add a UL tag at the beginning.
                if (!(prevView instanceof UnorderedListView)) {
                    htmlResult.append("<ul>");
                }

                String html = ((Attachable) child).getHtml();
                htmlResult.append(html);

                View nextView = null;
                int nextIndex = i + 1;
                if (nextIndex < childCount) nextView = binding.layoutArticle.getChildAt(nextIndex);
                // If it's the last list item, you need to add a UL tag at the beginning.
                if (!(nextView instanceof  UnorderedListView)){
                    htmlResult.append("</ul>");
                }

            } else if (child instanceof Attachable) {
                String html = ((Attachable) child).getHtml();
                htmlResult.append(html);
            }
        }
        return htmlResult.toString();
    }

    private void addUnorderedListView(@Nullable String initText, int index){
        UnorderedListView unorderedListView = new UnorderedListView(this);
        unorderedListView.setOnRemoveClickListener(view -> binding.layoutArticle.removeView(view));
        if (initText!=null){
            unorderedListView.setText(initText);
        }
        unorderedListView.setOnAddUnorderedListViewListener((view, textAfter) -> {
            int indexOfCurrentChild = binding.layoutArticle.indexOfChild(view);
            if (indexOfCurrentChild != -1){
                addUnorderedListView(textAfter, indexOfCurrentChild+1);
            }
        });
        if (index != -1) {
            binding.layoutArticle.addView(unorderedListView, index);
        } else
            binding.layoutArticle.addView(unorderedListView);
        unorderedListView.focus();
        Utils.showKeyboard(this, unorderedListView.getEditText());
    }

    private void showAddAttachmentImageDialog(){
        AddAttachmentImageDialog dialog = new AddAttachmentImageDialog(this);
        dialog.setListener((data)->{
            AttachmentImage attachmentImage = new AttachmentImage((this));
            attachmentImage.setOnRemoveClickListener((v) -> {
                ViewGroup parent = (ViewGroup) v.getParent();
                parent.removeView(v);
            });
            attachmentImage.setOnEditClickListener((v) -> {
                if (v instanceof AttachmentImage) {
                    showEditAttachmentImageDialog((AttachmentImage) v);
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