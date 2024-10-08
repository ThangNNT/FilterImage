package com.example.imagefilter.article.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.article.base.OnRemoveClickListener;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewUnorderedListBinding;

public class UnorderedListView extends FrameLayout implements Attachable {
    private ViewUnorderedListBinding mBinding;
    private OnRemoveClickListener mOnRemoveClickListener;
    private OnAddUnorderedListViewListener mOnAddUnorderedListViewListener;
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

    private void init(){
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
        });
        mBinding.edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int breakLineIndex = s.toString().indexOf("\n");
                if (breakLineIndex != -1) {
                    CharSequence text = s.toString().subSequence(0, breakLineIndex);
                    CharSequence textAfter = null;
                    if (breakLineIndex < s.length() - 2) {
                        textAfter = s.toString().subSequence(breakLineIndex+1, s.length());
                    }
                    mBinding.edtContent.setText(text);
                    if (textAfter != null && mOnAddUnorderedListViewListener != null) {
                        mOnAddUnorderedListViewListener.addView(UnorderedListView.this, textAfter.toString());
                    } else if (mOnAddUnorderedListViewListener != null) {
                        mOnAddUnorderedListViewListener.addView(UnorderedListView.this, null);
                    }
                }
            }
        });
    }

    public void setOnRemoveClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }

    public void setOnAddUnorderedListViewListener(OnAddUnorderedListViewListener mOnAddUnorderedListViewListener) {
        this.mOnAddUnorderedListViewListener = mOnAddUnorderedListViewListener;
    }

    @Override
    public String getHtml() {
        String content = mBinding.edtContent.getText().toString();
        int textSize = (int)  Utils.pxToSp(this.getContext(), mBinding.edtContent.getTextSize());
        return "<li style=\"font-size:" + textSize + "px; white-space: normal; word-wrap: break-word;\">" + content + "</li>\n";
    }

    public void setText(String text){
        mBinding.edtContent.setText(text);
    }

    @Override
    public void focus(){
        mBinding.edtContent.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtContent);
    }

    public EditText getEditText(){
        return mBinding.edtContent;
    }

    public interface OnAddUnorderedListViewListener {
        void addView(UnorderedListView view, @Nullable String textAfter);
    }
}
