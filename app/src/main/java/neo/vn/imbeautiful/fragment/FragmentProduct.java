package neo.vn.imbeautiful.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.activity.products.ActivityListCategoryProduct;
import neo.vn.imbeautiful.activity.products.ActivityListProduct;
import neo.vn.imbeautiful.activity.products.ActivityProductDetail;
import neo.vn.imbeautiful.activity.products.InterfaceProduct;
import neo.vn.imbeautiful.activity.products.PresenterProduct;
import neo.vn.imbeautiful.adapter.AdapterCategoryProduct;
import neo.vn.imbeautiful.adapter.AdapterCategoryProductHome;
import neo.vn.imbeautiful.base.BaseFragment;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.callback.OnListenerItemClickObjService;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.CategoryProductHome;
import neo.vn.imbeautiful.models.ObjCategoryProduct;
import neo.vn.imbeautiful.models.ObjSucCategory;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.respon_api.ResponGetCat;
import neo.vn.imbeautiful.models.respon_api.ResponGetProduct;
import neo.vn.imbeautiful.models.respon_api.ResponSubProduct;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * @author Quốc Huy
 * @version 1.0.0
 * @description
 * @desc Developer NEO Company.
 * @created 22/04/2019
 * @updated 22/04/2019
 * @modified by
 * @updated on 22/04/2019
 * @since 1.0
 */
public class FragmentProduct extends BaseFragment implements InterfaceProduct.View {
    private static final String TAG = "FragmentProduct";
    public static FragmentProduct fragment;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.ll_fragment_product)
    ConstraintLayout ll_fragment_product;
    private PresenterProduct mPresenter;
    private String mUser;

    public static FragmentProduct getInstance() {
        if (fragment == null) {
            synchronized (FragmentProduct.class) {
                if (fragment == null)
                    fragment = new FragmentProduct();
            }
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new PresenterProduct(this);
        Log.e(TAG, "onCreateView: Product");
        init();
        initDataCat();
        initProduct();
        initDataProduct();
        initEvent();
        return view;
    }

    private void initDataProduct() {
     /*   for (int i = 0; i < 5; i++) {
            List<Products> mLis = new ArrayList<>();
            mLis.add(new Products("Tẩy da chết", "3500 đ", "https://naototnhat.com/wp-content/uploads/2018/08/my-pham-duong-trang-da.jpg"));
            mLis.add(new Products("Tẩy da chết", "3500", "https://naototnhat.com/wp-content/uploads/2018/08/my-pham-duong-trang-da.jpg"));
            mLis.add(new Products("Tẩy da chết", "3500", "https://naototnhat.com/wp-content/uploads/2018/08/my-pham-duong-trang-da.jpg"));
            mLisCateProduct.add(new CategoryProductHome("Mỹ phẩm làm sạch", mLis));
        }
        adapter.notifyDataSetChanged();*/

    }

    boolean isHideCategory = true;

    private void initEvent() {
        ll_fragment_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isHideCategory) {
                    list_menu_untility.setVisibility(View.GONE);
                    recycle_lis_product.setVisibility(View.VISIBLE);
                    isHideCategory = !isHideCategory;
                } else {
                    list_menu_untility.setVisibility(View.VISIBLE);
                    recycle_lis_product.setVisibility(View.GONE);
                    isHideCategory = !isHideCategory;
                }

            }
        });
    }

    private void initDataCat() {
        showDialogLoading();
        mUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        mPresenter.api_get_product_cat(mUser, "0");
        mPresenter.api_get_get_sub_product(mUser, "");

    }

    AdapterCategoryProduct adapter;
    AdapterCategoryProductHome adapterProduct;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.LayoutManager mLayoutManagerProduct;
    List<ObjCategoryProduct> mLisShop;
    List<CategoryProductHome> mLisCateProduct;
    ObjSucCategory mObj;
    @BindView(R.id.recycle_lis_category_product)
    RecyclerView list_menu_untility;
    @BindView(R.id.recycle_lis_product)
    RecyclerView recycle_lis_product;

    private void init() {
        mLisShop = new ArrayList<>();
        adapter = new AdapterCategoryProduct(getContext(), mLisShop, new OnListenerItemClickObjService() {
            @Override
            public void onClickListener(ObjCategoryProduct item) {
                Intent intent = new Intent(getContext(), ActivityListProduct.class);
                ObjCategoryProduct obj = (ObjCategoryProduct) item;
                intent.putExtra(Constants.KEY_SEND_OBJ_CATEGORY_SUB, obj);
                startActivity(intent);
            }

            @Override
            public void onItemXemthemClick(ObjCategoryProduct objService) {
            }
        });
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        list_menu_untility.setHasFixedSize(true);
        list_menu_untility.setLayoutManager(mLayoutManager);
        list_menu_untility.setItemAnimator(new DefaultItemAnimator());
        list_menu_untility.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                mLisShop.get(position).setHideSub(!mLisShop.get(position).isHideSub());
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnIListener_Title(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                Intent intent = new Intent(getContext(), ActivityListCategoryProduct.class);
                ObjCategoryProduct obj = (ObjCategoryProduct) item;
                intent.putExtra(Constants.KEY_SEND_OBJ_CATEGORY, obj);
                startActivity(intent);
            }
        });
    }

    private void initProduct() {
        mLisCateProduct = new ArrayList<>();
        adapterProduct = new AdapterCategoryProductHome(getContext(), mLisCateProduct, new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                Intent intent = new Intent(getContext(), ActivityProductDetail.class);
                Products obj = (Products) item;
                intent.putExtra(Constants.KEY_SEND_OBJ_PRODUCTS, obj);
                startActivity(intent);
            }
        });
        mLayoutManagerProduct = new GridLayoutManager(getContext(), 1);
        recycle_lis_product.setHasFixedSize(true);
        recycle_lis_product.setLayoutManager(mLayoutManagerProduct);
        recycle_lis_product.setItemAnimator(new DefaultItemAnimator());
        recycle_lis_product.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();
        adapterProduct.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
               /* mLisShop.get(position).setHideSub(!mLisShop.get(position).isHideSub());
                adapter.notifyDataSetChanged();*/
                Intent intent = new Intent(getContext(), ActivityListCategoryProduct.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_product_cat(ResponGetCat obj) {
     //   hideDialogLoading();
        if (obj != null) {
            if (obj.getsERROR() != null && obj.getsERROR().equals("0000")) {
                mLisShop.clear();
                mLisShop.addAll(obj.getmList());
                adapter.notifyDataSetChanged();
            } else showAlertDialog("Thông báo", obj.getsRESULT());
        }
    }

    @Override
    public void show_product_sub_product(ResponSubProduct obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            mLisCateProduct.clear();
            mLisCateProduct.addAll(obj.getmList());
            adapterProduct.notifyDataSetChanged();
        } else
            showAlertDialog("Thông báo", obj.getsRESULT());
    }

    @Override
    public void show_product_sub_product_child(ResponSubProduct obj) {

    }

    @Override
    public void show_product_cat_detail(ResponGetProduct obj) {

    }
}
