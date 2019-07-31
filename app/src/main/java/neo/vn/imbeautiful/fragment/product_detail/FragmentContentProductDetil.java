package neo.vn.imbeautiful.fragment.product_detail;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseFragment;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.ObjLogin;
import neo.vn.imbeautiful.untils.SharedPrefs;

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
    String sUrl = "";
    @BindView(R.id.txt_affiliate)
    TextView txt_affiliate;
    @BindView(R.id.txt_share_affiliate)
    TextView txt_share_affiliate;
    @BindView(R.id.txt_coppy_link)
    TextView txt_coppy_link;
    String sData = "";

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
        initData();
        // initWebview();
        initEvent();
        return view;
    }

    private void initData() {
        ObjLogin sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
        if (App.mProduct.getLINK_AFFILIATE() != null) {
            String sName = "Link Affiliate: <font color='#1E90FF'>" + App.mProduct.getLINK_AFFILIATE() + "</font>";
            txt_affiliate.setText(Html.fromHtml(sName), TextView.BufferType.SPANNABLE);
            sUrl = App.mProduct.getLINK_AFFILIATE() + "?prc=" + App.mProduct.getCODE_PRODUCT() + "&uc=" + sUserName.getUSER_CODE();

        }
        if (App.mProduct.getCONTENT_WEB() != null) {
            sData = App.mProduct.getCONTENT_WEB();
            initWebview();
        }
    }

    private void initEvent() {
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_app(getActivity(), sUrl);
            }
        });
        txt_share_affiliate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_app(getActivity(), sUrl);
            }
        });
        txt_coppy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("imBeautiful", sUrl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Link Affiliate được coppy vào clipboard.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initWebview() {
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
