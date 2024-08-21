package com.example.imagefilter.article.view;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.article.base.OnRemoveClickListener;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewTextBinding;

public class TextAttachmentView extends FrameLayout implements Attachable, Focusable {
    private ViewTextBinding mBinding;
    private View.OnFocusChangeListener mOnFocusChangeListener;
    private OnRemoveClickListener mOnRemoveClickListener;

    public TextAttachmentView(@NonNull Context context) {
        super(context);
        init();
    }

    public TextAttachmentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextAttachmentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mBinding = ViewTextBinding.inflate(LayoutInflater.from(getContext()), this, true);
        mBinding.edtContent.setOnFocusChangeListener((v, hasFocus)->{
            mBinding.ivDelete.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            if (mOnFocusChangeListener == null) return;
            mOnFocusChangeListener.onFocusChange(this, hasFocus);
        });
        mBinding.ivDelete.setOnClickListener(v -> {
            if (mOnRemoveClickListener == null) return;
            mOnRemoveClickListener.onRemove(this);
        });
    }

    @Override
    public String getHtml() {
        return "<div style = \" margin: 16px 16px 0 16px;  white-space: normal; word-wrap: break-word;  \">" + mBinding.edtContent.getHtml() + "</div>";
    }

    @Override
    public void focus(){
        mBinding.edtContent.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtContent);
    }

    public void setLink(String text, String url){
        Editable editable = mBinding.edtContent.getText();
        if (editable == null) return;
        int selectedStart = mBinding.edtContent.getSelectionStart();
        editable.insert(selectedStart, text);
        mBinding.edtContent.setSelection(selectedStart + text.length());
        editable.setSpan(new URLSpan(url), selectedStart, selectedStart + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public void setOnFocusChangeListener(View.OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }
}
