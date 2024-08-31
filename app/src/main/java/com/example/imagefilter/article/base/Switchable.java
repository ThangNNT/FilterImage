package com.example.imagefilter.article.base;

import android.text.Spanned;

public interface Switchable {
    Spanned getSpanned();

    int getSelectionStart();

    void setSelectionStart(int position);
}
