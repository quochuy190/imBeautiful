package neo.vn.imbeautiful.activity.collaborators;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.activity.login.Menu_Search.ActivityDistrict;
import neo.vn.imbeautiful.activity.login.Menu_Search.ActivityListCity;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.City;
import neo.vn.imbeautiful.models.Districts;
import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.ObjCTV;
import neo.vn.imbeautiful.models.respon_api.ResponGetLisCTV;
import neo.vn.imbeautiful.untils.KeyboardUtil;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 20-May-2019
 * Time: 15:14
 * Version: 1.0
 */
public class ActivityCtvDetail extends BaseActivity implements View.OnClickListener, InterfaceCollaborators.View {
    private ObjCTV mObjCTV;
    @BindView(R.id.txt_name_ctv_detail)
    TextView txt_name_ctv_detail;
    @BindView(R.id.txt_user_ctv_detail)
    TextView txt_user_ctv_detail;
    @BindView(R.id.edt_name_ctv)
    EditText edt_name_ctv;
    @BindView(R.id.edt_phone_ctv)
    EditText edt_phone_ctv;
    @BindView(R.id.edt_birthday_ctv)
    EditText edt_birthday_ctv;
    @BindView(R.id.edt_gender_ctv)
    EditText edt_gender_ctv;
    @BindView(R.id.edt_email_ctv)
    EditText edt_email_ctv;
    @BindView(R.id.edt_number_bank_ctv)
    EditText edt_number_bank_ctv;
    @BindView(R.id.edt_name_bank_ctv)
    EditText edt_name_bank_ctv;
    @BindView(R.id.txt_chinhanh_bank)
    TextView txt_chinhanh_bank;
    @BindView(R.id.txt_city_ctv)
    TextView txt_city_ctv;
    @BindView(R.id.txt_district_ctv)
    TextView txt_district_ctv;
    @BindView(R.id.edt_address_ctv)
    EditText edt_address_ctv;
    @BindView(R.id.icon_edit_info)
    ImageView icon_edit_info;
    @BindView(R.id.txt_title_info)
    TextView txt_title_info;
    @BindView(R.id.edt_chinhanh_bank)
    EditText edt_chinhanh_bank;
    @BindView(R.id.btn_update_info)
    Button btn_update_info;
    @BindView(R.id.icon_edit_bank)
    ImageView icon_edit_bank;
    @BindView(R.id.btn_update_bank)
    Button btn_update_bank;
    @BindView(R.id.txt_title_reset_pass)
    TextView txt_title_reset_pass;
    Calendar myCalendar_to = Calendar.getInstance();
    private PresenterCTV mPresenter;
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
        edt_birthday_ctv.setText(sdf.format(myCalendar_to.getTime()));
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_ctv_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterCTV(this);
        init();
        initAppbar();
        initData();
        initEvent();

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

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void enable_edt(EditText edt) {
        edt.setEnabled(true);
        edt.setInputType(InputType.TYPE_CLASS_TEXT);
        edt.setFocusable(true);
    }

    private void disable_edt(EditText edt) {
        edt.setEnabled(true);
        edt.setInputType(InputType.TYPE_CLASS_TEXT);
        edt.setFocusable(true);
    }

