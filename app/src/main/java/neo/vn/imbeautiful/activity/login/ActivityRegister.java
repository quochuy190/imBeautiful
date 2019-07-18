package neo.vn.imbeautiful.activity.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

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
import neo.vn.imbeautiful.models.ObjLogin;
import neo.vn.imbeautiful.untils.KeyboardUtil;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 23-April-2019
 * Time: 13:33
 * Version: 1.0
 */
public class ActivityRegister extends BaseActivity
        implements View.OnClickListener, InterfaceLogin.View {
    @BindView(R.id.img_done)
    ImageView img_done;
    @BindView(R.id.edt_fullname_register)
    EditText edt_fullname;
    @BindView(R.id.edt_email_register)
    EditText edt_email;
    @BindView(R.id.edt_phone_register)
    EditText edt_phone;
    @BindView(R.id.edt_address_register)
    EditText edt_address;
    @BindView(R.id.edt_city_register)
    EditText edt_city;
    @BindView(R.id.edt_district_register)
    EditText edt_district;
    @BindView(R.id.edt_pass_register)
    EditText edt_pass;
    @BindView(R.id.edt_pass_confirm_register)
    EditText edt_pass_confirm;
    @BindView(R.id.txt_change_login)
    TextView txt_change_login;
    City objCity;
    Districts objDistrict;
    PresenterLogin mPresenter;

    @Override
    public int setContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterLogin(this);
        initEvent();
    }

    private void initEvent() {
       /* img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityRegister.this, ActivityConfirmOTP.class));
            }
        });*/
        img_done.setOnClickListener(this);
        edt_city.setOnClickListener(this);
        edt_district.setOnClickListener(this);
        txt_change_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_city_register:
                App.mCity = null;
                App.mDistrict = null;
                objCity = null;
                Intent intent_city = new Intent(ActivityRegister.this, ActivityListCity.class);
                startActivityForResult(intent_city, Constants.RequestCode.GET_CITY);
                break;
            case R.id.edt_district_register:
                if (objCity != null) {
                    App.mDistrict = null;
                    Intent intent_district = new Intent(ActivityRegister.this, ActivityDistrict.class);
                    intent_district.putExtra(Constants.KEY_SEND_ID_CITY, objCity.getMATP());
                    startActivityForResult(intent_district, Constants.RequestCode.GET_DISTRICT);
                } else
                    showDialogNotify("Thông báo", "Bạn chưa chọn tỉnh thành phố nào.");
                break;
            case R.id.img_done:
                login_api();
                break;
            case R.id.txt_change_login:
                start_activity();
                break;
        }
    }

    private void start_activity() {
        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }

    private void login_api() {
        if (edt_fullname.getText().toString().length() == 0) {
            showDialogNotify("Thông báo", "Mời bạn nhập vào họ và tên.");
            KeyboardUtil.requestKeyboard(edt_fullname);
            return;
        }
        if (edt_phone.getText().toString().length() == 0) {
            showDialogNotify("Thông báo", "Mời bạn nhập vào số điện thoại.");
            KeyboardUtil.requestKeyboard(edt_phone);
            return;
        }
        if (edt_email.getText().toString().length() == 0) {
            showDialogNotify("Thông báo", "Mời bạn nhập vào email của bạn.");
            KeyboardUtil.requestKeyboard(edt_email);
            return;
        }
        if (objCity == null) {
            showDialogNotify("Thông báo", "Bạn chưa chọn tỉnh thành phố.");
            return;
        }
        if (objDistrict == null) {
            showDialogNotify("Thông báo", "Bạn chưa chọn quận huyện.");
            return;
        }
        if (edt_address.getText().toString().length() == 0) {
            showDialogNotify("Thông báo", "Mời bạn nhập vào địa chỉ của bạn.");
            KeyboardUtil.requestKeyboard(edt_address);
            return;
        }
        if (edt_pass.getText().toString().length() < 6) {
            showDialogNotify("Thông báo", "Mật khẩu phải dài hơn 6 ký tự.");
            KeyboardUtil.requestKeyboard(edt_pass);
            return;
        }
        if (edt_pass_confirm.getText().toString().length() == 0) {
            showDialogNotify("Thông báo", "Mật khẩu phải dài hơn 6 ký tự.");
            KeyboardUtil.requestKeyboard(edt_pass_confirm);
            return;
        }
        if (!edt_pass.getText().toString().equals(edt_pass_confirm.getText().toString())) {
            showDialogNotify("Thông báo", "Xác nhận mật khẩu không chính xác.");
            return;
        }
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        // phoneLogin(edt_otp_code);
        if (accessToken != null) {
            goToMyLoggedInActivity();
            //Handle Returning User
        } else {
            //Handle new or logged out user
            phoneLogin(null);
        }
    }

    private void goToMyLoggedInActivity() {
        showDialogLoading();
        mPresenter.api_register(edt_fullname.getText().toString(), edt_phone.getText().toString(), edt_email.getText().toString(),
                objCity.getMATP(), objDistrict.getMAQH(), edt_address.getText().toString(), edt_pass.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.GET_CITY:
                if (App.mCity != null) {
                    objCity = App.mCity;
                    edt_city.setText(App.mCity.getNAME());
                } else {
                    edt_city.setText("");
                    edt_district.setText("");
                }

                break;
            case Constants.RequestCode.GET_DISTRICT:
                if (App.mDistrict != null) {
                    objDistrict = App.mDistrict;
                    edt_district.setText(App.mDistrict.getNAME());
                } else
                    edt_district.setText("");
                break;
        }
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                //   showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }
                goToMyLoggedInActivity();
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...

            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    "Success!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
    }

    @Override
    public void show_login(ObjLogin obj) {
        hideDialogLoading();
    }

    @Override
    public void show_register(ErrorApi obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_USERNAME, edt_phone.getText().toString());
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_PASSWORD, edt_pass.getText().toString());
            Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
            intent.putExtra(Constants.KEY_SEND_IS_REGISTER, true);
            startActivity(intent);
            finish();
        } else
            showDialogNotify("Thông báo", obj.getsRESULT());
    }

    @Override
    public void show_update_device(ErrorApi obj) {

    }

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE);
        // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public static int APP_REQUEST_CODE = 99;

}
