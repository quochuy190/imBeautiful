package neo.vn.imbeautiful.activity.tintuc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.InfomationObj;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 18-July-2019
 * Time: 11:24
 * Version: 1.0
 */
public class ActivityDetailNews extends BaseActivity {
    @BindView(R.id.txt_tieude)
    TextView txt_tieude;
    @BindView(R.id.webview_news)
    WebView webview_news;
    @BindView(R.id.img_tintuc)
    ImageView img_tintuc;

    private InfomationObj mInfo;

    @Override
    public int setContentViewId() {
        return R.layout.activity_detail_infomation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppbar();
        initData();
    }

    TextView txt_title;

    private void initAppbar() {
        ImageView img_back = findViewById(R.id.img_back);
        txt_title = findViewById(R.id.txt_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    String sTitle;

    private void initData() {
        sTitle = getIntent().getStringExtra(Constants.KEY_SEND_NEWS_TITLE);
        if (sTitle != null) {
            txt_title.setText(sTitle);
        } else {

        }
        mInfo = (InfomationObj) getIntent().getSerializableExtra(Constants.KEY_SEND_NEWS_OBJ);
        if (mInfo != null) {
            if (mInfo.getTITLE() != null)
                txt_tieude.setText(mInfo.getTITLE().trim());
            if (mInfo.getCONTENT() != null)
                initWebview(mInfo.getCONTENT(), webview_news);
            // txt_content.setText(Html.fromHtml(mInfo.getCONTENT()));
            if (mInfo.getIMAGE_COVER() != null) {
                Glide.with(this).load(mInfo.getIMAGE_COVER()).asBitmap()
                        .placeholder(R.drawable.img_defaul)
                        .into(new BitmapImageViewTarget(img_tintuc) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                //   progressBar.setVisibility(View.GONE);
                            }
                        });
            } else {
                Glide.with(this).load(R.drawable.img_defaul).into(img_tintuc);
            }

        }

    }

    public static void initWebview(String sData, WebView webView) {
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings();
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setDefaultFontSize(18);
        webSettings.setTextZoom((int) (webSettings.getTextZoom() * 2.5));
        //  webView.loadUrl(sUrl);
        webView.loadDataWithBaseURL("", sData,
                "text/html", "UTF-8", "");
    }
}
