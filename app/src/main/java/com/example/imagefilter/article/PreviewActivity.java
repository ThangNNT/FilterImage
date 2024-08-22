package com.example.imagefilter.article;

import androidx.annotation.DimenRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.imagefilter.R;
import com.example.imagefilter.article.utils.Utils;
import com.example.imagefilter.databinding.ActivityPreviewBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PreviewActivity extends AppCompatActivity {
    private static final String PREVIEW_HTML = "PREVIEW_HTML";

    private ActivityPreviewBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setUp();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUp(){
        String html = getIntent().getStringExtra(PREVIEW_HTML);
        if (html == null) return;
        int codeBlockTextSize = getTextSize(R.dimen.code_block_text_size);
        int headerTextSize = getTextSize(R.dimen.header_text_size);
        int quoteTextSize = getTextSize(R.dimen.quote_text_size);
        int textSize = getTextSize(R.dimen.text_attachment_size);
        int listItemTextSize = getTextSize(R.dimen.ul_item_text_size);
        int imageTitleTextSize = getTextSize(R.dimen.image_title_text_size);

        int dividerMarginTop = getSize(R.dimen.divider_margin_top);
        int dividerMarginBottom = getSize(R.dimen.divider_margin_bottom);


        int textViewMarginTop = getSize(R.dimen.text_view_margin_top);
        int textViewMarginHorizontal = getSize(R.dimen.text_view_margin_horizontal);

        int headerViewMarginHorizontal = getSize(R.dimen.header_view_margin_horizontal);
        int headerViewMarginBottom = getSize(R.dimen.header_view_margin_bottom);

        int codeBlockMarginHorizontal = getSize(R.dimen.code_block_margin_horizontal);
        int codeBlockMarginTop = getSize(R.dimen.code_block_margin_top);
        int codeBlockPaddingHorizontal = getSize(R.dimen.code_block_padding_horizontal);
        int codeBlockPaddingVertical = getSize(R.dimen.code_block_padding_vertical);

        int quoteMarginTop = getSize(R.dimen.quote_margin_top);
        int quoteMarginHorizontal = getSize(R.dimen.quote_margin_horizontal);

        int imageMarginTop = getSize(R.dimen.image_margin_top);
        int imageTitleMarginTop = getSize(R.dimen.image_title_margin_top);
        int imageTitleMarginHorizontal = getSize(R.dimen.image_title_margin_horizontal);

        int ulItemMarginTop = getSize(R.dimen.ul_item_margin_top);
        int ulItemMarginHorizontal = getSize(R.dimen.ul_item_margin_horizontal);

        String data = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <style type=\"text/css\">" +
                "        img {" +
                "            max-width: 100%;" +
                "            height: initial;" +
                "        }" +
                "        div, p, span, a {" +
                "            max-width: 100%;" +
                "        }" +
                "        html {" +
                "            font-size: 16px;" +
                "        }" +
                "        body {" +
                "            font-size: 1rem;" +
                "        }" +
                "        .simple-divider {" +
                "            text-align: center;" +
                "            margin: " + dividerMarginTop + "px 0px "+dividerMarginBottom+"px  0px;" +
                "        }" +
                "        .simple-divider span {" +
                "            padding: 0 10px;" +
                "            font-size: 32px;" +
                "            color: gray;" +
                "            letter-spacing: 8px;" +
                "        }" +
                "        blockquote {" +
                "            font-style: italic;" +
                "            padding-left: 32px;" +
                "            margin: 20px 16px;" +
                "            position: relative;" +
                "        }" +
                "        blockquote::before {" +
                "            content: \"\\0022\";" +
                "            font-size: 3rem;" +
                "            color: rgba(153, 153, 153, 1);" +
                "            position: absolute;" +
                "            left: 10px;" +
                "            top: -10px;" +

                "        }" +
                "        .code-block {" +
                "            background-color:rgba(221, 221, 221, 1);" +
                "            padding: "+codeBlockPaddingVertical+"px "+codeBlockPaddingHorizontal+"px;" +
                "            font-size:  " + codeBlockTextSize + "px;" +
                "            margin: " + codeBlockMarginTop + "px " + codeBlockMarginHorizontal + "px 0 " + codeBlockMarginHorizontal + "px;" +
                "        }" +
                "        .header-view {" +
                "           font-size: "+headerTextSize+"px;" +
                "            margin: 0px " + headerViewMarginHorizontal + "px " + headerViewMarginBottom + "px " + headerViewMarginHorizontal + "px;" +
                "        }" +
                "        .quote-view {" +
                "            font-size: "+quoteTextSize+"px;" +
                "            font-style: italic;" +
                "            padding-left: 32px;" +
                "            margin: " + quoteMarginTop + "px " + quoteMarginHorizontal + "px 0 " + quoteMarginHorizontal + "px;" +
                "            position: relative;" +
                "        }" +
                "        .quote-view::before {" +
                "            content: \"\\0022\";" +
                "            font-size: 3rem;" +
                "            color: rgba(153, 153, 153, 1);" +
                "            position: absolute;" +
                "            left: 10px;" +
                "            top: -10px;" +
                "        }" +
                "        .text-view {" +
                "            margin: " + textViewMarginTop + "px " + textViewMarginHorizontal + "px 0 " + textViewMarginHorizontal + "px;" +
                "            font-size: " + textSize + "px;" +
                "        }" +
                "        .list-item-view{" +
                "           font-size: "+listItemTextSize+"px;" +
                "            margin: " + ulItemMarginTop + "px " + ulItemMarginHorizontal + "px 0 " + ulItemMarginHorizontal + "px;" +
                "        }" +
                "        .image-view {" +
                "            margin: " + imageMarginTop + "px 0 0 0;" +
                "           max-width: 100%;" +
                "            height: auto;" +
                "        }" +
                "        .image-title {" +
                "            margin: " + imageTitleMarginTop + "px " + imageTitleMarginHorizontal + "px 0 " + imageTitleMarginHorizontal + "px;" +
                "            text-align:center;" +
                "            font-size: "+imageTitleTextSize+"px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body style=\"margin: 16px 0; padding: 0; white-space: normal; word-wrap: break-word\">" +
                         html +
                "</body>" +
                "</html>";
        mBinding.webview.setInitialScale(1);
        mBinding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mBinding.webview.getSettings().setUseWideViewPort(true);
        mBinding.webview.getSettings().setLoadWithOverviewMode(true);
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.getSettings().setAllowFileAccess(true);
        mBinding.webview.loadData(data, "text/html", "UTF-8");
        mBinding.btnTest.setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra(ArticleActivity.HTML, data);
            startActivity(intent);
        });
    }

    public static Intent newIntent(Context context, String html){
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(PREVIEW_HTML, html);
        return intent;
    }

    private int getTextSize(@DimenRes int dimenRes){
        return (int) Utils.pxToSp(this, getResources().getDimensionPixelSize(dimenRes));
    }

    private int getSize(@DimenRes int dimenRes){
        return (int) Utils.pxToDp(this, getResources().getDimensionPixelSize(dimenRes));
    }
}