    private void initEvent() {
        txt_title_reset_pass.setOnClickListener(this);
        txt_city_ctv.setOnClickListener(this);
        txt_district_ctv.setOnClickListener(this);
        icon_edit_bank.setOnClickListener(this);
        btn_update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  disable_edt(edt_email_ctv);
                disable_edt(edt_gender_ctv);
                disable_edt(edt_name_ctv);
                btn_update_info.setVisibility(View.GONE);*/
                update_info_ctv();
            }
        });
        icon_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_update_info.setVisibility(View.VISIBLE);
                enable_edt(edt_email_ctv);
                enable_edt(edt_gender_ctv);
                enable_edt(edt_name_ctv);
            }
        });
        edt_birthday_ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityCtvDetail.this, R.style.MyDatePickerStyle, to_date, myCalendar_to
                        .get(Calendar.YEAR), myCalendar_to.get(Calendar.MONTH),
                        myCalendar_to.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void update_info_ctv() {
        String sFullNam, sBirthday, sEmail, sGender, sCityId, sDistrictId, sAddress, sSTK, sNameTK, sNameNH;
        if (edt_name_ctv.getText().toString().length() > 0) {
            sFullNam = edt_name_ctv.getText().toString();
        } else {
            showAlertDialog("Thông báo", "Bạn chưa nhập vào họ tên");
            KeyboardUtil.requestKeyboard(edt_name_ctv);
            return;
        }
        if (edt_birthday_ctv.getText().toString().length() > 0) {
            sBirthday = edt_birthday_ctv.getText().toString();
        } else {
            showAlertDialog("Thông báo", "Bạn chưa nhập vào ngày tháng năm sinh");
            KeyboardUtil.requestKeyboard(edt_birthday_ctv);
            return;
        }
        if (edt_email_ctv.getText().toString().length() > 0) {
            sEmail = edt_email_ctv.getText().toString();
        } else {
            showAlertDialog("Thông báo", "Bạn chưa nhập vào Email");
            KeyboardUtil.requestKeyboard(edt_email_ctv);
            return;
        }
        if (edt_address_ctv.getText().toString().length() > 0) {
            sAddress = edt_address_ctv.getText().toString();
        } else {
            showAlertDialog("Thông báo", "Mời bạn nhập vào địa chỉ");
            KeyboardUtil.requestKeyboard(edt_address_ctv);
            return;
        }
        if (objCity != null && objCity.getNAME() != null) {
            sCityId = objCity.getNAME();
        } else {
            showAlertDialog("Thông báo", "Mời bạn chọn lại tỉnh/thành phố");
            return;
        }
        if (objDistrict != null && objDistrict.getNAME() != null) {
            sDistrictId = objDistrict.getNAME();
        } else {
            showAlertDialog("Thông báo", "Mời bạn chọn lại quận/huyện phố");
            return;
        }
        if (edt_number_bank_ctv.getText().toString().length() > 0) {
            sSTK = edt_number_bank_ctv.getText().toString();
        } else {
            sSTK = "";
        }
        if (edt_name_bank_ctv.getText().toString().length() > 0) {
            sNameTK = edt_name_bank_ctv.getText().toString();
        } else {
            sNameTK = "";
        }
        if (edt_chinhanh_bank.getText().toString().length() > 0) {
            sNameNH = edt_chinhanh_bank.getText().toString();
        } else {
            sNameNH = "";
        }
        String sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        showDialogLoading();
        mPresenter.api_update_ctv(sUserName, mObjCTV.getUSERNAME(), sFullNam, sBirthday, mObjCTV.getGENDER(), sEmail, sCityId,
                sDistrictId, sAddress, sSTK, sNameTK, sNameNH, "");
    }

    private void initData() {
        disable_edt(edt_birthday_ctv);
        disable_edt(edt_email_ctv);
        disable_edt(edt_gender_ctv);
        disable_edt(edt_name_ctv);
        mObjCTV = (ObjCTV) getIntent().getSerializableExtra(Constants.KEY_SEND_OBJ_CTV);
        if (mObjCTV != null) {
            if (mObjCTV.getSTK() != null) {
                edt_number_bank_ctv.setText(mObjCTV.getSTK());
            } else {
                edt_number_bank_ctv.setText("....");
            }
            if (mObjCTV.getTENTK() != null) {
                edt_name_bank_ctv.setText(mObjCTV.getTENTK());
            } else {
                edt_name_bank_ctv.setText("....");

            }
            if (mObjCTV.getTEN_NH() != null) {
                edt_chinhanh_bank.setText(mObjCTV.getTEN_NH());
            } else {
                edt_chinhanh_bank.setText("....");

            }
            if (mObjCTV.getUSER_CODE() != null) {
                txt_title_info.setText("Mã CTV: " + mObjCTV.getUSER_CODE());

            } else {
                txt_title_info.setText("Mã CTV: " + "....");
            }
            if (mObjCTV.getFULL_NAME() != null) {
                txt_name_ctv_detail.setText(mObjCTV.getFULL_NAME());
                edt_name_ctv.setText(mObjCTV.getFULL_NAME());
            } else {
                edt_name_ctv.setText("....");
                txt_name_ctv_detail.setText("....");
            }
            if (mObjCTV.getUSERNAME() != null) {
                txt_user_ctv_detail.setText(mObjCTV.getUSERNAME());
            } else
                txt_user_ctv_detail.setText("....");

            if (mObjCTV.getEMAIL() != null) {
                edt_email_ctv.setText(mObjCTV.getEMAIL());
            } else
                edt_email_ctv.setText("....");

            if (mObjCTV.getEMAIL() != null) {
                edt_email_ctv.setText(mObjCTV.getEMAIL());
            } else
                edt_email_ctv.setText("....");

            if (mObjCTV.getCITY() != null && mObjCTV.getCITY_ID() != null) {
                objCity.setNAME(mObjCTV.getCITY());
                objCity.setMATP(mObjCTV.getCITY_ID());
                txt_city_ctv.setText(mObjCTV.getCITY());
            } else
                txt_city_ctv.setText("....");
            if (mObjCTV.getDISTRICT() != null && mObjCTV.getDISTRICT_ID() != null) {
                txt_district_ctv.setText(mObjCTV.getDISTRICT());
                objDistrict.setNAME(mObjCTV.getDISTRICT());
                objDistrict.setMAQH(mObjCTV.getDISTRICT_ID());
            } else
                txt_district_ctv.setText("....");
            if (mObjCTV.getADDRESS() != null) {
                edt_address_ctv.setText("Địa chỉ: " + mObjCTV.getADDRESS());
            } else
                edt_address_ctv.setText("Địa chỉ: " + "....");
        }
    }

    private void init() {
    }

    City objCity = new City();
    Districts objDistrict = new Districts();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_title_reset_pass:
                String sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
                showDialogLoading();
                mPresenter.api_reset_pass_ctv(sUserName, mObjCTV.getUSERNAME());
                break;
            case R.id.btn_update_bank:
                update_info_ctv();
                break;
            case R.id.icon_edit_bank:
                btn_update_bank.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_city_ctv:
                App.mCity = null;
                App.mDistrict = null;
                Intent intent_city = new Intent(ActivityCtvDetail.this, ActivityListCity.class);
                startActivityForResult(intent_city, Constants.RequestCode.GET_CITY);
                break;
            case R.id.txt_district_ctv:
                if (objCity != null && objCity.getMATP() != null) {
                    App.mDistrict = null;
                    Intent intent_district = new Intent(ActivityCtvDetail.this, ActivityDistrict.class);
                    intent_district.putExtra(Constants.KEY_SEND_ID_CITY, objCity.getMATP());
                    startActivityForResult(intent_district, Constants.RequestCode.GET_DISTRICT);
                } else
                    showDialogNotify("Thông báo", "Bạn chưa chọn tỉnh thành phố nào.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.GET_CITY:
                if (App.mCity != null) {
                    objCity = App.mCity;
                }
                if (objCity != null && objCity.getNAME() != null) {
                    txt_city_ctv.setText(App.mCity.getNAME());
                    txt_district_ctv.setText("");
                } else {
                    txt_city_ctv.setText("");
                    txt_district_ctv.setText("");
                }
                break;
            case Constants.RequestCode.GET_DISTRICT:
                if (App.mDistrict != null) {
                    objDistrict = App.mDistrict;
                }
                if (objDistrict != null && objDistrict.getNAME() != null) {
                    txt_district_ctv.setText(App.mDistrict.getNAME());
                } else
                    txt_district_ctv.setText("");
                break;
        }
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
    }

    @Override
    public void show_get_list_ctv(ResponGetLisCTV obj) {

    }

    @Override
    public void show_update_ctv(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            btn_update_info.setVisibility(View.GONE);
            btn_update_bank.setVisibility(View.GONE);
            Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        } else
            showAlertDialog("Thông báo", "" + obj.getsRESULT());
    }

    @Override
    public void show_reset_pass_ctv(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            showAlertDialog("Thông báo", "" + obj.getsRESULT());
           // Toast.makeText(this, "Reset mật khẩu thành công", Toast.LENGTH_SHORT).show();
        } else
            showAlertDialog("Thông báo", "" + obj.getsRESULT());
    }
}
