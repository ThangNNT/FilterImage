package com.example.imagefilter.article;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;

public class SpannableEditText extends AppCompatEditText {
    private static final int INDEX_NOT_FOUND = -1;
    private CharSequence mPrevText;
    private ArrayList<SpanInfo> spanMap = new ArrayList<>();
    private ArrayList<SpanInfo> tempMap = new ArrayList<>();
    private boolean boldEnable = false;
    private boolean italicEnable = false;
    private boolean underlineEnable = false;

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

    private void setUp(){
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                Log.d("AAAAAAAAAAAAA", "beforeTextChanged: " + text.toString() + ", start: " + start + ", count: " + count + ", after: " + after);
                mPrevText = text.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("AAAAAAAAAAAA", "mPrevText: "+ mPrevText.toString());
                Log.d("AAAAAAAAAAAAA", "onTextChanged: " + s.toString() + ", start: " + start + ", before: " + before + ", count: " + count);
                int diffIndex = findIndexOfDifferentStart(mPrevText, s);
                if (isDeleteText(mPrevText, s) ){
                    Log.d("AAAAAAAAAAAA", "isDeleteText");
                    onHandleDeleted(diffIndex, before);
                }
                if (isAddText(mPrevText, s)){
                    Log.d("AAAAAAAAAAAA", "isAddText");
                    // empty case
                    if (mPrevText.length() == 0){
                        spanMap.add(new SpanInfo(0, count, boldEnable, italicEnable, underlineEnable, false, ""));
                        tempMap = SpannableEditText.this.clone(spanMap);
                    }else if(start == mPrevText.length()){
                        //insert on last
                        if (spanMap.size()>0){
                            SpanInfo lastSpan = spanMap.get(spanMap.size()-1);
                            if (match(lastSpan)){
                                int end = lastSpan.getEnd();
                                lastSpan.setEnd(end+1);
                            }
                            else {
                                spanMap.add(new SpanInfo(spanMap.size(), spanMap.size()+count, boldEnable, italicEnable, underlineEnable, false, ""));
                                tempMap = SpannableEditText.this.clone(spanMap);
                            }
                        }
                    } else {
                        boolean isBeforeMatch = false;
                        int positionOffset = 0;
                        for(int i = 0; i< spanMap.size(); i++){
                            SpanInfo spanItem = spanMap.get(i);
                            int spanStart = spanItem.getStart();
                            int spanEnd = spanItem.getEnd();
                            if (isBeforeMatch){
                                spanItem.setStart(spanStart+count);
                                spanItem.setEnd(spanEnd+count);
                            }
                            else if (diffIndex >= spanStart && diffIndex+count<=spanEnd){
                                // span property of span item is the same view span setting
                                if (match(spanItem)){
                                    spanItem.setEnd(spanEnd+count);
                                    isBeforeMatch = true;
                                } else if (spanStart == diffIndex){
                                    SpanInfo newSpan = spanItem.clone();

                                    spanItem.setEnd(diffIndex + count);
                                    setViewTextSpanToSpanInfo(spanItem);

                                    newSpan.setStart(spanStart + count);
                                    newSpan.setEnd(spanEnd + count);
                                    tempMap.add(i + 1 + positionOffset, newSpan);
                                    positionOffset++;
                                    isBeforeMatch = true;
                                }
                                else if (spanEnd == diffIndex+count){
                                    SpanInfo newSpan = spanItem.clone();
                                    spanItem.setEnd(diffIndex-1);
                                    setViewTextSpanToSpanInfo(newSpan);
                                    newSpan.setStart(diffIndex);
                                    tempMap.add(i+1+positionOffset, newSpan);
                                    isBeforeMatch = true;
                                }
                                else {
                                    SpanInfo newSpan = spanItem.clone();
                                    SpanInfo newOldSpan = spanItem.clone();

                                    spanItem.setEnd(diffIndex-1);

                                    newSpan.setStart(diffIndex);
                                    newSpan.setEnd(diffIndex+count);
                                    setViewTextSpanToSpanInfo(newSpan);
                                    tempMap.add(i+positionOffset+1, newSpan);
                                    positionOffset++;

                                    newOldSpan.setStart(newOldSpan.getStart()+1);
                                    newOldSpan.setEnd(spanEnd+count);
                                    tempMap.add(i+positionOffset+1, newOldSpan);
                                    positionOffset++;
                                    isBeforeMatch = true;

                                }
                            }
                        }
                        spanMap = SpannableEditText.this.clone(tempMap);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                notifySpanChanged();
            }
        });
    }

    private void notifySpanChanged(){
        try {
//            Objects.requireNonNull(getText()).clearSpans();
            for (SpanInfo spanItem: spanMap){
                if (spanItem.isBold()){
                    StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                    int flag = Spannable.SPAN_INCLUSIVE_INCLUSIVE;
                    Log.d("AAAAAAAAAAA", "notifySpanChanged: "+spanItem.getStart() +", "+ spanItem.getEnd());
                    getText().setSpan(boldSpan, spanItem.getStart(), spanItem.getEnd(), flag);
                }
            }
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
            Log.d("AAAAAAAAAAAAAAAA", ex.getMessage());
        }
    }

    private void onHandleDeleted(int diffIndex, int deletionCount){
        Log.d("AAAAAAAAAAAAAA", "onHandleDeleted: "+diffIndex);
        if ("".equals(getText())){
            spanMap.clear();
            tempMap.clear();
            return;
        }
        for (SpanInfo spanItem : spanMap) {
            if (diffIndex <= spanItem.getStart()) {
                int spanStart = spanItem.getStart();
                int spanEnd = spanItem.getEnd();
                spanItem.setStart(spanStart - deletionCount);
                spanItem.setEnd(spanEnd - deletionCount);
            } else if (diffIndex > spanItem.getStart() && diffIndex <= spanItem.getEnd()) {
                spanItem.setEnd(spanItem.getEnd() - deletionCount);
            }
        }
    }

    private void onHandleAdded(int diffIndex, int count, int startPosition){
        if (mPrevText.length() == 0){
            spanMap.add(new SpanInfo(0, count-1, boldEnable, italicEnable, underlineEnable, false, ""));
            tempMap = SpannableEditText.this.clone(spanMap);
        }else if(startPosition == mPrevText.length()){
            //insert on last
            if (spanMap.size()>0){
                SpanInfo lastSpan = spanMap.get(spanMap.size()-1);
                if (match(lastSpan)){
                    int end = lastSpan.getEnd();
                    lastSpan.setEnd(end+1);
                }
                else {
                    spanMap.add(new SpanInfo(spanMap.size(), spanMap.size()+count, boldEnable, italicEnable, underlineEnable, false, ""));
                    tempMap = SpannableEditText.this.clone(spanMap);
                }
            }
        }
        else {
            boolean isMatchBefore = false;
            int positionOffset = 0;
            for(int i = 0; i< spanMap.size(); i++){
                SpanInfo spanItem = spanMap.get(i);
                int spanStart = spanItem.getStart();
                int spanEnd = spanItem.getEnd();
                if (isMatchBefore){
                    spanItem.setStart(spanStart+count);
                    spanItem.setEnd(spanEnd+count);
                }
                else if (diffIndex >= spanStart && diffIndex+count<=spanEnd){
                    // span property of span item is the same view span setting
                    if (match(spanItem)){
                        spanItem.setEnd(spanEnd+count);
                        isMatchBefore = true;
                    } else if (spanStart == diffIndex){
                        SpanInfo newSpan = spanItem.clone();

                        spanItem.setEnd(diffIndex + count);
                        setViewTextSpanToSpanInfo(spanItem);

                        newSpan.setStart(spanStart + count);
                        newSpan.setEnd(spanEnd + count);
                        tempMap.add(i + 1 + positionOffset, newSpan);
                        positionOffset++;
                        isMatchBefore = true;
                    }
                    else if (spanEnd == diffIndex+count){
                        SpanInfo newSpan = spanItem.clone();
                        spanItem.setEnd(diffIndex-1);
                        setViewTextSpanToSpanInfo(newSpan);
                        newSpan.setStart(diffIndex);
                        tempMap.add(i+1+positionOffset, newSpan);
                        isMatchBefore = true;
                    }
                    else {
                        SpanInfo newSpan = spanItem.clone();
                        SpanInfo newOldSpan = spanItem.clone();

                        spanItem.setEnd(diffIndex-1);

                        newSpan.setStart(diffIndex);
                        newSpan.setEnd(diffIndex+count);
                        setViewTextSpanToSpanInfo(newSpan);
                        tempMap.add(i+positionOffset+1, newSpan);
                        positionOffset++;

                        newOldSpan.setStart(newOldSpan.getStart()+1);
                        newOldSpan.setEnd(spanEnd+count);
                        tempMap.add(i+positionOffset+1, newOldSpan);
                        positionOffset++;
                        isMatchBefore = true;
                    }
                }
            }
            spanMap = SpannableEditText.this.clone(tempMap);

        }
    }

    /**
     *
     * @param prevText
     * @param currentText
     * @return text changed position start
     */
    private int findIndexOfDifferentStart(CharSequence prevText, CharSequence currentText){
        int currentLength = currentText.length();
        int prevLength = prevText.length();
        int i;
        for (i = 0; i < currentLength && i < prevLength; ++i) {
            if (currentText.charAt(i) != prevText.charAt(i)) {
                break;
            }
        }
        if (i < currentLength || i < prevLength) {
            return i;
        }
        return INDEX_NOT_FOUND;
    }

    private boolean isDeleteText(CharSequence prevText, CharSequence currentText){
        return prevText.length()>currentText.length();
    }

    private boolean isAddText(CharSequence prevText, CharSequence currentText){
        return prevText.length() < currentText.length();
    }

    public void setBoldEnable(boolean boldEnable) {
        Log.d("AAAAAAAAAAAAAA", "setBoldEnable: "+boldEnable);
        this.boldEnable = boldEnable;
    }

    public void setItalicEnable(boolean italicEnable) {
        this.italicEnable = italicEnable;
    }

    public void setUnderlineEnable(boolean underlineEnable) {
        this.underlineEnable = underlineEnable;
    }

    public boolean isBoldEnable() {
        return boldEnable;
    }

    public boolean isItalicEnable() {
        return italicEnable;
    }

    public boolean isUnderlineEnable() {
        return underlineEnable;
    }

    private boolean match(SpanInfo spanInfo){
        return spanInfo.isBold() == boldEnable && spanInfo.isItalic() == italicEnable && spanInfo.isUnderline() == underlineEnable;
    }

    private void setViewTextSpanToSpanInfo(SpanInfo spanInfo){
        spanInfo.setBold(boldEnable);
        spanInfo.setItalic(italicEnable);
        spanInfo.setUnderline(underlineEnable);
    }

    private ArrayList<SpanInfo> clone(ArrayList<SpanInfo> spans){
        ArrayList<SpanInfo> cloneList = new ArrayList<>();
        spans.forEach((item)->{
                cloneList.add(item.clone());
        });
        return cloneList;
    }
}
