package com.example.imagefilter.article.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.imagefilter.article.base.BaseDialog;
import com.example.imagefilter.article.model.AttachmentImageResult;
import com.example.imagefilter.databinding.DialogAddAttchmentImagetBinding;

public class AddAttachmentImageDialog extends BaseDialog<DialogAddAttchmentImagetBinding, AttachmentImageResult> {
    public AddAttachmentImageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogAddAttchmentImagetBinding getViewViewBinding() {
        return DialogAddAttchmentImagetBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupView() {
        mBinding.btnOk.setOnClickListener((v)->{
            if (listener == null) return;
            String url = mBinding.edtText.getText().toString();
            listener.onConfirm(new AttachmentImageResult(url));
            dismiss();
        });
        mBinding.btnCancel.setOnClickListener((v)->{
            this.dismiss();
        });
    }
}
