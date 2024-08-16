package com.example.imagefilter.article.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;
import com.example.imagefilter.article.model.BaseDialogResult;

public abstract class BaseDialog<VB extends ViewBinding, Result extends BaseDialogResult> extends Dialog {
    protected VB mBinding;

    protected OnConfirmListener<Result> listener;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    protected abstract VB getViewViewBinding();

    protected abstract void setupView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mBinding = getViewViewBinding();
        setContentView(mBinding.getRoot());
        setupView();
    }

    public void setListener(OnConfirmListener<Result> listener){
        this.listener = listener;
    }

    public interface OnConfirmListener<Result extends BaseDialogResult> {
        void onConfirm(Result result);
    }
}
