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
import com.example.imagefilter.databinding.ViewCodeBlockBinding;

public class CodeBlockView extends FrameLayout implements Attachable, Focusable {

    private ViewCodeBlockBinding mBinding;
    private OnRemoveClickListener mOnRemoveClickListener;
    private OnFocusChangeListener mOnFocusChangeListener;

    public CodeBlockView(@NonNull Context context) {
        super(context);
        init();
    }

    public CodeBlockView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CodeBlockView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mBinding = ViewCodeBlockBinding.inflate(layoutInflater, this, true);
        mBinding.edtContent.setOnFocusChangeListener((v, hasFocus) -> {
            mBinding.ivDelete.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            if (mOnFocusChangeListener == null) return;
            mOnFocusChangeListener.onFocusChange(this, hasFocus);
        });
        mBinding.ivDelete.setOnClickListener((v)->{
            if (mOnRemoveClickListener == null) return;
            mOnRemoveClickListener.onRemove(this);
        });
    }
    @Override
    public String getHtml() {
        String text = mBinding.edtContent.getText().toString();
        int textSize = (int)  Utils.pxToSp(this.getContext(), mBinding.edtContent.getTextSize());
        return "<p style=\"background-color:rgba(221, 221, 221, 1); padding: 16px 8px 16px 8px; font-size: "+textSize+"px; margin: 16px 16px 0 16px; white-space: normal; word-wrap: break-word;\"><code>"+text+"</code></p>";
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
