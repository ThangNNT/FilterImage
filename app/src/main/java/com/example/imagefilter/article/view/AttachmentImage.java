package com.example.imagefilter.article.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.imagefilter.R;
import com.example.imagefilter.article.base.OnEditClickListener;
import com.example.imagefilter.article.base.OnRemoveClickListener;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewAttachmentImageBinding;

public class AttachmentImage extends LinearLayout implements Attachable, Focusable {
    private ViewAttachmentImageBinding mBinding;
    private String mUrl;
    private String mTitlte;
    private OnRemoveClickListener mOnRemoveClickListener;
    private OnEditClickListener mOnEditClickListener;
    private boolean isImageFocus = false;
    private boolean isTitleFocus = false;
    private OnFocusChangeListener mOnFocusChangeListener;

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
        handleUpdateClick();
        mBinding.ivImage.setOnFocusChangeListener((v, hasFocus) -> {
            isImageFocus = hasFocus;
            onFocusChanged();

        });
        mBinding.edtTitle.setOnFocusChangeListener((v, hasFocus) -> {
            isTitleFocus = hasFocus;
            onFocusChanged();
        });
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

    @Override
    public void focus() {
        mBinding.edtTitle.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtTitle);
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
        mBinding.ivImage.setOnClickListener(View::requestFocus);
    }


    private void handleUpdateClick(){
        mBinding.ivDelete.setOnClickListener((v)->{
            if (mOnRemoveClickListener == null) return;
            mOnRemoveClickListener.onRemove(this);
        });
        mBinding.ivEdit.setOnClickListener((v)->{
            if (mOnEditClickListener == null) return;
            mOnEditClickListener.onEdit(this);
        });
    }

    private void onFocusChanged(){
        boolean hasFocus = false;
        if (isTitleFocus || isImageFocus) hasFocus = true;
        mBinding.layoutModifiers.setVisibility(hasFocus? VISIBLE: INVISIBLE);
        if (mOnFocusChangeListener == null) return;
        mOnFocusChangeListener.onFocusChange(this, hasFocus);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }

    public void setOnEditClickListener(OnEditClickListener mOnEditClickListener) {
        this.mOnEditClickListener = mOnEditClickListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }
}
