package com.example.imagefilter.article.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ViewTextBinding;

public class TextAttachmentView extends FrameLayout implements Attachable {
    private ViewTextBinding mBinding;
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
}
