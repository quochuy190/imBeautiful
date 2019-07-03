package neo.vn.imbeautiful.fragment.product_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseFragment;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 22-April-2019
 * Time: 10:30
 * Version: 1.0
 */
public class FragmentContentProductDetil extends BaseFragment {
    private static final String TAG = "FragmentContentProductD";
    public static FragmentContentProductDetil fragment;
    @BindView(R.id.webview_detail_product)
    WebView webView;
    @BindView(R.id.btn_share_content)
    ImageView img_share;

    public static FragmentContentProductDetil getInstance() {
        if (fragment == null) {
            synchronized (FragmentContentProductDetil.class) {
                if (fragment == null)
                    fragment = new FragmentContentProductDetil();
            }
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_product_detail, container, false);
        ButterKnife.bind(this, view);
        Log.e(TAG, "onCreateView: FragmentContentProductD");
        initWebview();
        initData();
        initEvent();
        return view;
    }

    private void initData() {

    }

    private void initEvent() {
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_app(getActivity(),
                        "https://sandotot.com/tam-trang-sui-bot-toan-than-gluta-white-bubble-detox-body-gluta-white-1-1-225.html");
            }
        });
    }

    public void initWebview() {
        webView.setInitialScale(1);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("https://sandotot.com/kem-sach-mun-tan-goc-gluta-white-miracle-clean-acnes-1-1-2.html?aff=606466");
    }

    public static void share_app(Activity activity, String content) {
        final String my_package_name = "neo.vn.test365children";  // <- HERE YOUR PACKAGE NAME!!
        String url = "";
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "I'm Beautiful");
            i.putExtra(Intent.EXTRA_TEXT, content);
            activity.startActivity(Intent.createChooser(i, "Home365"));
        } catch (Exception e) {
            e.toString();
        }
    }
}
