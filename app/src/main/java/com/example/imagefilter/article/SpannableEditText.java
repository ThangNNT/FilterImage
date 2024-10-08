package com.example.imagefilter.article;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.imagefilter.R;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.article.view.Attachable;

import java.util.ArrayList;
import java.util.Objects;

public class SpannableEditText extends AppCompatEditText implements Attachable {

    public SpannableEditText(Context context) {
        super(context);
        setUp();
    }

    public SpannableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public SpannableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    private void setUp() {
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.editext_custom_menu, menu);
                return true;

            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Editable text = getText();
                if (text == null) return false;
                int selectionStart = getSelectionStart();
                int selectionEnd = getSelectionEnd();
                if (item.getItemId() == R.id.action_bold) {
                    StyleSpan[] styleSpans = text.getSpans(selectionStart, selectionEnd, StyleSpan.class);
                    boolean hasBoldSpan = false;
                    // if span exist, remove span
                    for (StyleSpan span : styleSpans) {
                        if (span.getStyle() == Typeface.BOLD) {
                            hasBoldSpan = true;
                            int spanStart = text.getSpanStart(span);
                            int spanEnd = text.getSpanEnd(span);
                            if (spanStart < selectionStart) {
                                text.setSpan(new StyleSpan(Typeface.BOLD), spanStart, selectionStart, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            }
                            if (spanEnd > selectionEnd) {
                                text.setSpan(new StyleSpan(Typeface.BOLD), selectionEnd, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            }
                            text.removeSpan(span);
                        }
                    }
                    // if span does not exist, add span
                    if (!hasBoldSpan) {
                        text.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_italic) {
                    StyleSpan[] styleSpans = text.getSpans(selectionStart, selectionEnd, StyleSpan.class);
                    boolean hasItalicSpan = false;
                    for (StyleSpan span : styleSpans) {
                        if (span.getStyle() == Typeface.ITALIC) {
                            hasItalicSpan = true;
                            int spanStart = text.getSpanStart(span);
                            int spanEnd = text.getSpanEnd(span);
                            if (spanStart < selectionStart) {
                                text.setSpan(new StyleSpan(Typeface.ITALIC), spanStart, selectionStart, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            }
                            if (spanEnd > selectionEnd) {
                                text.setSpan(new StyleSpan(Typeface.ITALIC), selectionEnd, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            }
                            text.removeSpan(span);
                        }
                    }
                    if (!hasItalicSpan) {
                        text.setSpan(new StyleSpan(Typeface.ITALIC), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_underline) {
                    UnderlineSpan[] spans = text.getSpans(selectionStart, selectionEnd, UnderlineSpan.class);
                    for (UnderlineSpan span : spans) {
                        int spanStart = text.getSpanStart(span);
                        int spanEnd = text.getSpanEnd(span);
                        if (spanStart < selectionStart) {
                            text.setSpan(new UnderlineSpan(), spanStart, selectionStart, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                        if (spanEnd > selectionEnd) {
                            text.setSpan(new UnderlineSpan(), selectionEnd, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                        text.removeSpan(span);
                    }
                    if (spans.length == 0) {
                        text.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    public String getHtml() {
        return Html.toHtml(getEditableText(), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
    }

    @Override
    public void focus() {
        requestFocus();
        Utils.showKeyboard(getContext(), this);
    }
}
