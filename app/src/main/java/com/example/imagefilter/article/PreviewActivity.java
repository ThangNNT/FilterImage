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
        String data = "<html>" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "        <style type='text/css'>\n" +
                "               img {max-width: 100%;height:initial;} div,p,span,a {max-width: 100%;}\n" +
                "            html {" +
                "               font-size: 16px;" +
                "             }" +
                "            body {" +
                "             font-size: 1rem;" +
                "            }" +
                "           .simple-divider {" +
                "               text-align: center;" +
                "               margin: 20px 0;" +
                "               }" +
                "           .simple-divider span {" +
                "                padding: 0 10px;" +
                "                font-size: 32px;" +
                "                color: gray;" +
                "                letter-spacing: 8px;" +
                "            }" +
                "            blockquote {" +
                "            font-style: italic;" +
                "            padding-left: 32px;" +
                "            margin: 20px 16px;" +
                "            position: relative;" +
                "             }" +
                "           ul {" +
                "                 margin-top: 10px;" +
                "           }" +
                "           ul li {" +
                "                 margin-bottom: 10px;" +
                "           }" +
                "           ul li:last-child {" +
                "                margin-bottom: 0;" +
                "            }" +
                "            blockquote::before {" +
                "            content: \"\\0022\";" +
                "            font-size: 3rem;" +
                "            color: rgba(153, 153, 153, 1);" +
                "            position: absolute;" +
                "            left: 10px;" +
                "            top: -10px;" +
                "                }" +
                "                         .code-block {" +
                "      background-color:rgba(221, 221, 221, 1);" +
                "            padding: 16px 8px 16px 8px;" +
                "            font-size:  "+codeBlockTextSize+"px;" +
                "            margin: 16px 16px 0 16px;" +
                "        }                                             " +
                "      .header-view {" +
                "        font-size: "+headerTextSize+"px;" +
                "            margin: 16px 16px 0 16px;" +
                "        }"+
                " .quote-view {" +
                "        font-size: "+quoteTextSize+"px;" +
                "        }"+
                " .text-view {\n" +
                "        margin: 16px 16px 0 16px;" +
                "            font-size: "+textSize+"px;" +
                "        }"+
                " .list-item-view{" +
                "        font-size: "+listItemTextSize+"px;" +
                "        }"+
                ".image-view {" +
                "        max-width: 100%; " +
                "            height: auto;" +
                "        }"+
                " .image-title {" +
                "        text-align:center;" +
                "            font-size: "+imageTitleTextSize+"px;" +
                "        }"+
                "       </style>" +
                "       <body style=\"margin: 0; padding: 0; white-space: normal; word-wrap: break-word\">"
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

    private int getTextSize(@DimenRes int dimenRes){
        return (int) Utils.pxToSp(this, getResources().getDimensionPixelSize(dimenRes));
    }
}