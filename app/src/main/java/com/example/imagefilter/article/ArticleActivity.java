package com.example.imagefilter.article;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.imagefilter.article.dialog.AddAttachmentImageDialog;
import com.example.imagefilter.article.dialog.AddAttachmentLinkDialog;
import com.example.imagefilter.article.base.Attachable;
import com.example.imagefilter.article.view.AttachmentImage;
import com.example.imagefilter.article.view.CodeBlockView;
import com.example.imagefilter.article.view.DividerView;
import com.example.imagefilter.article.base.Focusable;
import com.example.imagefilter.article.view.HeaderView;
import com.example.imagefilter.article.base.Linkable;
import com.example.imagefilter.article.view.QuoteView;
import com.example.imagefilter.article.view.TextAttachmentView;
import com.example.imagefilter.article.view.UnorderedListView;
import com.example.imagefilter.databinding.ActivityArticleBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleActivity extends AppCompatActivity {

    static final String HTML = "HTML";

    private ActivityArticleBinding binding;
    private View currentFocusChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUp();
        initData();
    }

    private void setUp() {
        binding.layoutArticle.addView(new HeaderView((this)));
        binding.tvPreview.setOnClickListener((v) -> {
            startActivity(PreviewActivity.newIntent(this, getHtml()));
        });
        // Text
        binding.layoutAttachment.tvText.setOnClickListener(v -> {
            addView(createTextAttachmentView());
        });
        // image
        binding.layoutAttachment.ivImage.setOnClickListener((v) -> {
            showAddAttachmentImageDialog();
        });
        // code block
        binding.layoutAttachment.ivCodeBlock.setOnClickListener((v)->{
            addView(createCodeBlockView());
        });
        // divider
        binding.layoutAttachment.ivDivider.setOnClickListener((v)->{
            addView(createDividerView());
        });
        // quote
        binding.layoutAttachment.ivQuote.setOnClickListener((v) -> {
            addView(createQuoteView());
        });
        // ul list
        binding.layoutAttachment.ivList.setOnClickListener((v) -> {
            addUnorderedListView(null, -1);
        });
        // link
        binding.layoutAttachment.ivLink.setOnClickListener((v)-> {
            if (currentFocusChild instanceof Linkable){
                Linkable linkable = (Linkable) currentFocusChild;
                AddAttachmentLinkDialog dialog = new AddAttachmentLinkDialog(this);
                dialog.setListener((data)->{
                    linkable.setLink(data.getText(), data.getUrl());
                });
                dialog.show();
            }
        });
    }

    /**
     * remove a view and focus on previous view
     */
    private void removeView(View view){
        if (currentFocusChild == null) return;
        ViewGroup parent = binding.layoutArticle;
        int currentSelectedIndex = parent.indexOfChild(currentFocusChild);
        if (currentSelectedIndex >=1){
            View prevView = parent.getChildAt(currentSelectedIndex-1);
            if (prevView instanceof Focusable){
                ((Focusable) prevView).focus();
            }
        }
        parent.removeView(view);
    }

    /**
     * Add a view after the item that is focused.
     */
    private void addView(View view){
        ViewGroup parent = binding.layoutArticle;
        if (currentFocusChild == null) {
            parent.addView(view);
            if (view instanceof Focusable){
                ((Focusable) view).focus();
            }
            return;
        };
        int currentSelectedIndex = parent.indexOfChild(currentFocusChild);
        if (currentSelectedIndex != -1 && currentSelectedIndex < parent.getChildCount() - 1) {
            parent.addView(view, currentSelectedIndex + 1);
        } else parent.addView(view);
        if (view instanceof Focusable){
            ((Focusable) view).focus();
        }
    }


    private String getHtml() {
        int childCount = binding.layoutArticle.getChildCount();
        StringBuilder htmlResult = new StringBuilder();
        for (int i = 0; i < childCount; i++) {
            View child = binding.layoutArticle.getChildAt(i);
            if (child instanceof UnorderedListView) {
                View prevView = null;
                int prevIndex = i - 1;
                if (prevIndex >= 0) prevView = binding.layoutArticle.getChildAt(prevIndex);
                // If it's the first list item, you need to add a UL tag at the beginning.
                if (!(prevView instanceof UnorderedListView)) {
                    htmlResult.append("<ul>");
                }

                String html = ((Attachable) child).getHtml();
                htmlResult.append(html);

                View nextView = null;
                int nextIndex = i + 1;
                if (nextIndex < childCount) nextView = binding.layoutArticle.getChildAt(nextIndex);
                // If it's the last list item, you need to add a UL tag at the beginning.
                if (!(nextView instanceof  UnorderedListView)){
                    htmlResult.append("</ul>");
                }

            } else if (child instanceof Attachable) {
                String html = ((Attachable) child).getHtml();
                htmlResult.append(html);
            }
        }
        return htmlResult.toString();
    }

    private void addUnorderedListView(@Nullable String initText, int index){
        UnorderedListView unorderedListView = new UnorderedListView(this);
        unorderedListView.setOnRemoveClickListener(this::removeView);
        if (initText!=null){
            unorderedListView.setText(initText);
        }
        unorderedListView.setOnAddUnorderedListViewListener((view, textAfter) -> {
            int indexOfCurrentChild = binding.layoutArticle.indexOfChild(view);
            if (indexOfCurrentChild != -1){
                addUnorderedListView(textAfter, indexOfCurrentChild+1);
            }
        });
        unorderedListView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) currentFocusChild = v;
            binding.layoutAttachment.ivLink.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        });
        if (index != -1) {
            binding.layoutArticle.addView(unorderedListView, index);
            unorderedListView.focus();
        } else
            addView(unorderedListView);
    }

    private void showAddAttachmentImageDialog(){
        AddAttachmentImageDialog dialog = new AddAttachmentImageDialog(this);
        dialog.setListener((data)->{
            addView(createAttachmentImage(data.getUrl()));
        });
        dialog.show();
    }

    private void showEditAttachmentImageDialog(AttachmentImage attachmentImage){
        AddAttachmentImageDialog dialog = new AddAttachmentImageDialog(this);
        dialog.setListener((data)-> attachmentImage.setImageUrl(data.getUrl()));
        dialog.show();
    }

    private void initData(){
        String html = getIntent().getStringExtra(HTML);
        if (html == null) return;
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("."+ClassDefine.ATTACHABLE_CLASS);
        for (Element element : elements) {
            if(element.hasClass(ClassDefine.HEADER_VIEW)){
                int childCount = binding.layoutArticle.getChildCount();
                if (childCount>0){
                    View firstView = binding.layoutArticle.getChildAt(0);
                    String header = element.text();
                    if (firstView instanceof HeaderView){
                        ((HeaderView) firstView).setText(header);
                    }
                }
            } else if ((element.hasClass(ClassDefine.TEXT_ATTACHMENT))){
                TextAttachmentView textAttachmentView = createTextAttachmentView();
                String text = element.html();
                Spanned spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
                textAttachmentView.setSpanned(spanned);
                Log.d("AAAAAAAAAAAAAAAA", "TEXT_ATTACHMENT:" +text);
                binding.layoutArticle.addView(textAttachmentView);
            } else if (element.hasClass(ClassDefine.DIVIDER)) {
                binding.layoutArticle.addView(createDividerView());
            }
            else if (element.hasClass(ClassDefine.CODE_BLOCK_VIEW)){
                CodeBlockView codeBlockView = createCodeBlockView();
                String text = element.select("p").text();
                codeBlockView.setText(text);
                binding.layoutArticle.addView(codeBlockView);
            }
            else if (element.hasClass(ClassDefine.QUOTE_VIEW)){
                QuoteView quoteView = createQuoteView();
                String text = element.html();
                Spanned spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
                quoteView.setSpanned(spanned);
                binding.layoutArticle.addView(quoteView);
            } else if (element.hasClass(ClassDefine.LIST_ITEM)){
                UnorderedListView unorderedListView = createUnorderedListView();
                String text = element.html();
                Log.d("AAAAAAAAAAAAAAAA", "ul:" +text);
                Spanned spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
                unorderedListView.setSpanned(spanned);
                binding.layoutArticle.addView(unorderedListView);
            }
            else if (element.hasClass(ClassDefine.IMAGE)){
                String url = element.select("img").attr("src");
                AttachmentImage attachmentImage = createAttachmentImage(url);
                binding.layoutArticle.addView(attachmentImage);
            }
            else if (element.hasClass(ClassDefine.IMAGE_TITLE)){
                int lastIndex = binding.layoutArticle.getChildCount() - 1;
                View lastView = binding.layoutArticle.getChildAt(lastIndex);
                Log.d("AAAAAAAAAAAAAAAA", "ul:" +element.text());
                if (lastView instanceof AttachmentImage){
                    ((AttachmentImage) lastView).setTitle(element.text());
                }
            }
        }
    }

    private TextAttachmentView createTextAttachmentView(){
        TextAttachmentView editText = new TextAttachmentView(this);
        editText.setOnFocusChangeListener(((view, hasFocus) -> {;
            if (hasFocus){
                currentFocusChild =  view;
                binding.layoutAttachment.ivLink.setVisibility(View.VISIBLE);
            }
            else {
                binding.layoutAttachment.ivLink.setVisibility(View.GONE);
            }
        }));
        editText.setOnRemoveClickListener(this::removeView);
        return editText;
    }

    private DividerView createDividerView(){
        DividerView dividerView = new DividerView((this));
        dividerView.setOnRemoveClickListener(this::removeView);
        dividerView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) currentFocusChild = view;
        });
        return dividerView;
    }
    private CodeBlockView createCodeBlockView(){
        CodeBlockView codeBlockView = new CodeBlockView(this);
        codeBlockView.setOnRemoveClickListener(this::removeView);
        codeBlockView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) currentFocusChild = view;
        });
        return codeBlockView;
    }

    private QuoteView createQuoteView() {
        QuoteView quoteView = new QuoteView(this);
        quoteView.setOnRemoveClickListener(this::removeView);
        quoteView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) currentFocusChild = view;
            binding.layoutAttachment.ivLink.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        });
        return quoteView;
    }

    private UnorderedListView createUnorderedListView(){
        UnorderedListView unorderedListView = new UnorderedListView(this);
        unorderedListView.setOnRemoveClickListener(this::removeView);
        unorderedListView.setOnAddUnorderedListViewListener((view, textAfter) -> {
            int indexOfCurrentChild = binding.layoutArticle.indexOfChild(view);
            if (indexOfCurrentChild != -1){
                addUnorderedListView(textAfter, indexOfCurrentChild+1);
            }
        });
        unorderedListView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) currentFocusChild = v;
            binding.layoutAttachment.ivLink.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        });
        return unorderedListView;
    }

    private AttachmentImage createAttachmentImage(String url){
        AttachmentImage attachmentImage = new AttachmentImage((this));
        attachmentImage.setOnRemoveClickListener(this::removeView);
        attachmentImage.setOnEditClickListener((v) -> {
            if (v instanceof AttachmentImage) {
                showEditAttachmentImageDialog((AttachmentImage) v);
            }
        });
        attachmentImage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                currentFocusChild = v;
            }
        });
        attachmentImage.setImageUrl(url);
        return attachmentImage;
    }
}