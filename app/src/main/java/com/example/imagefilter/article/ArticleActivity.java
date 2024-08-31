package com.example.imagefilter.article;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.imagefilter.R;
import com.example.imagefilter.article.base.Switchable;
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
import com.example.imagefilter.article.view.SubtitleView;
import com.example.imagefilter.article.view.TextAttachmentView;
import com.example.imagefilter.article.view.UnorderedListView;
import com.example.imagefilter.databinding.ActivityArticleBinding;
import com.example.imagefilter.databinding.LayoutSwitchMenuBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class ArticleActivity extends AppCompatActivity {

    static final String HTML = "HTML";

    private ActivityArticleBinding binding;
    private View currentFocusChild;

    private HashMap<Integer, View> switchMenuViewMap = new HashMap<>();

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
        // subtitle
        binding.layoutAttachment.ivSubTitle.setOnClickListener(v -> {
            addView(createSubtitleView());
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
            addView(createUnorderedListView());
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
        }
        int currentSelectedIndex = parent.indexOfChild(currentFocusChild);
        if (currentSelectedIndex != -1 && currentSelectedIndex < parent.getChildCount() - 1) {
            parent.addView(view, currentSelectedIndex + 1);
        } else parent.addView(view);
        if (view instanceof Focusable) {
            // ensure view added into parent before request focus for calculating view's coordinate purpose
            view.post(() -> ((Focusable) view).focus());
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
                if (lastView instanceof AttachmentImage){
                    ((AttachmentImage) lastView).setTitle(element.text());
                }
            } else if (element.hasClass(ClassDefine.SUBTITLE_ATTACHMENT)) {
                SubtitleView subtitleView = createSubtitleView();
                String text = element.html();
                Spanned spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
                subtitleView.setSpanned(spanned);
                binding.layoutArticle.addView(subtitleView);
            }
        }
    }

    private TextAttachmentView createTextAttachmentView(){
        TextAttachmentView editText = new TextAttachmentView(this);
        editText.setId(View.generateViewId());
        editText.setOnFocusChangeListener(((view, hasFocus) -> {;
            if (hasFocus){
                currentFocusChild =  view;
                binding.layoutAttachment.ivLink.setVisibility(View.VISIBLE);
                showSwitchViewMenuView(view);
            }
            else {
                binding.layoutAttachment.ivLink.setVisibility(View.GONE);
                removeSwitchViewMenuView(view);
            }
        }));
        editText.setOnRemoveClickListener(this::removeView);
        return editText;
    }

    private SubtitleView createSubtitleView(){
        SubtitleView newVew = new SubtitleView(this);
        newVew.setId(View.generateViewId());
        newVew.setOnFocusChangeListener(((view, hasFocus) -> {;
            if (hasFocus){
                currentFocusChild =  view;
                binding.layoutAttachment.ivLink.setVisibility(View.VISIBLE);
                showSwitchViewMenuView(view);
            }
            else {
                binding.layoutAttachment.ivLink.setVisibility(View.GONE);
                removeSwitchViewMenuView(view);
            }
        }));
        newVew.setOnRemoveClickListener(this::removeView);
        return newVew;
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
        codeBlockView.setId(View.generateViewId());
        codeBlockView.setOnRemoveClickListener(this::removeView);
        codeBlockView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                currentFocusChild = view;
                showSwitchViewMenuView(view);
            } else {
                removeSwitchViewMenuView(view);
            }
        });
        return codeBlockView;
    }

    private QuoteView createQuoteView() {
        QuoteView quoteView = new QuoteView(this);
        quoteView.setId(View.generateViewId());
        quoteView.setOnRemoveClickListener(this::removeView);
        quoteView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                currentFocusChild = view;
                showSwitchViewMenuView(view);
            } else {
                removeSwitchViewMenuView(view);
            }
            binding.layoutAttachment.ivLink.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        });
        return quoteView;
    }

    private UnorderedListView createUnorderedListView(){
        UnorderedListView unorderedListView = new UnorderedListView(this);
        unorderedListView.setId(View.generateViewId());
        unorderedListView.setOnRemoveClickListener(this::removeView);
        unorderedListView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                currentFocusChild = v;
                showSwitchViewMenuView(v);
            } else {
                removeSwitchViewMenuView(v);
            }
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

    /**
     * @param anchorView ensure anchorView has its it to use this function,
     * if anchorView has no if,  removeSwitchViewMenuView(View anchorView) will not work
     */
    private void showSwitchViewMenuView(View anchorView) {
        // Inflate the floating view layout
        LayoutSwitchMenuBinding layoutSwitchMenuBinding = LayoutSwitchMenuBinding.inflate(getLayoutInflater(), binding.getRoot(), false);
        layoutSwitchMenuBinding.ivCodeBlock.setOnClickListener(v -> switchToCodeBlock(anchorView));
        layoutSwitchMenuBinding.ivQuote.setOnClickListener(v -> switchToQuoteView(anchorView));
        layoutSwitchMenuBinding.tvText.setOnClickListener(v -> switchToTextAttachmentView(anchorView));
        layoutSwitchMenuBinding.ivList.setOnClickListener(v -> switchToUnorderedListView(anchorView));
        layoutSwitchMenuBinding.ivSubTitle.setOnClickListener(v -> switchToSubtitleView(anchorView));
        int offset = getResources().getDimensionPixelSize(R.dimen.switch_menu_offset);
        if (anchorView instanceof CodeBlockView) {
            // because code block an ul has different layout arrangement
            offset = 0;
        }
        // Set position of the floating view
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        layoutSwitchMenuBinding.getRoot().setY(location[1] - anchorView.getHeight() - offset);  // Adjust the position as needed
        switchMenuViewMap.put(anchorView.getId(), layoutSwitchMenuBinding.getRoot());
        // Add the floating view to the root layout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END;
        binding.getRoot().addView(layoutSwitchMenuBinding.getRoot(), params);
    }

    private void removeSwitchViewMenuView(View anchorView) {
        View view = switchMenuViewMap.get(anchorView.getId());
        if (view != null) {
            binding.getRoot().removeView(view);
        }
        switchMenuViewMap.remove(anchorView.getId());
    }

    private void switchToCodeBlock(View view){
        if (view instanceof CodeBlockView) return;
        CodeBlockView codeBlockView = createCodeBlockView();
        if (view instanceof Switchable){
            Spanned spanned = ((Switchable) view).getSpanned();
            int selectionPos = ((Switchable) view).getSelectionStart();
            codeBlockView.setText(spanned.toString());
            codeBlockView.setSelectionStart(selectionPos);
        }
        replace(view, codeBlockView);
    }

    private void switchToQuoteView(View view){
        if (view instanceof QuoteView) return;
        QuoteView quoteView = createQuoteView();
        if (view instanceof Switchable){
            Spanned spanned = ((Switchable) view).getSpanned();
            int selectionPos = ((Switchable) view).getSelectionStart();
            quoteView.setSpanned(spanned);
            quoteView.setSelectionStart(selectionPos);
        }
        replace(view, quoteView);
    }

    private void switchToTextAttachmentView(View view) {
        if (view instanceof TextAttachmentView) return;
        TextAttachmentView textAttachmentView = createTextAttachmentView();
        if (view instanceof Switchable) {
            Spanned spanned = ((Switchable) view).getSpanned();
            int selectionPos = ((Switchable) view).getSelectionStart();
            textAttachmentView.setSpanned(spanned);
            textAttachmentView.setSelectionStart(selectionPos);
        }
        replace(view, textAttachmentView);
    }

    private void switchToUnorderedListView(View view) {
        if (view instanceof UnorderedListView) return;
        UnorderedListView unorderedListView = createUnorderedListView();
        if (view instanceof Switchable) {
            Spanned spanned = ((Switchable) view).getSpanned();
            int selectionPos = ((Switchable) view).getSelectionStart();
            unorderedListView.setSpanned(spanned);
            unorderedListView.setSelectionStart(selectionPos);
        }
        replace(view, unorderedListView);
    }

    private void switchToSubtitleView(View view) {
        if (view instanceof SubtitleView) return;
        SubtitleView subtitleView = createSubtitleView();
        if (view instanceof Switchable) {
            Spanned spanned = ((Switchable) view).getSpanned();
            int selectionPos = ((Switchable) view).getSelectionStart();
            subtitleView.setSpanned(spanned);
            subtitleView.setSelectionStart(selectionPos);
        }
        replace(view, subtitleView);
    }

    protected void replace(View oldView, View newView) {
        int viewIndex = binding.layoutArticle.indexOfChild(oldView);
        if (viewIndex == -1) return;
        binding.layoutArticle.removeView(oldView);
        binding.layoutArticle.addView(newView, viewIndex);
        if (newView instanceof Focusable) {
            newView.post(() -> ((Focusable) newView).focus());
        }
    }

    @Override
    protected void onDestroy() {
        switchMenuViewMap.clear();
        super.onDestroy();
    }
}