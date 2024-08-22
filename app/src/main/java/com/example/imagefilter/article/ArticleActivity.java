package com.example.imagefilter.article;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.imagefilter.article.dialog.AddAttachmentImageDialog;
import com.example.imagefilter.article.dialog.AddAttachmentLinkDialog;
import com.example.imagefilter.article.base.Attachable;
import com.example.imagefilter.article.view.AttachmentImage;
import com.example.imagefilter.article.view.CodeBlockView;
import com.example.imagefilter.article.view.DividerView;
import com.example.imagefilter.article.base.Focusable;
import com.example.imagefilter.article.view.HeaderView;
import com.example.imagefilter.article.base.Linkable;
import com.example.imagefilter.article.view.QuoteView;
import com.example.imagefilter.article.view.TextAttachmentView;
import com.example.imagefilter.article.view.UnorderedListView;
import com.example.imagefilter.databinding.ActivityArticleBinding;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;
    private View currentFocusChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUp();
    }

    private void setUp() {
        binding.layoutArticle.addView(new HeaderView((this)));
        binding.tvPreview.setOnClickListener((v) -> {
            startActivity(PreviewActivity.newIntent(this, getHtml()));
        });
        // Text
        binding.layoutAttachment.tvText.setOnClickListener(v -> {
            TextAttachmentView editText = new TextAttachmentView(this);
            editText.setOnFocusChangeListener(((view, hasFocus) -> {;
                if (hasFocus){
                    currentFocusChild =  view;
                    binding.layoutAttachment.ivLink.setVisibility(View.VISIBLE);
                }
                else {
                    binding.layoutAttachment.ivLink.setVisibility(View.GONE);
                }
            }));
            editText.setOnRemoveClickListener(this::removeView);
            addView(editText);
        });
        // image
        binding.layoutAttachment.ivImage.setOnClickListener((v) -> {
            showAddAttachmentImageDialog();
        });
        // code block
        binding.layoutAttachment.ivCodeBlock.setOnClickListener((v)->{
            CodeBlockView codeBlockView = new CodeBlockView(this);
            codeBlockView.setOnRemoveClickListener(this::removeView);
            codeBlockView.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) currentFocusChild = view;
            });
            addView(codeBlockView);
        });
        // divider
        binding.layoutAttachment.ivDivider.setOnClickListener((v)->{
            DividerView dividerView = new DividerView((this));
            dividerView.setOnRemoveClickListener(this::removeView);
            dividerView.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) currentFocusChild = view;
            });
            addView(dividerView);
        });
        // quote
        binding.layoutAttachment.ivQuote.setOnClickListener((v) -> {
            QuoteView quoteView = new QuoteView(this);
            quoteView.setOnRemoveClickListener(this::removeView);
            quoteView.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) currentFocusChild = view;
                binding.layoutAttachment.ivLink.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            });
            addView(quoteView);
        });
        // ul list
        binding.layoutAttachment.ivList.setOnClickListener((v) -> {
            addUnorderedListView(null, -1);
        });
        // link
        binding.layoutAttachment.ivLink.setOnClickListener((v)-> {
            if (currentFocusChild instanceof Linkable){
                Linkable linkable = (Linkable) currentFocusChild;
                AddAttachmentLinkDialog dialog = new AddAttachmentLinkDialog(this);
                dialog.setListener((data)->{
                    linkable.setLink(data.getText(), data.getUrl());
                });
                dialog.show();
            }
        });
    }

    /**
     * remove a view and focus on previous view
     */
    private void removeView(View view){
        if (currentFocusChild == null) return;
        ViewGroup parent = binding.layoutArticle;
        int currentSelectedIndex = parent.indexOfChild(currentFocusChild);
        if (currentSelectedIndex >=1){
            View prevView = parent.getChildAt(currentSelectedIndex-1);
            if (prevView instanceof Focusable){
                ((Focusable) prevView).focus();
            }
        }
        parent.removeView(view);
    }

    /**
     * Add a view after the item that is focused.
     */
    private void addView(View view){
        ViewGroup parent = binding.layoutArticle;
        if (currentFocusChild == null) {
            parent.addView(view);
            if (view instanceof Focusable){
                ((Focusable) view).focus();
            }
            return;
        };
        int currentSelectedIndex = parent.indexOfChild(currentFocusChild);
        if (currentSelectedIndex != -1 && currentSelectedIndex < parent.getChildCount() - 1) {
            parent.addView(view, currentSelectedIndex + 1);
        } else parent.addView(view);
        if (view instanceof Focusable){
            ((Focusable) view).focus();
        }
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
        unorderedListView.setOnRemoveClickListener(this::removeView);
        if (initText!=null){
            unorderedListView.setText(initText);
        }
        unorderedListView.setOnAddUnorderedListViewListener((view, textAfter) -> {
            int indexOfCurrentChild = binding.layoutArticle.indexOfChild(view);
            if (indexOfCurrentChild != -1){
                addUnorderedListView(textAfter, indexOfCurrentChild+1);
            }
        });
        unorderedListView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) currentFocusChild = v;
            binding.layoutAttachment.ivLink.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        });
        if (index != -1) {
            binding.layoutArticle.addView(unorderedListView, index);
            unorderedListView.focus();
        } else
            addView(unorderedListView);
    }

    private void showAddAttachmentImageDialog(){
        AddAttachmentImageDialog dialog = new AddAttachmentImageDialog(this);
        dialog.setListener((data)->{
            AttachmentImage attachmentImage = new AttachmentImage((this));
            attachmentImage.setOnRemoveClickListener(this::removeView);
            attachmentImage.setOnEditClickListener((v) -> {
                if (v instanceof AttachmentImage) {
                    showEditAttachmentImageDialog((AttachmentImage) v);
                }
            });
            attachmentImage.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus){
                    currentFocusChild = v;
                }
            });
            attachmentImage.setImageUrl(data.getUrl());
            addView(attachmentImage);
        });
        dialog.show();
    }

    private void showEditAttachmentImageDialog(AttachmentImage attachmentImage){
        AddAttachmentImageDialog dialog = new AddAttachmentImageDialog(this);
        dialog.setListener((data)-> attachmentImage.setImageUrl(data.getUrl()));
        dialog.show();
    }
}