package com.example.imagefilter.image_filter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.ViewHolder> {
    private List<FilterItem> items;
    private OnClickListener listener;
    private Uri uri;
    FilterItemAdapter(Uri uri, List<FilterItem> items, OnClickListener listener){
        this.items = items;
        this.listener = listener;
        this.uri = uri;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.example.imagefilter.databinding.ItemFilterBinding binding = com.example.imagefilter.databinding.ItemFilterBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setUpView(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public com.example.imagefilter.databinding.ItemFilterBinding binding;
        public ViewHolder(com.example.imagefilter.databinding.ItemFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setUpView(FilterItem item){
            ImageFilterView filterView = binding.ivImage;
            try {
                InputStream inputStream = binding.getRoot().getContext().getContentResolver().openInputStream(uri);
                Drawable drawable = Drawable.createFromStream(inputStream, uri.toString() );
                filterView.setImageDrawable(drawable);
            } catch (FileNotFoundException e) {
            }
            filterView.setSaturation(item.getSaturation());
            filterView.setContrast(item.getContrast());
            filterView.setWarmth(item.getWarmth());
            filterView.setBrightness(item.getBrightness());
            binding.tvFilterName.setText(item.getName());
            binding.getRoot().setOnClickListener((v)->{
                if (listener!=null){
                    listener.onClick(item);
                }
            });
        }
    }

    interface OnClickListener{
        void onClick(FilterItem item);
    }
}
