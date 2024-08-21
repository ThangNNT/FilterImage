package com.example.imagefilter.article.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.article.ClassDefine;
import com.example.imagefilter.article.base.Attachable;
import com.example.imagefilter.article.base.Focusable;
import com.example.imagefilter.article.base.OnRemoveClickListener;
import com.example.imagefilter.databinding.ViewDividerBinding;

public class DividerView extends FrameLayout implements Attachable, Focusable {
    private ViewDividerBinding mBinding;
    private OnFocusChangeListener mOnFocusChangeListener;

    private OnRemoveClickListener mOnRemoveClickListener;

    public DividerView(@NonNull Context context) {
        super(context);
        init();
    }

    public DividerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DividerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        mBinding = ViewDividerBinding.inflate(layoutInflater, this, true);
        mBinding.getRoot().setOnClickListener((v) -> {
            focus();
        });
        mBinding.ivDelete.setOnClickListener((v) -> {
            if (mOnRemoveClickListener == null) return;
            mOnRemoveClickListener.onRemove(this);
        });
        mBinding.getRoot().setOnFocusChangeListener((v, hasFocus) ->{
            mBinding.ivDelete.setVisibility(hasFocus ? VISIBLE : GONE);
            if (mOnFocusChangeListener == null) return;
            mOnFocusChangeListener.onFocusChange(this, hasFocus);
        });
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.mOnRemoveClickListener = listener;
    }


    @Override
    public String getHtml() {
        return "<div class=\"" + ClassDefine.ATTACHABLE_CLASS + " " + ClassDefine.DIVIDER + "\"><span>•••</span></div>";
    }

    @Override
    public void focus() {
        mBinding.getRoot().requestFocus();
    }

    public void setOnFocusChangeListener(OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }
}
