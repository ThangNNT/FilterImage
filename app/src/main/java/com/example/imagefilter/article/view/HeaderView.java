package com.example.imagefilter.article.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewHeaderBinding;

public class HeaderView extends FrameLayout implements Attachable, Focusable {
    private ViewHeaderBinding mBinding;

    public HeaderView(@NonNull Context context) {
        super(context);
        init();
    }

    public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        mBinding = ViewHeaderBinding.inflate(layoutInflater, this, true);
    }


    @Override
    public String getHtml() {
        String text = mBinding.edtHeader.getText().toString();
        int textSize = (int)  Utils.pxToSp(this.getContext(), mBinding.edtHeader.getTextSize());
        return "<h3 style=\"font-size:" + textSize + "px; white-space: normal; word-wrap: break-word; margin: 16px 16px 0 16px;\">"+text+"</h3>";
    }

    @Override
    public void focus() {
        mBinding.edtHeader.requestFocus();
        Utils.showKeyboard(getContext(), mBinding.edtHeader);

    }
}
