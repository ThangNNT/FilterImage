package com.example.imagefilter.article.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.imagefilter.article.base.BaseDialog;
import com.example.imagefilter.article.model.AttachmentLinkResult;
import com.example.imagefilter.databinding.DialogAddLinkBinding;

public class AddAttachmentLinkDialog extends BaseDialog<DialogAddLinkBinding, AttachmentLinkResult> {
    public AddAttachmentLinkDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogAddLinkBinding getViewViewBinding() {
        return DialogAddLinkBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupView() {
        mBinding.btnOk.setOnClickListener((v)->{
            if (listener == null) return;
            String url = mBinding.edtLink.getText().toString();
            String text = mBinding.edtText.getText().toString();
            listener.onConfirm(new AttachmentLinkResult(url, text));
            dismiss();
        });
        mBinding.btnCancel.setOnClickListener((v)->{
            this.dismiss();
        });
    }
}
