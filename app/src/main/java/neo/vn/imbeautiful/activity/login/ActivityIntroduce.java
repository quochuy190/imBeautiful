package neo.vn.imbeautiful.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseActivity;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 23-April-2019
 * Time: 10:03
 * Version: 1.0
 */
public class ActivityIntroduce extends BaseActivity {
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    public int setContentViewId() {
        return R.layout.activity_introduce;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    private void initEvent() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityIntroduce.this, ActivityRegister.class));
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityIntroduce.this, ActivityLogin.class));
                finish();
            }
        });
    }
}
