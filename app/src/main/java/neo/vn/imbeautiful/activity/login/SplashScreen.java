package neo.vn.imbeautiful.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import neo.vn.imbeautiful.MainActivity;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.untils.SharedPrefs;

public class SplashScreen extends BaseActivity {
    private static final String TAG = "SplashScreen";

    ImageView img_splash;
    // public static Storage storage; // this Preference comes for free from the library
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    Intent mainIntent = new Intent();
    Intent mainIntent_welcom = new Intent();
    String id;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        boolean isLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_IS_LOGIN, Boolean.class);
        img_splash = (ImageView) findViewById(R.id.img_logo);
        Glide.with(this).load(R.drawable.logo).into(img_splash);
        if (isLogin) {
            mainIntent.setClass(SplashScreen.this, MainActivity.class);
        } else {
            mainIntent.setClass(SplashScreen.this, ActivityIntroduce.class);
        }
        start_activity();
    }

    private String sTokenKey;


    private void start_activity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_flash;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
}