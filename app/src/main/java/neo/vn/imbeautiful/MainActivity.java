package neo.vn.imbeautiful;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import neo.vn.imbeautiful.activity.login.ActivityIntroduce;
import neo.vn.imbeautiful.activity.login.InterfaceLogin;
import neo.vn.imbeautiful.activity.login.PresenterLogin;
import neo.vn.imbeautiful.activity.products.ActivityCart;
import neo.vn.imbeautiful.adapter.AdapterViewpager;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ClickDialog;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.fragment.FragmentCommissionsHome;
import neo.vn.imbeautiful.fragment.FragmentHome;
import neo.vn.imbeautiful.fragment.FragmentOrder;
import neo.vn.imbeautiful.fragment.FragmentProduct;
import neo.vn.imbeautiful.fragment.left_menu.ctv.FragmentLeftMenu;
import neo.vn.imbeautiful.fragment.products.FragmentCommissionCTV;
import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.MessageEvent;
import neo.vn.imbeautiful.models.ObjLogin;
import neo.vn.imbeautiful.untils.SharedPrefs;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, InterfaceLogin.View {
    private static final String TAG = "MainActivity";
    /*    @BindView(R.id.vp_home)
        ViewPager viewPager;
        @BindView(R.id.tablayout_toolbar)
        TabLayout customTabLayout;*/
    AdapterViewpager adapter;
    MenuItem prevMenuItem;
    @BindView(R.id.frame_leftmenu)
    FrameLayout frame_leftmenu;
    @BindView(R.id.img_nav_right)
    ImageView img_nav_right;
    @BindView(R.id.txt_name_nav)
    TextView txt_name_nav;
    public static DrawerLayout drawer;
    public static Toolbar toolbar;
    private PresenterLogin mPresenterLogin;
    public static EditText edt_search_main;
    public static TextView txt_title_main;
    String UUID;
    FragmentHome fragmentHome;
    FragmentOrder fragmentOrder;
    FragmentProduct fragmentProduct;
    FragmentCommissionsHome fragmentCommissions;
    FragmentCommissionCTV fragmentCommissions_CTV;
    Fragment fragmentCurrent;
    ObjLogin objLogin;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edt_search_main = findViewById(R.id.edt_search_main);
        txt_title_main = findViewById(R.id.txt_title_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer_layout);
        mPresenterLogin = new PresenterLogin(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        UUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        navigation = (BottomNavigationView) findViewById(R.id.nav_bottom_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initData();
        if (App.isLoginHome) {
            loadFragmentHome();
            start_left_menu();
            //  navigation.setSelectedItemId(R.id.navigation_order);
        } else {
            initLogin();
        }

        //setupViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.isLoginHome = false;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.message.equals("home")) {
            if (fragmentOrder != null && fragmentOrder.isAdded())
                EventBus.getDefault().post(new MessageEvent("load_order",
                        Float.parseFloat("1"), 0));
            else
                App.isLoadOrder = true;
            navigation.setSelectedItemId(R.id.navigation_order);
        } else if (event.message.equals("product")) {
            navigation.setSelectedItemId(R.id.navigation_product);
        }
    }

    private ObjLogin mObjLogin;

    private void initData() {
        mObjLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
        if (mObjLogin != null && mObjLogin.getGROUPS() != null & mObjLogin.getGROUPS().equals("5")) {
            txt_name_nav.setText(mObjLogin.getFULL_NAME());
            img_nav_right.setImageResource(R.drawable.ic_setup);
        } else {
            img_nav_right.setImageResource(R.drawable.ic_right);
            txt_name_nav.setText(mObjLogin.getFULL_NAME());
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainActivity.edt_search_main.setVisibility(View.VISIBLE);
                    MainActivity.txt_title_main.setVisibility(View.GONE);
                    //  toolbar.setTitle("Shop");
                    //fragment = FragmentHome.getInstance();
                    loadFragmentHome();
                    return true;
                case R.id.navigation_order:
                    MainActivity.edt_search_main.setVisibility(View.VISIBLE);
                    MainActivity.txt_title_main.setVisibility(View.GONE);
                    loadFragmentOrder();
                    // toolbar.setTitle("My Gifts");
              /*      fragment = FragmentOrder.getInstance();
                    loadFragment(fragment);*/
                    return true;
                case R.id.navigation_product:
                    MainActivity.edt_search_main.setVisibility(View.VISIBLE);
                    MainActivity.txt_title_main.setVisibility(View.GONE);
                    loadFragmentProduct();
                    // toolbar.setTitle("Cart");
               /*     fragment = FragmentProduct.getInstance();
                    loadFragment(fragment);*/
                    return true;
                case R.id.navigation_bonus:
                    if (objLogin != null && objLogin.getGROUPS() != null && objLogin.getGROUPS().equals("5")) {
                        MainActivity.edt_search_main.setVisibility(View.GONE);
                        MainActivity.txt_title_main.setVisibility(View.VISIBLE);
                        MainActivity.txt_title_main.setText("Hoa hồng");
                        loadFragment_HH_CTV();
                    } else if (objLogin != null && objLogin.getGROUPS() != null && objLogin.getGROUPS().equals("3")) {
                        MainActivity.edt_search_main.setVisibility(View.GONE);
                        MainActivity.txt_title_main.setVisibility(View.VISIBLE);
                        MainActivity.txt_title_main.setText("Hoa hồng");
                        loadFragmentSetup();
                    }
                    return true;
            }
            return false;
        }
    };
