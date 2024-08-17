package com.example.imagefilter.article.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imagefilter.databinding.ViewDividerBinding;

public class DividerView extends FrameLayout implements Attachable {
    private boolean mDisplayDelete = false;

    private OnUpdateClickListener mOnUpdateClickListener;

    public DividerView(@NonNull Context context) {
        super(context);
        init();
    }

    public DividerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DividerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        ViewDividerBinding mBinding = ViewDividerBinding.inflate(layoutInflater, this, true);
        // hide delete button when init
        mBinding.ivDelete.setVisibility(mDisplayDelete ? VISIBLE : GONE);
        mBinding.getRoot().setOnClickListener((v) -> {
            mDisplayDelete = !mDisplayDelete;
            mBinding.ivDelete.setVisibility(mDisplayDelete ? VISIBLE : GONE);
        });
        mBinding.ivDelete.setOnClickListener((v) -> {
            if (mOnUpdateClickListener == null) return;
            mOnUpdateClickListener.onDeleted(this);
        });
    }

    public void setOnUpdateClickListener(OnUpdateClickListener mOnUpdateClickListener) {
        this.mOnUpdateClickListener = mOnUpdateClickListener;
    }


    @Override
    public String getHtml() {
        return "<style>" +
                "  .simple-divider {" +
                "    text-align: center;" +
                "    margin: 20px 0;" +
                "  }" +
                "  .simple-divider span {" +
                "    padding: 0 10px;" +
                "    font-size: 32px;" +
                "    color: gray;" +
                "    letter-spacing: 8px;"+
                "  }" +
                "</style>" +
                "<div class=\"simple-divider\"><span>•••</span></div>";
    }

    public interface OnUpdateClickListener {
        void onDeleted(DividerView view);
    }
}
