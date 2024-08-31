package com.example.imagefilter.article.view;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
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
import com.example.imagefilter.article.base.OnRemoveClickListener;
import com.example.imagefilter.article.base.Switchable;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewCodeBlockBinding;

public class CodeBlockView extends FrameLayout implements Attachable, Focusable, Switchable {

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
        Editable text = mBinding.edtContent.getText();
        return "<div class=\"" + ClassDefine.ATTACHABLE_CLASS + " " + ClassDefine.CODE_BLOCK_VIEW + "\"><code>" + Html.toHtml(text, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH) + "</code></div>";
    }

    @Override
    public void focus() {
        mBinding.edtContent.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtContent);

    }

    public void setText(String text){
        mBinding.edtContent.setText(text);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.mOnRemoveClickListener = listener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }

    @Override
    public Spanned getSpanned() {
        return mBinding.edtContent.getText();
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
