package neo.vn.imbeautiful.fragment.left_menu.ctv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.MainActivity;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.activity.charts.ActivityChartMenu;
import neo.vn.imbeautiful.activity.charts.ActivityReportCTVDetail;
import neo.vn.imbeautiful.activity.collaborators.FragmentListCTV;
import neo.vn.imbeautiful.activity.login.ActivityIntroduce;
import neo.vn.imbeautiful.activity.login.Activity_Webview;
import neo.vn.imbeautiful.activity.notify.ActivityNotify;
import neo.vn.imbeautiful.adapter.AdapterLeftMenu;
import neo.vn.imbeautiful.base.BaseFragment;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.ObjLeftMenu;
import neo.vn.imbeautiful.models.ObjLogin;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 22-April-2019
 * Time: 10:30
 * Version: 1.0
 */
public class FragmentLeftMenu extends BaseFragment {
    private static final String TAG = "FragmentLeftMenu";
    public static FragmentLeftMenu fragment;
    private List<ObjLeftMenu> mList;
    private AdapterLeftMenu adapterService;
    @BindView(R.id.recycle_left_menu)
    RecyclerView recycle_product;
    @BindView(R.id.txt_logout)
    TextView txt_logout;
    RecyclerView.LayoutManager mLayoutManager;

    public static FragmentLeftMenu getInstance() {
        if (fragment == null) {
            synchronized (FragmentLeftMenu.class) {
                if (fragment == null)
                    fragment = new FragmentLeftMenu();
            }
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_menu, container, false);
        ButterKnife.bind(this, view);
        Log.e(TAG, "onCreateView: Setup");
        init();
        initData();
        initEvent();
        return view;
    }

    private void initEvent() {
        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.getInstance().put(Constants.KEY_SAVE_LIST_CART, null);
                SharedPrefs.getInstance().put(Constants.KEY_SAVE_IS_LOGIN, false);
                Intent intent = new Intent(getContext(), ActivityIntroduce.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private ObjLogin objLogin;

    private void initData() {
        objLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
        if (objLogin != null && objLogin.getGROUPS().equals("5")) {

        } else if (objLogin != null) {
            mList.add(new ObjLeftMenu("Thông tin CTV", R.drawable.ic_groups, 1));
        }
        mList.add(new ObjLeftMenu("Báo cáo", R.drawable.ic_left_menu_info_chart, 2));
        mList.add(new ObjLeftMenu("Chính sách", R.drawable.ic_left_menu_info_policy, 3));
        mList.add(new ObjLeftMenu("Đào tạo", R.drawable.ic_left_menu_traning, 4));
        mList.add(new ObjLeftMenu("Tin tức sự kiện", R.drawable.ic_left_menu_notify, 5));
        mList.add(new ObjLeftMenu("Thông báo", R.drawable.ic_left_menu_thongbao, 6));
        mList.add(new ObjLeftMenu("Giới thiệu I'm Beautiful Shop", R.drawable.ic_left_menu_introduct, 7));
        mList.add(new ObjLeftMenu("Duyệt bài", R.drawable.ic_left_menu_dyetbai, 8));
        adapterService.notifyDataSetChanged();
    }

    private void init() {
        mList = new ArrayList<>();
        adapterService = new AdapterLeftMenu(mList, getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recycle_product.setNestedScrollingEnabled(false);
        recycle_product.setHasFixedSize(true);
        recycle_product.setLayoutManager(mLayoutManager);
        recycle_product.setItemAnimator(new DefaultItemAnimator());
        recycle_product.setAdapter(adapterService);
      /*  adapterService.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                setResult(RESULT_OK, new Intent());
                App.mCity = (City) item;
            }
        });*/
        adapterService.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                ObjLeftMenu obj = (ObjLeftMenu) item;
                set_click(obj.getId());
            }
        });

    }

    ObjLogin mLogin;

    private void set_click(int position) {
        mLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
        Intent intent = null;
        switch (position) {
            case 1:
                intent = new Intent(getActivity(), FragmentListCTV.class);
                //  MainActivity.setback_toolbar();
                break;
            case 2:
                if (mLogin.getGROUPS().equals("5")) {
                    intent = new Intent(getActivity(), ActivityReportCTVDetail.class);
                } else {
                    intent = new Intent(getActivity(), ActivityChartMenu.class);
                }

                //  MainActivity.setback_toolbar();
                break;
            case 6:
                intent = new Intent(getActivity(), ActivityNotify.class);
                //  MainActivity.setback_toolbar();
                break;
            case 3:
                intent = new Intent(getActivity(), Activity_Webview.class);
                intent.putExtra(Constants.KEY_SEND_WEBVIEW_TITLE, "Chính sách");
                intent.putExtra(Constants.KEY_SEND_WEBVIEW_OPTION, "https://imbeautiful.vn/chinh-sach");
                //  MainActivity.setback_toolbar();
                break;
            case 7:
                intent = new Intent(getActivity(), Activity_Webview.class);
                intent.putExtra(Constants.KEY_SEND_WEBVIEW_TITLE, "Giới thiệu");
                intent.putExtra(Constants.KEY_SEND_WEBVIEW_OPTION, "https://imbeautiful.vn/");
                //  MainActivity.setback_toolbar();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        } else {

        }
        MainActivity.close_drawer();
    }

}
