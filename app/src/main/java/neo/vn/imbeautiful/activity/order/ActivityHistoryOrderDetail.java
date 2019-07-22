package neo.vn.imbeautiful.activity.order;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterProductInHistoryOrder;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ItemClickCartListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.ObjLogin;
import neo.vn.imbeautiful.models.ObjOrder;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.respon_api.ResponGetProduct;
import neo.vn.imbeautiful.models.respon_api.ResponHistoryOrder;
import neo.vn.imbeautiful.untils.SharedPrefs;
import neo.vn.imbeautiful.untils.StringUtil;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 03-June-2019
 * Time: 08:23
 * Version: 1.0
 */
public class ActivityHistoryOrderDetail extends BaseActivity
        implements InterfaceOrder.View, View.OnClickListener {
    AdapterProductInHistoryOrder adapterProduct;
    RecyclerView.LayoutManager mLayoutManagerProduct;
    List<Products> mLisCateProduct;
    @BindView(R.id.rcv_product_in_order_detail)
    RecyclerView recycle_lis_product;
    @BindView(R.id.txt_price)
    TextView txt_price;
    ObjOrder objOrder;
    String sUserName;
    ObjLogin mLogin;
    private PresenterOrder mPresenter;
    private int sPrice;
    @BindView(R.id.txt_nguoinhan)
    TextView txt_nguoinhan;
    @BindView(R.id.txt_user)
    TextView txt_user;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_phone_ngnhan)
    TextView txt_phone_ngnhan;
    @BindView(R.id.txt_address_ngnhan)
    TextView txt_address_ngnhan;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_item_order_status)
    TextView txt_item_order_status;
    @BindView(R.id.ic_edit_blue)
    ImageView ic_edit_blue;
    @BindView(R.id.ic_edit_green)
    ImageView ic_edit_green;
    @BindView(R.id.ic_edit_product)
    ImageView ic_edit_product;
    @BindView(R.id.txt_total)
    TextView txt_total;
    @BindView(R.id.txt_time_dukien)
    TextView txt_time_dukien;
    @BindView(R.id.txt_ship)
    EditText txt_ship;
    @BindView(R.id.btn_update)
    Button btn_update;

    @Override
    public int setContentViewId() {
        return R.layout.activity_order_history_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterOrder(this);
        initAppbar();
        initProduct();
        get_all_history();
        initData();
        initEvent();
    }

    public void get_all_history() {
        myCalendar_to.add(Calendar.DAY_OF_MONTH, 4);
        updateTodate();

    }

    private void initEvent() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_api_order();
            }
        });
        ic_edit_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityHistoryOrderDetail.this, R.style.MyDatePickerStyle,
                        to_date, myCalendar_to
                        .get(Calendar.YEAR), myCalendar_to.get(Calendar.MONTH),
                        myCalendar_to.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        ic_edit_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStatus();
            }
        });
        ic_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLogin.getGROUPS().equals("5") && objOrder.getSTATUS().equals("1")) {
                    if (mLisCateProduct.size() > 0) {
                        for (Products obj : mLisCateProduct) {
                            obj.setVisibleButtonAdd(true);
                        }
                        adapterProduct.notifyDataSetChanged();
                    }
                }

            }
        });
        txt_ship.addTextChangedListener(new TextWatcher() {
            private boolean lock;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // txt_ship.setText(StringUtil.conventMonney_Long(s.toString()));
            }
        });
    }

    private void initData() {
        mLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
        objOrder = (ObjOrder) getIntent().getSerializableExtra(Constants.KEY_SAND_OBJ_ORDER);
        sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        if (objOrder != null && objOrder.getCODE_ORDER() != null) {
            showDialogLoading();
            mPresenter.api_get_order_history_detail(sUserName, objOrder.getCODE_ORDER());
            mPresenter.api_get_order_history_detail_pd(sUserName, objOrder.getCODE_ORDER());
            set_bg_status();
        }

        if (mLogin.getGROUPS().equals("5") && objOrder.getSTATUS().equals("1")) {
            ic_edit_product.setVisibility(View.VISIBLE);
        } else
            ic_edit_product.setVisibility(View.INVISIBLE);
        if (!mLogin.getGROUPS().equals("5")) {
            if (objOrder.getSTATUS().equals("0") || objOrder.getSTATUS().equals("4")) {
                ic_edit_blue.setVisibility(View.INVISIBLE);
                ic_edit_green.setVisibility(View.INVISIBLE);
            } else {
                ic_edit_blue.setVisibility(View.VISIBLE);
                ic_edit_green.setVisibility(View.VISIBLE);
            }

        } else {
            ic_edit_green.setVisibility(View.INVISIBLE);
            ic_edit_green.setVisibility(View.INVISIBLE);
        }


    }

    private void set_bg_status() {
        switch (objOrder.getSTATUS()) {
            case "0":
                txt_item_order_status.setText("Đã hoàn thành");
                txt_item_order_status.setBackground(getResources()
                        .getDrawable(R.drawable.spr_txt_status_order_orange));
                break;
            case "1":
                txt_item_order_status.setText("Đang xử lý");
                txt_item_order_status.setBackground(getResources()
                        .getDrawable(R.drawable.spr_txt_status_order_blue));
                break;
            case "2":
                txt_item_order_status.setText("Đã tiếp nhận");
                txt_item_order_status.setBackground(getResources()
                        .getDrawable(R.drawable.spr_txt_status_order_green));
                break;
            case "3":
                txt_item_order_status.setText("Đang vận chuyển");
                txt_item_order_status.setBackground(getResources()
                        .getDrawable(R.drawable.spr_txt_status_order_green));
                break;
            case "4":
                txt_item_order_status.setText("Đã huỷ");
                txt_item_order_status.setBackground(getResources()
                        .getDrawable(R.drawable.spr_txt_status_order_red));
                break;

        }
    }

    private void initProduct() {
        mLisCateProduct = new ArrayList<>();
        adapterProduct = new AdapterProductInHistoryOrder(mLisCateProduct, this);
        mLayoutManagerProduct = new GridLayoutManager(this, 1);
        recycle_lis_product.setHasFixedSize(true);
        recycle_lis_product.setLayoutManager(mLayoutManagerProduct);
        recycle_lis_product.setItemAnimator(new DefaultItemAnimator());
        recycle_lis_product.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();
        adapterProduct.setOnIListener(new ItemClickCartListener() {
            @Override
            public void onClickItem(int position, Object item) {

            }

            @Override
            public void onClickItem_Add(int position, Object item) {
                Products objPro = (Products) item;
                Integer iQuantum = 0;
                if (objPro.getNUM() != null && objPro.getNUM().length() > 0) {
                    iQuantum = Integer.parseInt(objPro.getNUM());
                }
                iQuantum++;
                mLisCateProduct.get(position).setNUM("" + iQuantum);
                adapterProduct.notifyDataSetChanged();
                set_price_total();
            }

            @Override
            public void onClickItem_Minus(int position, Object item) {
                Products obj = (Products) item;
                Integer iQuantum = 0;
                String s = "";
                if (obj != null && obj.getNUM() != null && obj.getNUM().length() > 0) {
                    s = obj.getNUM();
                    iQuantum = Integer.parseInt(s);
                }
                if (iQuantum > 0) {
                    iQuantum--;
                    mLisCateProduct.get(position).setNUM("" + iQuantum);
                } else {
                    mLisCateProduct.get(position).setNUM("" + iQuantum);
                }
                adapterProduct.notifyDataSetChanged();
                set_price_total();
            }
        });
    }

    private void set_price_total() {
        long lPrice = 0;
        for (Products obj : mLisCateProduct) {
            if (obj != null && obj.getNUM() != null && obj.getNUM().length() > 0 && obj.getsPrice() != null) {
                lPrice = lPrice + (Integer.parseInt(obj.getNUM()) * Integer.parseInt(obj.getsPrice()));
            }
        }
        txt_price.setText(StringUtil.conventMonney_Long("" + lPrice));
        txt_total.setText(StringUtil.conventMonney_Long("" + (lPrice + 30000)));
        txt_ship.setText(StringUtil.conventMonney_Long("30000"));

    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_order_history(ResponHistoryOrder obj) {

    }

    @Override
    public void show_order_history_detail(ObjOrder objOrder) {
        hideDialogLoading_delay();
        if (objOrder != null && objOrder.getERROR().equals("0000")) {
            if (objOrder.getFULL_NAME_CTV() != null) {
                txt_name.setText("Tên CTV: " + objOrder.getFULL_NAME_CTV());
            } else {
                txt_name.setText("Tên CTV: ...");
            }
            if (objOrder.getUSER_CODE() != null) {
                txt_user.setText("Mã CTV: " + objOrder.getUSER_CODE());
            } else {
                txt_user.setText("Mã CTV: ...");
            }
            if (objOrder.getEMAIL() != null) {
                txt_email.setText("Email: " + objOrder.getEMAIL());
            } else {
                txt_email.setText("Email: ...");
            }
            if (objOrder.getCREATE_DATE() != null) {
                txt_time.setText(objOrder.getCREATE_DATE());
            } else {
                txt_time.setText("...");
            }
            if (objOrder.getFULLNAME_RECEIVER() != null) {
                sFullName = objOrder.getFULLNAME_RECEIVER();
                txt_nguoinhan.setText(objOrder.getFULLNAME_RECEIVER());
            } else {
                sFullName = "";
                txt_nguoinhan.setText("...");
            }
            if (objOrder.getMOBILE_RECEIVER() != null) {
                sPhone = objOrder.getMOBILE_RECEIVER();
                txt_phone_ngnhan.setText(objOrder.getMOBILE_RECEIVER());
            } else {

                txt_phone_ngnhan.setText("...");
            }
            if (objOrder.getADDRESS_RECEIVER() != null) {
                sAddress = objOrder.getADDRESS_RECEIVER();
                txt_address_ngnhan.setText(objOrder.getADDRESS_RECEIVER());
            } else {
                txt_address_ngnhan.setText("...");
            }
            if (objOrder.getID_CODE_ORDER() != null)
                sCode_Order = objOrder.getID_CODE_ORDER();
            if (objOrder.getCREATE_DATE() != null) {
                sTimeReceiver = objOrder.getCREATE_DATE();
            }
            if (objOrder.getID_CITY() != null)
                sCity = objOrder.getID_CITY();
            if (objOrder.getID_DISTRICT() != null)
                sDistrict = objOrder.getID_DISTRICT();
            if (objOrder.getSTATUS() != null) {
                switch (objOrder.getSTATUS()) {
                    case "0":
                        txt_item_order_status.setText("Đã hoàn thành");
                        sStatus = "0";
                        break;
                    case "1":
                        sStatus = "1";
                        txt_item_order_status.setText("Đang xử lý");
                        break;
                    case "2":
                        sStatus = "2";
                        txt_item_order_status.setText("Đã tiếp nhận");
                        break;
                    case "3":
                        sStatus = "3";
                        txt_item_order_status.setText("Đang vận chuyển");
                        break;
                    case "4":
                        sStatus = "4";
                        txt_item_order_status.setText("Đã huỷ");
                        break;
                }
            }
        }
    }

    Calendar myCalendar_to = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener to_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar_to.set(Calendar.YEAR, year);
            myCalendar_to.set(Calendar.MONTH, monthOfYear);
            myCalendar_to.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTodate();
        }

    };

    private void updateTodate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txt_time_dukien.setText(sdf.format(myCalendar_to.getTime()));
    }

    @Override
    public void show_order_history_detail_pd(ResponGetProduct obj) {
        hideDialogLoading_delay();
        if (obj.getsERROR().equals("0000")) {
            mLisCateProduct.clear();
            if (obj.getmList() != null) {
                mLisCateProduct.addAll(obj.getmList());
            }
            set_price_total();
          /*  for (Products objpro : mLisCateProduct) {
                if (objpro.getMONEY() != null && objpro.getMONEY().length() > 0) {
                    sPrice = sPrice + Integer.parseInt(objpro.getMONEY());
                }
            }

            txt_price.setText(StringUtil.conventMonney_Long("" + sPrice));*/

            adapterProduct.notifyDataSetChanged();
        }
    }

    @Override
    public void show_edit_order_product(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            Toast.makeText(this, "Cập nhật đơn hàng thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else showDialogNotify("Thông báo", obj.getsRESULT());
    }

    @Override
    public void show_order_product(ErrorApi obj) {

    }

    @Override
    public void show_config_commission(ErrorApi obj) {

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

    Dialog dialog_yes;

    public void showDialogStatus() {
        dialog_yes = new Dialog(this, R.style.Theme_Dialog);
        dialog_yes.setCancelable(false);
        dialog_yes.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_yes.setContentView(R.layout.dialog_selected_status);
        dialog_yes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txt_title = (TextView) dialog_yes.findViewById(R.id.txt_warning_title);
        TextView txt_dangchoduyet = (TextView) dialog_yes.findViewById(R.id.txt_dangchoduyet);
        TextView txt_dangchuyen = (TextView) dialog_yes.findViewById(R.id.txt_dangchuyen);
        TextView txt_hoanthanh = (TextView) dialog_yes.findViewById(R.id.txt_hoanthanh);
        TextView txt_huy = (TextView) dialog_yes.findViewById(R.id.txt_huy);
        TextView txt_back = (TextView) dialog_yes.findViewById(R.id.txt_back);
        TextView txt_datiepnhan = (TextView) dialog_yes.findViewById(R.id.txt_datiepnhan);

        txt_title.setOnClickListener(this);
        txt_dangchoduyet.setOnClickListener(this);
        txt_dangchuyen.setOnClickListener(this);
        txt_hoanthanh.setOnClickListener(this);
        txt_huy.setOnClickListener(this);
        txt_back.setOnClickListener(this);
        txt_datiepnhan.setOnClickListener(this);
        dialog_yes.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_dangchoduyet:
                txt_item_order_status.setText("Đang xử lý");
                dialog_yes.dismiss();
                sStatus = "1";
                break;
            case R.id.txt_datiepnhan:
                txt_item_order_status.setText("Đã tiếp nhận");
                sStatus = "2";
                dialog_yes.dismiss();
                break;
            case R.id.txt_dangchuyen:
                txt_item_order_status.setText("Đang vận chuyển");
                sStatus = "3";
                dialog_yes.dismiss();
                break;
            case R.id.txt_hoanthanh:
                sStatus = "0";
                txt_item_order_status.setText("Đã hoàn thành");
                dialog_yes.dismiss();
                break;
            case R.id.txt_huy:
                sStatus = "4";
                txt_item_order_status.setText("Huỷ đơn");
                dialog_yes.dismiss();
                break;
            case R.id.txt_back:

                dialog_yes.dismiss();
                break;
        }
    }

    String sExerShip = "", sStatus = "", sFullName = "", sPhone = "", sAddress = "", sCity = "", sDistrict = "",
            sUsername = "", sCODE_PRODUCT = "", sAMOUNT = "", sPRICE = "", sMONEY = "", sBONUS = "",
            sCode_Order = "", sTimeReceiver = "";

    private void get_api_order() {
        if (mLisCateProduct != null && mLisCateProduct.size() > 0) {
            for (int i = 0; i < mLisCateProduct.size(); i++) {
                if (mLisCateProduct.get(i).getNUM() != null && mLisCateProduct.get(i).getNUM().length() > 0) {
                    int iQuantum = Integer.parseInt(mLisCateProduct.get(i).getNUM());
                    if (iQuantum > 0) {
                        sCODE_PRODUCT = sCODE_PRODUCT + mLisCateProduct.get(i).getCODE_PRODUCT() + "#";
                        sAMOUNT = sAMOUNT + mLisCateProduct.get(i).getNUM() + "#";
                        sPRICE = sPRICE + mLisCateProduct.get(i).getsPrice() + "#";
                        sMONEY = sMONEY + (iQuantum * (Integer.parseInt(mLisCateProduct.get(i).getsPrice()))) + "#";
                        if (mLisCateProduct.get(i).getCOMMISSION() != null) {
                            long commission = (Integer.parseInt(mLisCateProduct.get(i).getNUM())
                                    * Integer.parseInt(mLisCateProduct.get(i).getCOMMISSION())
                                    * Long.parseLong(mLisCateProduct.get(i).getsPrice())) / 100;
                            sBONUS = sBONUS + commission + "#";
                        }
                        ;
                    }
                }
            }
        }
        sUsername = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);

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
        sExerShip = txt_time_dukien.getText().toString();
        showDialogLoading();
        mPresenter.api_edit_order_product(sUsername, sCODE_PRODUCT, sAMOUNT, sPRICE, sMONEY, sBONUS,
                sFullName, sPhone, sCity, sDistrict, sAddress, sCode_Order, sStatus, "30000", sExerShip);
    }
}
