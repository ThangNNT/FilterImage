package com.example.imagefilter.article;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.imagefilter.databinding.ActivityPreviewBinding;

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
        String data = "<html>" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"+
                "        <style type='text/css'>\n" +
                "               img {max-width: 100%;height:initial;} div,p,span,a {max-width: 100%;}\n" +
                "            html {" +
                "               font-size: 16px;" +
                "             }" +
                "            body {" +
                "             font-size: 1rem;" +
                "            }" +
                "       </style>" +
                "       <body style=\"margin: 0; padding: 0\">"
                            + html +
                "       </body>" +
                "     </html>";
        mBinding.webview.setInitialScale(1);
        mBinding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mBinding.webview.getSettings().setUseWideViewPort(true);
        mBinding.webview.getSettings().setLoadWithOverviewMode(true);
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.getSettings().setAllowFileAccess(true);
        mBinding.webview.loadData(data, "text/html", "UTF-8");
    }

    public static Intent newIntent(Context context, String html){
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(PREVIEW_HTML, html);
        return intent;
    }
}