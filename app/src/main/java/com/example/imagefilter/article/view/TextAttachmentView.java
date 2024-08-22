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
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewTextBinding;

public class TextAttachmentView extends FrameLayout implements Attachable, Focusable, Linkable {
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
        return "<div class=\"" + ClassDefine.ATTACHABLE_CLASS + " " + ClassDefine.TEXT_ATTACHMENT + "\">" + mBinding.edtContent.getHtml() + "</div>";
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


    public void setOnFocusChangeListener(View.OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }
}
