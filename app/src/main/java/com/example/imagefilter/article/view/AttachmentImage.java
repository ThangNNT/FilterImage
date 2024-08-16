package com.example.imagefilter.article.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.imagefilter.R;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewAttachmentImageBinding;

public class AttachmentImage extends LinearLayout implements Attachable {
    private ViewAttachmentImageBinding mBinding;
    private String mUrl;
    private String mTitlte;
    private boolean showDeleteButton = false;
    private OnUpdateClickListener onUpdateClickListener;
    public AttachmentImage(@NonNull Context context) {
        super(context);
        init();
    }

    public AttachmentImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttachmentImage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mBinding = ViewAttachmentImageBinding.inflate(layoutInflater, this, true);
        handleEditText();
        handleImageClick();
        handleModifiersLayoutVisibility();
        handleUpdateClick();
    }

    public void setImageUrl(String url){
        mUrl = url;
        Glide.with(this)
                .load(url).error(R.drawable.no_image)
                .into(mBinding.ivImage);
    }

    public void  setTitle(@Nullable String title){
        mTitlte = title;
    }
    @Nullable
    @Override
    public String getHtml() {
        if (mUrl == null) return null;
        StringBuilder builder = new StringBuilder();
        builder.append("<img style=\"max-width: 100%; height: auto;\" src=\"").append(mUrl).append("\">");
        int textSize = (int)  Utils.pxToSp(this.getContext(), mBinding.edtTitle.getTextSize());
        if (mTitlte != null) {
            builder.append("\n<h1 style=\"text-align:center; font-size: ").append(textSize).append("px;\"><strong>").append(mTitlte).append("</strong></h1>");
        }
        return builder.toString();
    }

    private void handleEditText(){
        mBinding.edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTitlte = s.toString();
            }
        });
    }

    private void handleImageClick(){
        mBinding.ivImage.setOnClickListener(v -> {
            showDeleteButton = !showDeleteButton;
            handleModifiersLayoutVisibility();
        });
    }

    private void handleModifiersLayoutVisibility(){
        mBinding.layoutModifiers.setVisibility(showDeleteButton? VISIBLE: INVISIBLE);
    }

    private void handleUpdateClick(){
        mBinding.ivDelete.setOnClickListener((v)->{
            if (onUpdateClickListener == null) return;
            onUpdateClickListener.onDeleted(this);
        });
        mBinding.ivEdit.setOnClickListener((v)->{
            if (onUpdateClickListener == null) return;
            onUpdateClickListener.onEdit(this);
        });
    }

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        onUpdateClickListener = listener;
    }

    public interface OnUpdateClickListener {
        void onDeleted(AttachmentImage view);
        void onEdit(AttachmentImage view);
    }
}