/*
    public static void setback_toolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
    }*/

 /*   private void loadFragmentHome() {
        Fragment fragment = FragmentMainActivity.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_home_main, fragment);
        transaction.commit();
    }*/

    private void start_left_menu() {
        Fragment fragmentleft = FragmentLeftMenu.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_leftmenu, fragmentleft);
        transaction.commit();
    }

    public static void close_drawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //FragmentUtil.popBackStack(this);
            super.onBackPressed();
        }
    }

    String sUser, sPass;

    private void initLogin() {
        sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        sPass = SharedPrefs.getInstance().get(Constants.KEY_SAVE_PASSWORD, String.class);
        if (sUser != null && sPass != null) {
            showDialogLoading();
            mPresenterLogin.api_login(sUser, sPass);
        }
    /*    if (chil != null && chil.getsObjInfoKid() != null) {
            InfoKids obj = chil.getsObjInfoKid();
            mPresenter.api_get_list_sticker(sUserMe, obj.getsLEVEL_ID());
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        itemToHide = menu.findItem(R.id.action_settings);
        item_notify = menu.findItem(R.id.action_notify);
        if (mObjLogin != null && mObjLogin.getGROUPS() != null) {
            if (mObjLogin.getGROUPS().equals("5")) {
                itemToHide.setVisible(true);
                item_notify.setVisible(false);
            } else {
                itemToHide.setVisible(false);
                item_notify.setVisible(true);
            }

        }
        return true;
    }

    private MenuItem itemToHide, item_notify;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, ActivityCart.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
        showAlertErrorNetwork();
    }

    @Override
    public void show_login(ObjLogin obj) {
        hideDialogLoading_delay();
        if (obj != null && obj.getsERROR() != null && obj.getsERROR().equals("0000")) {
            //loadFragmentHome();
            start_left_menu();
            loadFragmentHome();
            App.isLoginHome = true;
            String sTokenKey = SharedPrefs.getInstance().get(Constants.KEY_TOKEN, String.class);
            mPresenterLogin.api_update_device(sUser, BuildConfig.VERSION_NAME,
                    android.os.Build.BRAND + " "
                            + android.os.Build.MODEL, sTokenKey, "1", android.os.Build.VERSION.RELEASE, UUID);
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_IS_LOGIN, true);
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_USER_LOGIN, obj);
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_USERNAME, obj.getUSERNAME());
            SharedPrefs.getInstance().put(Constants.KEY_SAVE_PASSWORD, obj.getPASSWORD());
        } else if (obj != null && obj.getsRESULT() != null) {
            showDialogComfirm("Thông báo", obj.getsRESULT(), false, new ClickDialog() {
                @Override
                public void onClickYesDialog() {
                    SharedPrefs.getInstance().put(Constants.KEY_SAVE_LIST_CART, null);
                    SharedPrefs.getInstance().put(Constants.KEY_SAVE_IS_LOGIN, false);
                    Intent intent = new Intent(MainActivity.this, ActivityIntroduce.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onClickNoDialog() {

                }
            });
            // showDialogNotify("Thông báo", obj.getsRESULT());
        }
    }

    @Override
    public void show_register(ErrorApi obj) {

    }

    @Override
    public void show_update_device(ErrorApi obj) {

    }

    private void loadFragmentHome() {
        objLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
        Log.i(TAG, "loadFragmentHome: " + objLogin.getSTATUS());
        fragmentHome = (FragmentHome) getSupportFragmentManager().findFragmentByTag(FragmentHome.class.getName());

        if (fragmentHome == null) {
            fragmentHome = FragmentHome.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentHome.isAdded()) {
            transaction.add(R.id.frame_home_fragment, fragmentHome, FragmentHome.class.getName());
        } else {
            //  transaction.hide(fragmentCurrent);
            if (fragmentProduct != null && fragmentProduct.isAdded()) {
                transaction.hide(fragmentProduct);
            }
            if (fragmentOrder != null && fragmentOrder.isAdded()) {
                transaction.hide(fragmentOrder);
            }
            if (fragmentCommissions != null && fragmentCommissions.isAdded()) {
                transaction.hide(fragmentCommissions);
            }
            if (fragmentCommissions_CTV != null && fragmentCommissions_CTV.isAdded()) {
                transaction.hide(fragmentCommissions_CTV);
            }
            transaction.show(fragmentHome);
        }
        //   fragmentCurrent = fragmentHome;
        transaction.commit();
    }

    private void loadFragmentOrder() {
        fragmentOrder = (FragmentOrder) getSupportFragmentManager().findFragmentByTag(FragmentOrder.class.getName());
        if (fragmentOrder == null) {
            fragmentOrder = FragmentOrder.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentOrder.isAdded()) {
            transaction.add(R.id.frame_home_fragment, fragmentOrder, FragmentOrder.class.getName());
        } else {
            if (fragmentProduct != null && fragmentProduct.isAdded()) {
                transaction.hide(fragmentProduct);
            }
            if (fragmentHome != null && fragmentHome.isAdded()) {
                transaction.hide(fragmentHome);
            }
            if (fragmentCommissions != null && fragmentCommissions.isAdded()) {
                transaction.hide(fragmentCommissions);
            }
            if (fragmentCommissions_CTV != null && fragmentCommissions_CTV.isAdded()) {
                transaction.hide(fragmentCommissions_CTV);
            }
            transaction.show(fragmentOrder);
        }
        //  fragmentCurrent = fragmentOrder;
        transaction.commit();
    }

    private void loadFragmentProduct() {
        fragmentProduct = (FragmentProduct) getSupportFragmentManager().findFragmentByTag(FragmentProduct.class.getName());
        if (fragmentProduct == null) {
            fragmentProduct = FragmentProduct.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentProduct.isAdded()) {
            transaction.add(R.id.frame_home_fragment, fragmentProduct, FragmentProduct.class.getName());
        } else {
            if (fragmentOrder != null && fragmentOrder.isAdded()) {
                transaction.hide(fragmentOrder);
            }
            if (fragmentHome != null && fragmentHome.isAdded()) {
                transaction.hide(fragmentHome);
            }
            if (fragmentCommissions != null && fragmentCommissions.isAdded()) {
                transaction.hide(fragmentCommissions);
            }
            if (fragmentCommissions_CTV != null && fragmentCommissions_CTV.isAdded()) {
                transaction.hide(fragmentCommissions_CTV);
            }
            //   transaction.hide(fragmentCurrent);
            transaction.show(fragmentProduct);
        }
        // fragmentCurrent = fragmentProduct;
        transaction.commit();
    }

    private void loadFragmentSetup() {
        fragmentCommissions = (FragmentCommissionsHome) getSupportFragmentManager().findFragmentByTag(FragmentCommissionsHome.class.getName());
        if (fragmentCommissions == null) {
            fragmentCommissions = FragmentCommissionsHome.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentCommissions.isAdded()) {
            transaction.add(R.id.frame_home_fragment, fragmentCommissions, FragmentCommissionsHome.class.getName());
        } else {
            if (fragmentProduct != null && fragmentProduct.isAdded()) {
                transaction.hide(fragmentProduct);
            }
            if (fragmentHome != null && fragmentHome.isAdded()) {
                transaction.hide(fragmentHome);
            }
            if (fragmentOrder != null && fragmentOrder.isAdded()) {
                transaction.hide(fragmentOrder);
            }
            if (fragmentCommissions_CTV != null && fragmentCommissions_CTV.isAdded()) {
                transaction.hide(fragmentCommissions_CTV);
            }
            //  transaction.hide(fragmentCurrent);
            transaction.show(fragmentCommissions);
        }
        //   fragmentCurrent = fragmentCommissions;
        transaction.commit();
    }

    private void loadFragment_HH_CTV() {
        fragmentCommissions_CTV = (FragmentCommissionCTV) getSupportFragmentManager().findFragmentByTag(FragmentCommissionCTV.class.getName());
        if (fragmentCommissions_CTV == null) {
            fragmentCommissions_CTV = FragmentCommissionCTV.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentCommissions_CTV.isAdded()) {
            transaction.add(R.id.frame_home_fragment, fragmentCommissions_CTV, FragmentCommissionCTV.class.getName());
        } else {
            if (fragmentProduct != null && fragmentProduct.isAdded()) {
                transaction.hide(fragmentProduct);
            }
            if (fragmentHome != null && fragmentHome.isAdded()) {
                transaction.hide(fragmentHome);
            }
            if (fragmentOrder != null && fragmentOrder.isAdded()) {
                transaction.hide(fragmentOrder);
            }
            if (fragmentCommissions != null && fragmentCommissions.isAdded()) {
                transaction.hide(fragmentCommissions);
            }
            // transaction.hide(fragmentCurrent);
            transaction.show(fragmentCommissions_CTV);
        }
        //  fragmentCurrent = fragmentCommissions_CTV;
        transaction.commit();
    }
}
