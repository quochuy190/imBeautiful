package neo.vn.imbeautiful.activity.products;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterImaViewPager;
import neo.vn.imbeautiful.adapter.AdapterViewpager;
import neo.vn.imbeautiful.adapter.ImageAdapter;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.TaskCompleted;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.fragment.product_detail.FragmenFacebookProductDetail;
import neo.vn.imbeautiful.fragment.product_detail.FragmentContentProductDetil;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.respon_api.ObjLisCart;
import neo.vn.imbeautiful.models.respon_api.ResponGetCat;
import neo.vn.imbeautiful.untils.SharedPrefs;
import neo.vn.imbeautiful.untils.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 10-May-2019
 * Time: 17:00
 * Version: 1.0
 */
public class ActivityProductDetail extends BaseActivity implements InterfaceProperties.View, TaskCompleted {
    private static final String TAG = "ActivityProductDetail";
    @BindView(R.id.view_product_detail)
    ViewPager view_pager;
    AdapterViewpager adapter;
    @BindView(R.id.ll_tab_layout_content)
    ConstraintLayout ll_tab_layout_content;
    @BindView(R.id.ll_tab_layout_face)
    ConstraintLayout ll_tab_layout_face;
    @BindView(R.id.txt_name_product_detail)
    TextView txt_name_product_detail;
    @BindView(R.id.tx_price_product_detail)
    TextView tx_price_product_detail;
    @BindView(R.id.txt_add_cart)
    TextView txt_add_cart;
    @BindView(R.id.scroll_product_detail)
    NestedScrollView scroll_product_detail;
    TabLayout tabLayout;
    private Products mProduct;
    private List<String> mLisImage;
    private List<Products> mList;
    private ObjLisCart objLisCart;
    private PresenterProperties mPresenterProperties;

    @Override
    public int setContentViewId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager(view_pager);
        ll_tab_layout_content.setSelected(true);
        mPresenterProperties = new PresenterProperties(this);
        // DownloadImageFromPath("http://developer.android.com/images/activity_lifecycle.png");
        // new AsyncDownloadFile(ActivityProductDetail.this).execute("http://developer.android.com/images/activity_lifecycle.png");

