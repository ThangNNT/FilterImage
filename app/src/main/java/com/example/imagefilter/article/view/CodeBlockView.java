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

public class CodeBlockView extends FrameLayout implements Attachable {

    private ViewCodeBlockBinding mBinding;
    private OnRemoveClickListener mOnRemoveClickListener;
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
            if (hasFocus) {
                mBinding.ivDelete.setVisibility(View.VISIBLE);
            } else mBinding.ivDelete.setVisibility(View.GONE);
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
}
