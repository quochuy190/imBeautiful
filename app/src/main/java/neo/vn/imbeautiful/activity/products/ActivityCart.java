package neo.vn.imbeautiful.activity.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.activity.order.ActivityAddOrder;
import neo.vn.imbeautiful.adapter.AdapterCart;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ClickDialog;
import neo.vn.imbeautiful.callback.ItemClickCartListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.respon_api.ObjLisCart;
import neo.vn.imbeautiful.untils.SharedPrefs;
import neo.vn.imbeautiful.untils.StringUtil;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 23-May-2019
 * Time: 23:03
 * Version: 1.0
 */
public class ActivityCart extends BaseActivity {
    @BindView(R.id.txt_value_total)
    TextView txt_value_total;
    @BindView(R.id.btn_order)
    TextView btn_order;
    @BindView(R.id.txt_value_commission)
    TextView txt_value_commission;

    @Override
    public int setContentViewId() {
        return R.layout.activity_cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppbar();
        initProduct();
        initDataProduct();
        initEvent();
    }

    private void initEvent() {
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Products objpro : mLisCateProduct) {
                    objpro.setVisibleDelete(true);
                }
                adapterProduct.notifyDataSetChanged();
            }
        });
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = false;
                for (Products obj : mLisCateProduct) {
                    if (obj.getsQuantum() != null && Integer.parseInt(obj.getsQuantum()) > 0) {
                        isCheck = true;
                    }
                }
                if (isCheck) {
                    Intent intent = new Intent(ActivityCart.this, ActivityAddOrder.class);
                    intent.putExtra(Constants.KEY_SEND_LIST_PRODUCT, (Serializable) mLisCateProduct);
                    startActivityForResult(intent, Constants.RequestCode.GET_ADD_ORDER);
                } else {
                    showDialogNotify("Thông báo", "Bạn chưa chọn sản phẩm nào để đặt hàng");
                }

            }
        });
    }

    AdapterCart adapterProduct;
    RecyclerView.LayoutManager mLayoutManagerProduct;
    List<Products> mLisCateProduct;
    @BindView(R.id.rcv_carts)
    RecyclerView recycle_lis_product;

    private void initProduct() {
        mLisCateProduct = new ArrayList<>();
        adapterProduct = new AdapterCart(mLisCateProduct, this);
        mLayoutManagerProduct = new GridLayoutManager(this, 1);
        recycle_lis_product.setHasFixedSize(true);
        recycle_lis_product.setLayoutManager(mLayoutManagerProduct);
        recycle_lis_product.setItemAnimator(new DefaultItemAnimator());
        recycle_lis_product.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();
        adapterProduct.setOnIListener(new ItemClickCartListener() {
            @Override
            public void onClickItem(int position, Object item) {
                final Products objPro = (Products) item;
                showDialogComfirm("Thông báo", "Bạn có chắc chắn muốn xoá sản phẩm này ra " +
                        "khỏi đơn hàng", true, new ClickDialog() {
                    @Override
                    public void onClickYesDialog() {
                        if (mLisCateProduct.size() > 0) {
                            for (int i = 0; i < mLisCateProduct.size(); i++) {
                                Products obj = mLisCateProduct.get(i);
                                if (obj.getCODE_PRODUCT().equals(objPro.getCODE_PRODUCT())) {
                                    mLisCateProduct.remove(obj);
                                }
                            }
                            ObjLisCart objLisCart = new ObjLisCart(mLisCateProduct);
                            SharedPrefs.getInstance().put(Constants.KEY_SAVE_LIST_CART, objLisCart);
                            adapterProduct.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onClickNoDialog() {

                    }
                });

            }

            @Override
            public void onClickItem_Add(int position, Object item) {
                Products objPro = (Products) item;
                Integer iQuantum = 0;
                if (objPro.getsQuantum() != null && objPro.getsQuantum().length() > 0) {
                    iQuantum = Integer.parseInt(objPro.getsQuantum());
                }
                iQuantum++;
                mLisCateProduct.get(position).setsQuantum("" + iQuantum);
                adapterProduct.notifyDataSetChanged();
                set_price_total();
            }

            @Override
            public void onClickItem_Minus(int position, Object item) {
                Products obj = (Products) item;
                Integer iQuantum = 0;
                String s = "";
                if (obj != null && obj.getsQuantum() != null && obj.getsQuantum().length() > 0) {
                    s = obj.getsQuantum();
                    iQuantum = Integer.parseInt(s);
                }
                if (iQuantum > 0) {
                    iQuantum--;
                    mLisCateProduct.get(position).setsQuantum("" + iQuantum);
                } else {
                    mLisCateProduct.get(position).setsQuantum("" + iQuantum);
                }
                adapterProduct.notifyDataSetChanged();
                set_price_total();
            }
        });
    }



    private void set_price_total() {
        long lPrice = 0;
        long lCommission = 0;
        for (Products obj : mLisCateProduct) {
            if (obj != null && obj.getsQuantum() != null && obj.getsQuantum().length() > 0 && obj.getsPrice() != null) {
                lPrice = lPrice + (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));
                if (obj.getCOMMISSION() != null) {
                    long pire = Integer.parseInt(obj.getCOMMISSION()) *
                            (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));
                    lCommission = lCommission + pire / 100;
                }
            }
        }
        txt_value_commission.setText(StringUtil.conventMonney_Long("" + lCommission));
        txt_value_total.setText(StringUtil.conventMonney_Long("" + lPrice));
    }

    ImageView img_home;

    private void initAppbar() {
        ImageView img_back = findViewById(R.id.img_back);
        img_home = findViewById(R.id.img_home);
        TextView txt_title = findViewById(R.id.txt_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_title.setText("Giỏ hàng");
        img_home.setVisibility(View.VISIBLE);
    }

    private void initDataProduct() {
        objCat = SharedPrefs.getInstance().get(Constants.KEY_SAVE_LIST_CART, ObjLisCart.class);
        if (objCat != null && objCat.getmList() != null && objCat.getmList().size() > 0) {
            mLisCateProduct.clear();
            mLisCateProduct.addAll(objCat.getmList());
        }
        set_price_total();
        for (Products obj : mLisCateProduct) {
            obj.setVisibleDelete(false);
        }
        adapterProduct.notifyDataSetChanged();
    }

    private void save_list_cart(List<Products> mLis) {
        ObjLisCart obj = new ObjLisCart(mLis);
        Gson gson = new Gson();
        String json = gson.toJson(mLis);
        SharedPrefs.getInstance().put(Constants.KEY_SAVE_LIST_CART, obj);
    }

    ObjLisCart objCat;

    private List<Products> get_list_cart() {
/*        String json = SharedPrefs.getInstance().get(Constants.KEY_SAVE_LIST_CART, String.class);
        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        objCat = gson.fromJson(json, ObjLisCart.class);*/
        objCat = SharedPrefs.getInstance().get(Constants.KEY_SAVE_LIST_CART, ObjLisCart.class);

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCode.GET_ADD_ORDER && resultCode == RESULT_OK)
            finish();
    }
}
