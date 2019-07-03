package neo.vn.imbeautiful.activity.products;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterCategoryProductHome;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.CategoryProductHome;
import neo.vn.imbeautiful.models.ObjCategoryProduct;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.respon_api.ResponGetCat;
import neo.vn.imbeautiful.models.respon_api.ResponGetProduct;
import neo.vn.imbeautiful.models.respon_api.ResponSubProduct;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 10-May-2019
 * Time: 16:20
 * Version: 1.0
 */
public class ActivityListCategoryProduct extends BaseActivity implements InterfaceProduct.View,
        SwipeRefreshLayout.OnRefreshListener {
    AdapterCategoryProductHome adapterProduct;
    RecyclerView.LayoutManager mLayoutManagerProduct;
    List<CategoryProductHome> mLisCateProduct;
    @BindView(R.id.recycle_product)
    RecyclerView recycle_lis_product;
    private ObjCategoryProduct mCat;
    private PresenterProduct mPresenter;
    private String sUser;
    @BindView(R.id.pull_refresh_product)
    SwipeRefreshLayout pull_refresh_product;

    @Override
    public int setContentViewId() {
        return R.layout.activity_list_product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterProduct(this);
        initData();
        initAppbar();
        initPulltoRefesh();
        initProduct();
        initEvent();
        //initDataProduct();
    }

    private void initEvent() {


    }

    private void initAppbar() {
        ImageView img_back = findViewById(R.id.img_back);
        TextView txt_title = findViewById(R.id.txt_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_title.setText("I'm beautiful");
    }

    private void initPulltoRefesh() {
        pull_refresh_product.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_product.setRefreshing(false);
            }
        }, 500);
    }

    private void initData() {
        mCat = (ObjCategoryProduct) getIntent().getSerializableExtra(Constants.KEY_SEND_OBJ_CATEGORY);
        if (mCat != null) {
            sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
            showDialogLoading();
            mPresenter.api_get_get_sub_product_child(sUser, "");
        }
    }

    private void initProduct() {
        mLisCateProduct = new ArrayList<>();
        adapterProduct = new AdapterCategoryProductHome(this, mLisCateProduct, new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {

            }
        });
        mLayoutManagerProduct = new GridLayoutManager(this, 1);
        recycle_lis_product.setHasFixedSize(true);
        recycle_lis_product.setLayoutManager(mLayoutManagerProduct);
        recycle_lis_product.setItemAnimator(new DefaultItemAnimator());
        recycle_lis_product.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();
        adapterProduct.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {

            }
        });
    }

    private void initDataProduct() {
        for (int i = 0; i < 5; i++) {
            List<Products> mLis = new ArrayList<>();
            mLis.add(new Products("Tẩy da chết", "3500 đ", "https://naototnhat.com/wp-content/uploads/2018/08/my-pham-duong-trang-da.jpg"));
            mLis.add(new Products("Tẩy da chết", "3500 đ", "https://naototnhat.com/wp-content/uploads/2018/08/my-pham-duong-trang-da.jpg"));
            mLis.add(new Products("Tẩy da chết", "3500 đ", "https://naototnhat.com/wp-content/uploads/2018/08/my-pham-duong-trang-da.jpg"));
            mLisCateProduct.add(new CategoryProductHome("Mỹ phẩm làm sạch", mLis));
        }
        adapterProduct.notifyDataSetChanged();
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_product_cat(ResponGetCat obj) {
        hideDialogLoading();
    }

    @Override
    public void show_product_sub_product(ResponSubProduct obj) {
        hideDialogLoading();
    }

    @Override
    public void show_product_sub_product_child(ResponSubProduct obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            mLisCateProduct.clear();
            mLisCateProduct.addAll(obj.getmList());
            adapterProduct.notifyDataSetChanged();
        } else
            showAlertDialog("Thông báo", obj.getsRESULT());
    }

    @Override
    public void show_product_cat_detail(ResponGetProduct obj) {

    }
}
