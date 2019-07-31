package neo.vn.imbeautiful.activity.products;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterProducts;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.CategoryProductHome;
import neo.vn.imbeautiful.models.ObjCategoryProduct;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.respon_api.ResponGetCat;
import neo.vn.imbeautiful.models.respon_api.ResponGetProduct;
import neo.vn.imbeautiful.models.respon_api.ResponSubProduct;
import neo.vn.imbeautiful.untils.KeyboardUtil;
import neo.vn.imbeautiful.untils.SharedPrefs;
import neo.vn.imbeautiful.untils.StringUtil;

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
    List<Products> mLisSearch;
    @BindView(R.id.recycle_product)
    RecyclerView recycle_lis_product;
    @BindView(R.id.img_back)
    ImageView img_back;
    private ObjCategoryProduct mCat;
    private PresenterProduct mPresenter;
    private String sUser;
    @BindView(R.id.pull_refresh_product)
    SwipeRefreshLayout pull_refresh_product;
    @BindView(R.id.edt_search_appbar)
    EditText edt_search_service;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.img_edt_search)
    ImageView ic_search_appbar;
    @BindView(R.id.txt_title)
    TextView txt_title;
    boolean isSearch = false;

    @Override
    public int setContentViewId() {
        return R.layout.activity_list_product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterProduct(this);
        KeyboardUtil.hideSoftKeyboard(this);
        img_search.setVisibility(View.VISIBLE);
        hide_search();
        initData();
        initAppbar();
        initPulltoRefesh();
        initProduct();
        initDataProduct();
        initEvent();
    }

    private void hide_title() {
        txt_title.setVisibility(View.GONE);
        edt_search_service.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.VISIBLE);
        ic_search_appbar.setVisibility(View.VISIBLE);
    }

    private void hide_search() {
        txt_title.setVisibility(View.VISIBLE);
        edt_search_service.setVisibility(View.GONE);
        img_search.setVisibility(View.VISIBLE);
        ic_search_appbar.setVisibility(View.GONE);
    }

    private void initEvent() {
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearch) {
                    hide_title();
                    isSearch = !isSearch;
                } else {
                    hide_search();
                    isSearch = !isSearch;
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_search_service.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


    }

    void filter(String text) {
        mLisSearch.clear();
        for (Products d : mLisCateProduct) {
            if (d.getsName() != null) {
                String sName = StringUtil.removeAccent(d.getsName().toLowerCase());
                String sInput = StringUtil.removeAccent(text.toLowerCase());
                if (sName.contains(sInput)) {
                    //adding the element to filtered list
                    mLisSearch.add(d);
                }
            }
        }
        adapterProduct.updateList(mLisSearch);

    }

    String sIdParent = null, sSubid = "", sTitle = "";

    private void initData() {
        sTitle = getIntent().getStringExtra(Constants.KEY_SEND_ID_PRODUCT_TITLE);
        if (sTitle != null) {
            txt_title.setText(sTitle);
        } else
            txt_title.setText("Danh sách sản phẩm");
        sIdParent = getIntent().getStringExtra(Constants.KEY_SEND_ID_PRODUCT_PARENT);
        sSubid = getIntent().getStringExtra(Constants.KEY_SEND_ID_PRODUCT_SUB);
        sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        if (sIdParent != null) {
            showDialogLoading();
            if (sSubid != null) {
                mPresenter.api_get_product_cat_detail(sUser, sIdParent, sSubid,
                        "" + page, "" + index);
            } else {
                sSubid = "";
                mPresenter.api_get_product_cat_detail(sUser, sIdParent, sSubid,
                        "" + page, "" + index);
            }

        } else {
            mCat = (ObjCategoryProduct) getIntent().getSerializableExtra(Constants.KEY_SEND_OBJ_CATEGORY_SUB);
            if (mCat != null) {
                showDialogLoading();
                if (mCat.getsName() != null)
                    txt_title.setText(mCat.getsName());
                else if (mCat.getSUB_NAME() != null)
                    txt_title.setText(mCat.getSUB_NAME());
                else txt_title.setText("Danh sách đơn hàng");
                sIdParent = mCat.getSUB_ID_PARENT();
                sSubid = mCat.getSUB_ID();
                mPresenter.api_get_product_cat_detail(sUser, sIdParent, sSubid,
                        "" + page, "" + index);
            }
        }

    }

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean isLoading = true;
    int page = 1;
    int index = 30;


    private void initAppbar() {
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initProduct() {
        mLisCateProduct = new ArrayList<>();
        mLisSearch = new ArrayList<>();
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
                                    mPresenter.api_get_product_cat_detail(sUser, sIdParent,
                                            sSubid, "" + page, "" + index);
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
            } else {
                if (page == 1)
                    showAlertDialog("Thông báo", obj.getsRESULT());
            }
        }
    }

    @Override
    public void show_product_trend(CategoryProductHome obj) {

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
