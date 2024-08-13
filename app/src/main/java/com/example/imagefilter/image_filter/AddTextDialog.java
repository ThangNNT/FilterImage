package com.example.imagefilter.image_filter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.imagefilter.databinding.DialogAddTextBinding;

public class AddTextDialog extends Dialog {
    private DialogAddTextBinding binding;
    private OnConfirmListener listener;
    public AddTextDialog(@NonNull Context context) {
        super(context);
    }

    public void setListener(OnConfirmListener listener){
        this.listener = listener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogAddTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCancel.setOnClickListener((v)->{
            this.dismiss();
        });
        binding.btnOk.setOnClickListener((v)->{
            if (binding.edtText.getText().toString().equals("")) return;
            if (listener!=null){
                int color;
                if (binding.rdRed.isChecked()){
                    color = Color.RED;
                } else if (binding.rdBlue.isChecked()){
                    color = Color.BLUE;
                }
                else if (binding.rdGreen.isChecked()){
                    color = Color.GREEN;
                } else if (binding.rdGreen.isChecked()){
                    color = Color.YELLOW;
                }
                else color = Color.WHITE;
                listener.onConfirm(binding.edtText.getText().toString(), color);
            }
            dismiss();
        });

    }

    interface OnConfirmListener{
        void onConfirm(String text, int color);
    }
}