        initAppbar();
        initData();
        initEvent();
        loadFragmentContentProductDetil();
    }

    private void initViewpager() {
        tabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this, mLisImage);
        AdapterImaViewPager customPagerAdapter = new AdapterImaViewPager(this, mLisImage);
        viewPager.setAdapter(customPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
        txt_title.setText("Chi tiết sản phẩm");
    }

    private void initData() {
        mList = new ArrayList<>();
        String sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        objLisCart = SharedPrefs.getInstance().get(Constants.KEY_SAVE_LIST_CART, ObjLisCart.class);
        if (objLisCart != null && objLisCart.getmList() != null && objLisCart.getmList().size() > 0) {
            mList.clear();
            mList.addAll(objLisCart.getmList());
        }
        mProduct = (Products) getIntent().getSerializableExtra(Constants.KEY_SEND_OBJ_PRODUCTS);
        if (mProduct.getMEDIA_FB() != null && mProduct.getMEDIA_FB().length() > 0) {
            String[] arrayImage = mProduct.getMEDIA_FB().split("\\|\\|");
            if (arrayImage.length > 0) {
                for (String sLink : arrayImage) {
                    if (sLink != null && sLink.length() > 0)
                        load_image(sLink);
                }
            }
        }
      /*  if (mProduct.getsUrlImage() != null)
            load_image(mProduct.getsUrlImage());
        if (mProduct.getIMG1() != null)
            load_image(mProduct.getIMG1());
        if (mProduct.getIMG2() != null)
            load_image(mProduct.getIMG2());
        if (mProduct.getIMG3() != null)
            load_image(mProduct.getIMG3());*/
        if (mProduct.getVIDEO_FB() != null) {
            startDownload(mProduct.getVIDEO_FB());
        }
        App.mProduct = mProduct;
        if (mProduct != null) {
           /* showDialogLoading();
            mPresenterProperties.api_get_properties(sUserName, mProduct.getID_PRODUCT_PROPERTIES());*/
            mLisImage = new ArrayList<>();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("imBeautiful", mProduct.getCONTENT_FB());
            clipboard.setPrimaryClip(clip);
            boolean isClick = false;
            for (Products obj : mList) {
                if (obj.getCODE_PRODUCT().equals(mProduct.getCODE_PRODUCT())) {
                    isClick = true;
                }
            }
            if (isClick) {
                btn_add_selected();
            }
            if (mProduct.getsUrlImage() != null) {
                mLisImage.add(mProduct.getsUrlImage());
            }
            if (mProduct.getIMG1() != null) {
                mLisImage.add(mProduct.getIMG1());
            }
            if (mProduct.getIMG2() != null) {
                mLisImage.add(mProduct.getIMG2());
            }
            if (mProduct.getIMG3() != null) {
                mLisImage.add(mProduct.getIMG3());
            }
            if (mProduct.getsName() != null && mProduct.getsName().length() > 0) {
                txt_name_product_detail.setText(mProduct.getsName());
            } else
                txt_name_product_detail.setText(".....");
            if (mProduct.getsPrice() != null && mProduct.getsPrice().length() > 0) {
                tx_price_product_detail.setText(StringUtil.conventMonney(mProduct.getsPrice()));
            } else
                tx_price_product_detail.setText(".....");
            if (mLisImage.size() == 0) {
                mLisImage.add("abc");
            }
            initViewpager();
        }
    }

    private void btn_add_selected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txt_add_cart.setBackground(getResources().getDrawable(R.drawable.spr_btn_add_product_selected));
            txt_add_cart.setText("Đã thêm vào giỏ hàng");
        }
        txt_add_cart.setEnabled(false);
    }

    private void initEvent() {
        txt_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAddPro = false;
                if (mList != null && mList.size() > 0) {
                    for (Products obj : mList) {
                        if (mProduct.getCODE_PRODUCT().equals(obj.getCODE_PRODUCT())) {
                            isAddPro = true;
                            return;
                        }
                    }
                    if (!isAddPro) {
                        mList.add(mProduct);
                        btn_add_selected();
                    }
                } else {
                    btn_add_selected();
                    mList.add(mProduct);
                }
                ObjLisCart obj = new ObjLisCart(mList);
                SharedPrefs.getInstance().put(Constants.KEY_SAVE_LIST_CART, obj);
            }
        });
        ll_tab_layout_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_tab_layout_face.setSelected(true);
                ll_tab_layout_content.setSelected(false);
                loadFragmentFracebookProductDetil();
                scroll_product_detail.stopNestedScroll();
                // scroll_product_detail.scrollTo(0, (int)ll_tab_layout_content.getY());
            }
        });
        ll_tab_layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragmentContentProductDetil();
                scroll_product_detail.stopNestedScroll();
                // scroll_product_detail.scrollTo(0, (int)ll_tab_layout_content.getY());
                ll_tab_layout_face.setSelected(false);
                ll_tab_layout_content.setSelected(true);


            }
        });


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new AdapterViewpager(getSupportFragmentManager());
        adapter.addFragment(FragmentContentProductDetil.getInstance(), "Báo cáo");
        adapter.addFragment(FragmenFacebookProductDetail.getInstance(), "Cá nhân");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        //   setUpTablayout();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 0) {
                    ll_tab_layout_content.setSelected(true);
                    ll_tab_layout_face.setSelected(false);
                } else if (position == 1) {
                    ll_tab_layout_content.setSelected(false);
                    ll_tab_layout_face.setSelected(true);
                }
                // Check if this is the page you want.
            }
        });
    }

    Fragment fragmentCurrent;
    FragmentContentProductDetil fragmentHome;
    FragmenFacebookProductDetail fragmentFacebook;

    private void loadFragmentContentProductDetil() {
        fragmentHome = (FragmentContentProductDetil) getSupportFragmentManager().findFragmentByTag(FragmentContentProductDetil.class.getName());

        if (fragmentHome == null) {
            fragmentHome = FragmentContentProductDetil.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentHome.isAdded()) {
            transaction.add(R.id.frame_product_detail, fragmentHome, FragmentContentProductDetil.class.getName());
        } else {
            transaction.hide(fragmentCurrent);
            transaction.show(fragmentHome);
        }
        fragmentCurrent = fragmentHome;
        transaction.commit();
    }

    private void loadFragmentFracebookProductDetil() {
        fragmentFacebook = (FragmenFacebookProductDetail) getSupportFragmentManager().findFragmentByTag(FragmenFacebookProductDetail.class.getName());

        if (fragmentFacebook == null) {
            fragmentFacebook = FragmenFacebookProductDetail.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentFacebook.isAdded()) {
            transaction.add(R.id.frame_product_detail, fragmentFacebook, FragmenFacebookProductDetail.class.getName());
        } else {
            transaction.hide(fragmentCurrent);
            transaction.show(fragmentFacebook);
        }
        fragmentCurrent = fragmentFacebook;
        transaction.commit();
    }

    @Override
    public void show_error_api() {

    }

    @Override
    public void show_get_properties(ResponGetCat obj) {

    }

    public void DownloadImageFromPath(String path) {
        InputStream in = null;
        Bitmap bmp = null;
        // ImageView iv = (ImageView)findViewById(R.id.img1);
        int responseCode = -1;
        try {
            URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
                //iv.setImageBitmap(bmp);
            }

        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
    }

    @Override
    public void onTaskComplete(String result) {
        if (!result.equals("")) { //successfully downloaded
           /* ImageView imgView;
            imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageBitmap(BitmapFactory.decodeFile(result));*/
            Toast.makeText(this, "" + result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Fail to download image", Toast.LENGTH_LONG).show();
        }
    }

    public static List<Bitmap> mLisIma = new ArrayList<>();

    private void load_image(String sUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(sUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response.body().byteStream(); // Read the data from the stream
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                if (bmp != null)
                    mLisIma.add(bmp);
                Log.i(TAG, "onResponse: " + bmp);
            }
        });
    }

    File file;
    public static Uri mUri = null;

    private void startDownload(String sUrl) {
        PackageManager m = getPackageManager();
        String s = getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        s = p.applicationInfo.dataDir + "/video/facebook.MP4";

        file = new File(s);
        if (file.exists()) {
            file.deleteOnExit();
        }

        String urlStr = sUrl;

        URL url = null;
        try {
            url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileDownloader.getImpl()
                .create(url.toString())
                .setTag(sUrl)
                .setPath(s)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        System.out.println("pending--------------->");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        System.out.println("-------------------------------");
                        float t = (float) soFarBytes / (float) totalBytes;
                        System.out.println("t-------->" + t);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.i(TAG, "completed: " + task.getPath());
                        System.out.println("path ---->" + task.getPath());
                        mUri = Uri.fromFile(new File(task.getPath()));
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        System.out.println("pause--------------->");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        System.out.println("error--------------->" + e.toString());

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        System.out.println("warn--------------->");
                    }
                })
                .start();
    }

    private void delete_file() {
        int count = 0;
        File file = new File(FileDownloadUtils.getDefaultSaveRootPath());
        do {
            if (!file.exists()) {
                break;
            }

            if (!file.isDirectory()) {
                break;
            }

            File[] files = file.listFiles();

            if (files == null) {
                break;
            }

            for (File file1 : files) {
                count++;
                file1.delete();
            }
            if (file.exists()) {
                file.delete();
            }
        } while (false);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delete_file();
        mLisIma.clear();
    }
}
