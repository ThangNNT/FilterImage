package com.example.imagefilter.article.view;

import android.content.Context;
import android.text.Editable;
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
import com.example.imagefilter.databinding.ViewUnorderedListBinding;

public class UnorderedListView extends FrameLayout implements Attachable, Focusable, Linkable {
    private ViewUnorderedListBinding mBinding;
    private OnRemoveClickListener mOnRemoveClickListener;
    private OnFocusChangeListener mOnFocusChangeListener;

    public UnorderedListView(@NonNull Context context) {
        super(context);
        init();
    }

    public UnorderedListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnorderedListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mBinding = ViewUnorderedListBinding.inflate(layoutInflater, this, true);
        mBinding.ivDelete.setVisibility(GONE);
        mBinding.ivDelete.setOnClickListener(v -> {
            if (mOnRemoveClickListener == null) return;
            mOnRemoveClickListener.onRemove(this);
        });
        mBinding.edtContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mBinding.ivDelete.setVisibility(View.VISIBLE);
            } else mBinding.ivDelete.setVisibility(View.GONE);
            if (mOnFocusChangeListener != null) {
                mOnFocusChangeListener.onFocusChange(this, hasFocus);
            }
        });
    }

    public void setOnRemoveClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }

    @Override
    public String getHtml() {
        String content = mBinding.edtContent.getHtml();
        return "<li class=\"" + ClassDefine.ATTACHABLE_CLASS + " " + ClassDefine.LIST_ITEM + "\">" + content + "</li>\n";
    }

    public void setText(String text) {
        mBinding.edtContent.setText(text);
    }

    public void setText(Editable text) {
        mBinding.edtContent.setText(text);
    }

    public void setSpanned(Spanned spanned) {
        mBinding.edtContent.setText(spanned);
    }

    @Override
    public void focus() {
        mBinding.edtContent.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtContent);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }

    @Override
    public void setLink(String text, String url) {
        mBinding.edtContent.setLink(text, url);
    }

}
