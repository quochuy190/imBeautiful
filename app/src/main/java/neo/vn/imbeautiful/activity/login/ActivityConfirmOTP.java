package neo.vn.imbeautiful.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import butterknife.BindView;
import neo.vn.imbeautiful.MainActivity;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseActivity;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 23-April-2019
 * Time: 14:40
 * Version: 1.0
 */
public class ActivityConfirmOTP extends BaseActivity {
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_otp_code)
    EditText edt_otp_code;


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
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityConfirmOTP.this, MainActivity.class));
            }
        });
    }




}
