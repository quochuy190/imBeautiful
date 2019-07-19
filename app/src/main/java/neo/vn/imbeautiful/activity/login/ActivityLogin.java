package neo.vn.imbeautiful.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.BuildConfig;
import neo.vn.imbeautiful.MainActivity;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.ObjLogin;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 23-April-2019
 * Time: 08:39
 * Version: 1.0
 */
public class ActivityLogin extends BaseActivity implements View.OnClickListener, InterfaceLogin.View {
    private static final String TAG = "ActivityLogin";
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.edt_pass)
    EditText edt_pass;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.txt_register)
    TextView txt_register;
    @BindView(R.id.txt_remember_pass)
    TextView txt_remember_pass;
    @BindView(R.id.img_showpass)
    ImageView img_showpass;
    PresenterLogin mPresenter;
    String sUser, sPass;
    String UUID;

    @Override
    public int setContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterLogin(this);
        UUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        get_token_firebase();
        initData();
        initEvent();
    }

    private void initData() {
        boolean isRegister = getIntent().getBooleanExtra(Constants.KEY_SEND_IS_REGISTER, false);
        if (isRegister) {
            sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
            sPass = SharedPrefs.getInstance().get(Constants.KEY_SAVE_PASSWORD, String.class);
            showDialogLoading();
            mPresenter.api_login(sUser, sPass);
        } else {
            sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
            sPass = SharedPrefs.getInstance().get(Constants.KEY_SAVE_PASSWORD, String.class);
            edt_username.setText(sUser);
            edt_pass.setText(sPass);
        }
    }

    private void initEvent() {
        btn_login.setOnClickListener(this);
        img_showpass.setOnClickListener(this);
        txt_register.setOnClickListener(this);
    }

    private void start_activity() {
        Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.img_showpass:
                show_pass();
                break;
            case R.id.txt_register:
                start_activity();
                break;
        }
    }

    boolean isShowpass = true;

    private void show_pass() {
        if (!isShowpass) {
            img_showpass.setImageDrawable(getResources().getDrawable(R.drawable.icon_hide_pass));
            //Glide.with(ActivityLogin.this).load(R.drawable.ic_eye_hide).into(img_showpass);
            edt_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isShowpass = !isShowpass;
        } else {
            img_showpass.setImageDrawable(getResources().getDrawable(R.drawable.icon_show_pass));
            edt_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isShowpass = !isShowpass;
        }
    }
    private void get_token_firebase() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);
                    }
                });
    }

    private void login() {
        if (edt_username.getText().toString().length() > 0) {
            sUser = edt_username.getText().toString();
        } else {
            showAlertDialog("Thông báo", "Bạn chưa nhập vào tên đăng nhập.");
            return;
        }
        if (edt_pass.getText().toString().length() > 0) {
            sPass = edt_pass.getText().toString();
        } else {
            showAlertDialog("Thông báo", "Bạn chưa nhập vào mật khẩu.");
            return;
        }
        showDialogLoading();
        mPresenter.api_login(sUser, sPass);
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_login(ObjLogin obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            App.isLoginHome = true;
            String sTokenKey = SharedPrefs.getInstance().get(Constants.KEY_TOKEN, String.class);
            mPresenter.api_update_device(sUser, BuildConfig.VERSION_NAME, android.os.Build.BRAND + " "
                    + android.os.Build.MODEL, sTokenKey, "1", android.os.Build.VERSION.RELEASE, UUID);
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_IS_LOGIN, true);
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_USER_LOGIN, obj);
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_USERNAME, obj.getUSERNAME());
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_PASSWORD, obj.getPASSWORD());
         /*   Intent intent = new Intent(ActivityLogin.this, ActivityConfirmOTP.class);
            startActivity(intent);*/
            //    finish();
            goToMyLoggedInActivity();

        } else
            showAlertDialog("Thông báo", obj.getsRESULT());
    }

    @Override
    public void show_register(ErrorApi obj) {

    }

    @Override
    public void show_update_device(ErrorApi obj) {

    }
    private void goToMyLoggedInActivity() {
        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
