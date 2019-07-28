package neo.vn.imbeautiful.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.untils.PhoneNumberUntil;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 23-April-2019
 * Time: 14:40
 * Version: 1.0
 */
public class ActivityConfirmOTP extends BaseActivity {
    private static final String TAG = "ActivityConfirmOTP";
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_otp_code)
    EditText edt_otp_code;
    @BindView(R.id.txt_register)
    TextView txt_register;


    @Override
    public int setContentViewId() {
        return R.layout.activity_confirm_otp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        // phoneLogin(edt_otp_code);
        if (accessToken != null) {

            //Handle Returning User
        } else {
            //Handle new or logged out user
        }
    }

    private void initEvent() {
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityConfirmOTP.this, ActivityLogin.class));
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   startActivity(new Intent(ActivityConfirmOTP.this, MainActivity.class));
                if (edt_otp_code.getText().toString().length() > 0) {
                    phoneLogin();
                } else {
                    showDialogNotify("Thông báo", "Bạn chưa nhập vào số điện thoại.");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    getAccount();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                    getAccount();
                }

                //   goToMyLoggedInActivity();
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...

            }

            // Surface the result to your user in an appropriate way.
        }
    }

    public void phoneLogin() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        PhoneNumber phoneNumber = new PhoneNumber("+84",
                "" + edt_otp_code.getText().toString(), "VN");
// country code, phone number, ISO country code(like "BD", "US")
        configurationBuilder.setInitialPhoneNumber(phoneNumber);

        // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());

        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    String sPhone;

    private void getAccount() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (phoneNumber != null) {
                    String phoneNumberString = PhoneNumberUntil.convertToVnPhoneNumber(phoneNumber.toString());
                    sPhone = PhoneNumberUntil.convertToVnPhoneNumber(edt_otp_code.getText().toString());
                    if (phoneNumberString.equals(sPhone)) {
                        Intent intent = new Intent(ActivityConfirmOTP.this, ActivityRegister.class);
                        intent.putExtra(Constants.KEY_SEND_PHONE_REGISTER, sPhone);
                        startActivity(intent);
                        finish();
                    } else {
                        showAlertDialog("Thông báo", "Số điện thoại xác nhận không trùng với số điện thoại bạn đăng ký");
                    }
                }

                // Get email
                String email = account.getEmail();
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e(TAG, "onError: " + error);
                // Handle Error
            }
        });
    }

    public static int APP_REQUEST_CODE = 99;


}
