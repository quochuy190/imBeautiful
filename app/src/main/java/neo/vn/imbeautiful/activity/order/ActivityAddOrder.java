package neo.vn.imbeautiful.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.activity.login.Menu_Search.ActivityDistrict;
import neo.vn.imbeautiful.activity.login.Menu_Search.ActivityListCity;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.City;
import neo.vn.imbeautiful.models.CustomEvent;
import neo.vn.imbeautiful.models.Districts;
import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.ObjOrder;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.PropetiObj;
import neo.vn.imbeautiful.models.respon_api.ResponGetProduct;
import neo.vn.imbeautiful.models.respon_api.ResponHistoryOrder;
import neo.vn.imbeautiful.untils.KeyboardUtil;
import neo.vn.imbeautiful.untils.SharedPrefs;
import neo.vn.imbeautiful.untils.StringUtil;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 31-May-2019
 * Time: 08:58
 * Version: 1.0
 */
public class ActivityAddOrder extends BaseActivity implements InterfaceOrder.View, View.OnClickListener {
    @BindView(R.id.btn_add_order)
    Button btn_add_order;
    @BindView(R.id.edt_fullname_customer)
    EditText edt_fullname_customer;
    @BindView(R.id.txt_district)
    TextView txt_district;
    @BindView(R.id.edt_phone_customer)
    EditText edt_phone_customer;
    @BindView(R.id.edt_address_customer)
    EditText edt_address_customer;
    @BindView(R.id.txt_city)
    TextView txt_city;
    @BindView(R.id.ll_city_spinner)
    ConstraintLayout ll_city_spinner;
    @BindView(R.id.ll_filter_district)
    ConstraintLayout ll_filter_district;
    @BindView(R.id.txt_commission)
    TextView txt_commission;
    @BindView(R.id.txt_price)
    TextView txt_price;
    private List<Products> mList;
    private PresenterOrder mPresenter;

    @Override
    public int setContentViewId() {
        return R.layout.activity_add_order;
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
        txt_title.setText("Đặt hàng");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterOrder(this);
        initAppbar();
        initData();
        initEvent();

    }

    private void initEvent() {
        ll_city_spinner.setOnClickListener(this);
        ll_filter_district.setOnClickListener(this);
        btn_add_order.setOnClickListener(this);
    }

    private void initData() {
        mList = new ArrayList<>();
        if (App.mLisProductCart.size() > 0)
            mList.addAll(App.mLisProductCart);
        if (mList.size() > 0)
            set_price_total();
    }

