package com.example.imagefilter.article.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.article.base.OnRemoveClickListener;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewQuoteBinding;

public class QuoteView extends FrameLayout implements Attachable, Focusable {
    private ViewQuoteBinding mBinding;
    private OnRemoveClickListener mOnRemoveClickListener;
    private OnFocusChangeListener mOnFocusChangeListener;
    public QuoteView(@NonNull Context context) {
        super(context);
        init();
    }

    public QuoteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuoteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mBinding = ViewQuoteBinding.inflate(layoutInflater, this, true);
        mBinding.ivDelete.setVisibility(GONE);
        mBinding.ivDelete.setOnClickListener(v -> {
            if (mOnRemoveClickListener == null) return;
            mOnRemoveClickListener.onRemove(this);
        });
        mBinding.edtContent.setOnFocusChangeListener((v, hasFocus) -> {
            mBinding.ivDelete.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            if (mOnFocusChangeListener != null){
                mOnFocusChangeListener.onFocusChange(this, hasFocus);
            }
        });
    }

    @Override
    public String getHtml() {
        String quote = mBinding.edtContent.getText().toString();
        int textSize = (int)  Utils.pxToSp(this.getContext(), mBinding.edtContent.getTextSize());
        return "<blockquote style=\"font-size:" + textSize + "px; white-space: normal; word-wrap: break-word;\">" + quote + " </blockquote>";
    }

    @Override
    public void focus() {
        mBinding.edtContent.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtContent);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.mOnRemoveClickListener = listener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }
}
