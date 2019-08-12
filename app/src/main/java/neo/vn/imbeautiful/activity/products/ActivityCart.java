package neo.vn.imbeautiful.activity.products;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.activity.order.ActivityAddOrder;
import neo.vn.imbeautiful.adapter.AdapterCart;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ClickDialog;
import neo.vn.imbeautiful.callback.ItemClickCartListener;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.PropetiObj;
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
        initBottom();
        initAppbar();
        initProduct();
        initDataProduct();
        initEvent();
    }

    BottomSheetDialog mBottomSheetDialog;
    ImageView img_product_dialog;
    TextView txt_name_pro_dialog;
    TextView txt_price_pro_dialog;
    TextView txt_code_pro_dialog;
    Button btn_comfirm;
    ConstraintLayout ll_spinner_1, ll_spinner_2, ll_spinner_3, ll_spinner_4;
    TextView txt_title_spinner_1, txt_title_spinner_2, txt_title_spinner_3, txt_title_spinner_4;
    List<String> data_spinner_1, data_spinner_2, data_spinner_3, data_spinner_4;
    Spinner spiner_type_1, spiner_type_2, spiner_type_3, spiner_type_4;

    private void initBottom() {
        data_spinner_1 = new ArrayList<>();
        data_spinner_2 = new ArrayList<>();
        data_spinner_3 = new ArrayList<>();
        data_spinner_4 = new ArrayList<>();
        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet_cart, null);
        mBottomSheetDialog.setContentView(sheetView);
        img_product_dialog = sheetView.findViewById(R.id.img_product_dialog);
        txt_name_pro_dialog = sheetView.findViewById(R.id.txt_name_product_dialog);
        txt_price_pro_dialog = sheetView.findViewById(R.id.txt_price_cart);
        txt_code_pro_dialog = sheetView.findViewById(R.id.txt_code_pro_dialog);
        ll_spinner_1 = sheetView.findViewById(R.id.ll_spinner_1);
        ll_spinner_2 = sheetView.findViewById(R.id.ll_spinner_2);
        ll_spinner_3 = sheetView.findViewById(R.id.ll_spinner_3);
        ll_spinner_4 = sheetView.findViewById(R.id.ll_spinner_4);

        txt_title_spinner_1 = sheetView.findViewById(R.id.txt_title_spinner_1);
        txt_title_spinner_2 = sheetView.findViewById(R.id.txt_title_spinner_2);
        txt_title_spinner_3 = sheetView.findViewById(R.id.txt_title_spinner_3);
        txt_title_spinner_4 = sheetView.findViewById(R.id.txt_title_spinner_4);

        spiner_type_1 = sheetView.findViewById(R.id.spiner_type_1);
        spiner_type_2 = sheetView.findViewById(R.id.spiner_type_2);
        spiner_type_3 = sheetView.findViewById(R.id.spiner_type_3);
        spiner_type_4 = sheetView.findViewById(R.id.spiner_type_4);
        btn_comfirm = sheetView.findViewById(R.id.btn_comfirm);

    }

    private String sThuoctinh1 = "", sThuoctinh2 = "", sThuoctinh3 = "", sThuoctinh4 = "";
    PropetiObj.PropetiDetail mObjPro1, mObjPro2, mObjPro3, mObjPro4;
    Products mProductClick;

    private void show_Bottom_Dialog(Products objProduct) {
        mProductClick = objProduct;
        data_spinner_1.clear();
        data_spinner_2.clear();
        data_spinner_3.clear();
        data_spinner_4.clear();

        if (objProduct.getsUrlImage() != null) {
            Glide.with(this).load(objProduct.getsUrlImage()).asBitmap()
                    .placeholder(R.drawable.img_defaul)
                    .into(new BitmapImageViewTarget(img_product_dialog) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            //   progressBar.setVisibility(View.GONE);
                        }
                    });
        } else
            Glide.with(this).load(R.drawable.img_defaul).into(img_product_dialog);
        if (objProduct.getsName() != null && objProduct.getsName().length() > 0)
            txt_name_pro_dialog.setText(objProduct.getsName());
        else
            txt_name_pro_dialog.setText("...");
        if (objProduct != null && objProduct.getsPrice().length() > 0)
            txt_price_pro_dialog.setText(StringUtil.conventMonney(objProduct.getsPrice()));
        else
            txt_price_pro_dialog.setText("...");
        if (objProduct != null && objProduct.getCODE_PRODUCT().length() > 0)
            txt_code_pro_dialog.setText("Mã sản phẩm: " + objProduct.getCODE_PRODUCT());
        else
            txt_code_pro_dialog.setText("...");
        if (objProduct.getmListThuoctinhTong() != null) {
            if (objProduct.getmListThuoctinhTong().size() > 0) {
                ll_spinner_1.setVisibility(View.VISIBLE);
                PropetiObj objPro = objProduct.getmListThuoctinhTong().get(0);
                txt_title_spinner_1.setText(objPro.getNAME());
                for (PropetiObj.PropetiDetail objDetail : objPro.getINFO()) {
                    objDetail.setNAME_PARENT(objPro.getNAME());
                    data_spinner_1.add(objDetail.getSUB_PROPERTIES());
                }
            }
            if (objProduct.getmListThuoctinhTong().size() > 1) {
                ll_spinner_2.setVisibility(View.VISIBLE);
                PropetiObj objPro = objProduct.getmListThuoctinhTong().get(1);
                txt_title_spinner_2.setText(objPro.getNAME());
                for (PropetiObj.PropetiDetail objDetail : objPro.getINFO()) {
                    objDetail.setNAME_PARENT(objPro.getNAME());
                    data_spinner_2.add(objDetail.getSUB_PROPERTIES());
                }
            }
            if (objProduct.getmListThuoctinhTong().size() > 2) {
                ll_spinner_3.setVisibility(View.VISIBLE);
                PropetiObj objPro = objProduct.getmListThuoctinhTong().get(2);
                txt_title_spinner_3.setText(objPro.getNAME());
                for (PropetiObj.PropetiDetail objDetail : objPro.getINFO()) {
                    objDetail.setNAME_PARENT(objPro.getNAME());
                    data_spinner_3.add(objDetail.getSUB_PROPERTIES());
                }
            }
            if (objProduct.getmListThuoctinhTong().size() > 3) {
                ll_spinner_4.setVisibility(View.VISIBLE);
                PropetiObj objPro = objProduct.getmListThuoctinhTong().get(3);
                txt_title_spinner_4.setText(objPro.getNAME());
                for (PropetiObj.PropetiDetail objDetail : objPro.getINFO()) {
                    objDetail.setNAME_PARENT(objPro.getNAME());
                    data_spinner_4.add(objDetail.getSUB_PROPERTIES());
                }
            }
        }
        set_data_spinner_1();
        set_data_spinner_2();
        set_data_spinner_3();
        set_data_spinner_4();
        mBottomSheetDialog.show();
    }

    private void set_data_spinner_1() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_spinner, data_spinner_1);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spiner_type_1.setAdapter(adapter);
        spiner_type_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sThuoctinh1 = data_spinner_1.get(position);
                mObjPro1 = mProductClick.getmListThuoctinhTong().get(0).getINFO().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void set_data_spinner_2() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_spinner, data_spinner_2);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spiner_type_2.setAdapter(adapter);
        spiner_type_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sThuoctinh2 = data_spinner_2.get(position);
                mObjPro2 = mProductClick.getmListThuoctinhTong().get(1).getINFO().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void set_data_spinner_3() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_spinner, data_spinner_3);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spiner_type_3.setAdapter(adapter);
        spiner_type_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sThuoctinh3 = data_spinner_3.get(position);
                mObjPro3 = mProductClick.getmListThuoctinhTong().get(2).getINFO().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void set_data_spinner_4() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_spinner, data_spinner_4);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spiner_type_4.setAdapter(adapter);
        spiner_type_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sThuoctinh4 = data_spinner_4.get(position);
                mObjPro4 = mProductClick.getmListThuoctinhTong().get(3).getINFO().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initEvent() {
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                for (Products obj : mLisCateProduct) {
                    if (obj.getCODE_PRODUCT().equals(mProductClick.getCODE_PRODUCT())) {
                        String sProperties = "";
                        List<PropetiObj.PropetiDetail> mListPrppeti = new ArrayList<>();
                        if (sThuoctinh1.length() > 0) {
                            sProperties = sThuoctinh1 + ",";
                        }
                        if (sThuoctinh2.length() > 0) {
                            sProperties = sThuoctinh2 + ",";
                        }
                        if (sThuoctinh3.length() > 0) {
                            sProperties = sThuoctinh3 + ",";
                        }
                        if (sThuoctinh4.length() > 0) {
                            sProperties = sThuoctinh4 + ",";
                        }
                        if (mObjPro1 != null)
                            mListPrppeti.add(mObjPro1);
                        if (mObjPro2 != null)
                            mListPrppeti.add(mObjPro2);
                        if (mObjPro3 != null)
                            mListPrppeti.add(mObjPro3);
                        if (mObjPro4 != null)
                            mListPrppeti.add(mObjPro4);
                        if (mListPrppeti.size() > 0)
                            obj.setmLisPropeti(mListPrppeti);
                        if (sProperties.length() > 0) {
                            sProperties = sProperties.substring(0, sProperties.length() - 1);
                            obj.setsThuoctinh(sProperties);
                        }
                    }
                }
                set_price_total();
                adapterProduct.notifyDataSetChanged();
            }
        });
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
                    // intent.putExtra(Constants.KEY_SEND_LIST_PRODUCT, (Serializable) mLisCateProduct);
                    App.mLisProductCart.clear();
                    App.mLisProductCart.addAll(mLisCateProduct);
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
        adapterProduct.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                Products objPro = (Products) item;
                show_Bottom_Dialog(objPro);

            }
        });
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
