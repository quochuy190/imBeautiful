package neo.vn.imbeautiful.activity.products;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterProducts;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.config.Constants;
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
public class ActivityListProduct extends BaseActivity implements InterfaceProduct.View,
        SwipeRefreshLayout.OnRefreshListener {
    AdapterProducts adapterProduct;
    RecyclerView.LayoutManager mLayoutManagerProduct;
    List<Products> mLisCateProduct;
    @BindView(R.id.recycle_product)
    RecyclerView recycle_lis_product;
    @BindView(R.id.img_back)
    ImageView img_back;
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
        initDataProduct();
        initEvent();
    }

    private void initEvent() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mCat = (ObjCategoryProduct) getIntent().getSerializableExtra(Constants.KEY_SEND_OBJ_CATEGORY_SUB);
        if (mCat != null) {
            sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
            showDialogLoading();
            mPresenter.api_get_product_cat_detail(sUser, mCat.getSUB_ID_PARENT(), mCat.getSUB_ID(),
                    "" + page, "" + index);
        }
    }

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean isLoading = true;
    int page = 1;
    int index = 30;
    private void initAppbar() {
        ImageView img_back = findViewById(R.id.img_back);
        TextView txt_title = findViewById(R.id.txt_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_title.setText("Danh sách sản phẩm");
    }
    private void initProduct() {
        mLisCateProduct = new ArrayList<>();
        adapterProduct = new AdapterProducts(mLisCateProduct, this);
        mLayoutManagerProduct = new GridLayoutManager(this, 2);
        recycle_lis_product.setHasFixedSize(true);
        recycle_lis_product.setLayoutManager(mLayoutManagerProduct);
        recycle_lis_product.setItemAnimator(new DefaultItemAnimator());
        recycle_lis_product.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();
        adapterProduct.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
              /*  mLisShop.get(position).setHideSub(!mLisShop.get(position).isHideSub());
                adapter.notifyDataSetChanged();*/
                Intent intent = new Intent(ActivityListProduct.this, ActivityProductDetail.class);
                Products obj = (Products) item;
                intent.putExtra(Constants.KEY_SEND_OBJ_PRODUCTS, obj);
                startActivity(intent);
            }
        });

        // loadmore
        recycle_lis_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    GridLayoutManager layoutmanager = (GridLayoutManager) recyclerView
                            .getLayoutManager();
                    visibleItemCount = layoutmanager.getChildCount();
                    totalItemCount = layoutmanager.getItemCount();
                    pastVisiblesItems = layoutmanager.findFirstVisibleItemPosition();
                    //Log.i(TAG, visibleItemCount + " " + totalItemCount + " " + presenter_detail_ringtunes);
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (!isLoading) {
                            isLoading = true;
                            page++;
                            showDialogLoading();
                            //  key = ed_key_search_fragment.getText().toString();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mPresenter.api_get_product_cat_detail(sUser, mCat.getIDD(),
                                            mCat.getSUB_ID(), "" + page, "" + index);
                                }
                            }, 1000);
                        }
                    }
                }
            }
        });
    }

    private void initDataProduct() {
       /* for (int i = 0; i < 10; i++) {
            mLisCateProduct.add(new Products("Tẩy tế bào chết Arrahan Lemon Peeling Gel hương chanh 180ml",
                    "3500 đ", "https://naototnhat.com/wp-content/uploads/2018/08/my-pham-duong-trang-da.jpg"));

        }
        adapterProduct.notifyDataSetChanged();*/
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_product_cat(ResponGetCat obj) {

    }

    @Override
    public void show_product_sub_product(ResponSubProduct obj) {

    }

    @Override
    public void show_product_sub_product_child(ResponSubProduct obj) {

    }

    @Override
    public void show_product_cat_detail(ResponGetProduct obj) {
        hideDialogLoading();
        if (obj != null) {
            if (obj.getsERROR() != null && obj.getsERROR().equals("0000")) {
                if (obj.getmList() != null && obj.getmList().size() > 0) {
                    isLoading = false;
                    mLisCateProduct.addAll(obj.getmList());
                    adapterProduct.notifyDataSetChanged();
                }
            }
        }
    }

    private void initPulltoRefesh() {
        pull_refresh_product.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                showDialogLoading();
                mLisCateProduct.clear();
                mPresenter.api_get_product_cat_detail(sUser, mCat.getIDD(), mCat.getSUB_ID(),
                        "" + page, "" + index);
                pull_refresh_product.setRefreshing(false);
            }
        }, 500);
    }
}