    City objCity;
    Districts objDistrict;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_order:
                get_api_order();
                break;
            case R.id.ll_city_spinner:
                App.mCity = null;
                App.mDistrict = null;
                objCity = null;
                Intent intent_city = new Intent(ActivityAddOrder.this, ActivityListCity.class);
                startActivityForResult(intent_city, Constants.RequestCode.GET_CITY);
                break;
            case R.id.ll_filter_district:
                if (objCity != null) {
                    App.mDistrict = null;
                    Intent intent_district = new Intent(ActivityAddOrder.this, ActivityDistrict.class);
                    intent_district.putExtra(Constants.KEY_SEND_ID_CITY, objCity.getMATP());
                    startActivityForResult(intent_district, Constants.RequestCode.GET_DISTRICT);
                } else
                    showDialogNotify("Thông báo", "Bạn chưa chọn tỉnh thành phố nào.");
                break;
        }
    }

    String sFullName = "", sPhone = "", sAddress = "", sCity = "", sDistrict = "", sUsername = "",
            sCODE_PRODUCT = "", sAMOUNT = "", sPRICE = "", sMONEY = "", sBONUS = "", sThuoctinh = "";

    private void get_api_order() {
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getsQuantum() != null && mList.get(i).getsQuantum().length() > 0) {
                    Products obj = mList.get(i);

                    int iQuantum = Integer.parseInt(mList.get(i).getsQuantum());
                    if (iQuantum > 0) {
                        if (obj.getmLisPropeti().size() > 0) {
                            String sPro = "";
                            for (PropetiObj.PropetiDetail objPropeti : obj.getmLisPropeti()) {
                                sPro = sPro + objPropeti.getSUB_ID() + ",";
                            }
                            sThuoctinh = sThuoctinh + sPro.substring(0, sPro.length() - 1) + "#";
                        }
                        sCODE_PRODUCT = sCODE_PRODUCT + mList.get(i).getCODE_PRODUCT() + "#";
                        sAMOUNT = sAMOUNT + mList.get(i).getsQuantum() + "#";
                        sPRICE = sPRICE + mList.get(i).getsPrice() + "#";
                        sMONEY = sMONEY + (iQuantum * (Integer.parseInt(mList.get(i).getsPrice()))) + "#";
                        if (mList.get(i).getCOMMISSION() != null) {
                            long commission = (Integer.parseInt(mList.get(i).getsQuantum())
                                    * Integer.parseInt(mList.get(i).getCOMMISSION())
                                    * Long.parseLong(mList.get(i).getsPrice())) / 100;
                            sBONUS = sBONUS + commission + "#";
                        }

                    }
                }
            }

        }
        sUsername = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        if (sThuoctinh.length() > 1)
            sThuoctinh = sThuoctinh.substring(0, (sThuoctinh.length() - 1));
        if (sCODE_PRODUCT.length() > 1)
            sCODE_PRODUCT = sCODE_PRODUCT.substring(0, (sCODE_PRODUCT.length() - 1));
        if (sBONUS.length() > 1)
            sBONUS = sBONUS.substring(0, (sBONUS.length() - 1));
        if (sPRICE.length() > 1)
            sPRICE = sPRICE.substring(0, (sPRICE.length() - 1));
        if (sAMOUNT.length() > 1)
            sAMOUNT = sAMOUNT.substring(0, (sAMOUNT.length() - 1));
        if (sMONEY.length() > 1)
            sMONEY = sMONEY.substring(0, (sMONEY.length() - 1));
        if (edt_fullname_customer.getText().toString().length() > 0) {
            sFullName = edt_fullname_customer.getText().toString();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào tên khách hàng");
            KeyboardUtil.requestKeyboard(edt_fullname_customer);
            return;
        }
        if (edt_phone_customer.getText().toString().length() > 0) {
            sPhone = edt_phone_customer.getText().toString();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào số điện thoại khách hàng");
            KeyboardUtil.requestKeyboard(edt_phone_customer);
            return;
        }
        if (edt_address_customer.getText().toString().length() > 0) {
            sAddress = edt_address_customer.getText().toString();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào địa chỉ khách hàng");
            KeyboardUtil.requestKeyboard(edt_address_customer);
            return;
        }
        if (objCity != null) {
            sCity = objCity.getMATP();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào địa chỉ khách hàng");
        }
        if (objDistrict != null) {
            sDistrict = objDistrict.getMAQH();
        } else {
            showDialogNotify("Thông báo", "Mời nhập vào địa chỉ khách hàng");
        }
        showDialogLoading();
        mPresenter.api_order_product_2(sUsername, sCODE_PRODUCT, sAMOUNT, sPRICE, sMONEY, sBONUS, sThuoctinh,
                sFullName, sPhone, sCity, sDistrict, sAddress);
    }

    @Override
    public void show_error_api() {
        delete_data();
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_order_history(ResponHistoryOrder obj) {

    }

    @Override
    public void show_order_history_detail(ObjOrder obj) {

    }


    @Override
    public void show_order_history_detail_pd(ResponGetProduct obj) {

    }

    @Override
    public void show_edit_order_product(ErrorApi obj) {

    }

    @Override
    public void show_order_product(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, new Intent());
            EventBus.getDefault().post(new CustomEvent("0"));
            finish();
        } else {
            delete_data();
            showDialogNotify("Thông báo", obj.getsRESULT());
        }

    }

    private void delete_data() {
        sFullName = "";
        sPhone = "";
        sAddress = "";
        sCity = "";
        sDistrict = "";
        sUsername = "";
        sCODE_PRODUCT = "";
        sAMOUNT = "";
        sPRICE = "";
        sMONEY = "";
        sBONUS = "";
        sThuoctinh = "";
    }

    @Override
    public void show_config_commission(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            if (obj.getVALUE() != null && obj.getVALUE().length() > 0) {
                int iComiss = Integer.parseInt(obj.getVALUE());
                lCommission = (iComiss * lPrice) / 100;
                txt_commission.setText(StringUtil.conventMonney_Long("" + lCommission));
                //  txt_price.setText(StringUtil.conventMonney_Long("" + lCommission));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.GET_CITY:
                if (App.mCity != null) {
                    objCity = App.mCity;
                    txt_city.setText(App.mCity.getNAME());
                } else {
                    txt_city.setText("");
                    txt_district.setText("");
                }

                break;
            case Constants.RequestCode.GET_DISTRICT:
                if (App.mDistrict != null) {
                    objDistrict = App.mDistrict;
                    txt_district.setText(App.mDistrict.getNAME());
                } else
                    txt_district.setText("");
                break;
        }
    }

    long lPrice = 0;
    long lCommission = 0;

    private void set_price_total() {

        for (Products obj : mList) {
            if (obj != null && obj.getsQuantum() != null && obj.getsQuantum().length() > 0 && obj.getsPrice() != null) {
                lPrice = lPrice + (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));
                if (obj.getCOMMISSION() != null) {
                    long pire = Integer.parseInt(obj.getCOMMISSION()) *
                            (Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice()));
                    lCommission = lCommission + pire / 100;
                }
            }
        }
        showDialogLoading();
        String sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        mPresenter.api_get_config_commission(sUserName, "" + lPrice);
        //  txt_commission.setText(StringUtil.conventMonney_Long("" + lCommission));
        txt_price.setText(StringUtil.conventMonney_Long("" + lPrice));
    }
}
