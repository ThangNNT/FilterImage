package com.example.imagefilter.article.view;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.article.ClassDefine;
import com.example.imagefilter.article.base.Attachable;
import com.example.imagefilter.article.base.Focusable;
import com.example.imagefilter.article.base.Linkable;
import com.example.imagefilter.article.base.OnRemoveClickListener;
import com.example.imagefilter.article.base.Switchable;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewSubtitleBinding;
import com.example.imagefilter.databinding.ViewTextBinding;

public class SubtitleView extends FrameLayout implements Attachable, Focusable, Linkable, Switchable {
    private ViewSubtitleBinding mBinding;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnRemoveClickListener mOnRemoveClickListener;

    public SubtitleView(@NonNull Context context) {
        super(context);
        init();
    }

    public SubtitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubtitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mBinding = ViewSubtitleBinding.inflate(LayoutInflater.from(getContext()), this, true);
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
        return "<div class=\"" + ClassDefine.ATTACHABLE_CLASS + " " + ClassDefine.SUBTITLE_ATTACHMENT + "\">" + mBinding.edtContent.getHtml() + "</div>";
    }

    public void setSpanned(Spanned spanned){
        mBinding.edtContent.setText(spanned);
    }

    @Override
    public void focus(){
        mBinding.edtContent.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtContent);
    }

    @Override
    public void setLink(String text, String url){
         mBinding.edtContent.setLink(text, url);
    }


    public void setOnFocusChangeListener(OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }

    @Override
    public Spanned getSpanned() {
        return mBinding.edtContent.getEditableText();
    }

    @Override
    public int getSelectionStart() {
        return mBinding.edtContent.getSelectionStart();
    }

    @Override
    public void setSelectionStart(int position) {
        mBinding.edtContent.setSelection(position);
    }
}
