package neo.vn.imbeautiful.activity.notify;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterListNotify;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.ObjNotify;
import neo.vn.imbeautiful.models.respon_api.ResponGetnotify;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 24-June-2019
 * Time: 16:19
 * Version: 1.0
 */
public class ActivityNotify extends BaseActivity implements InterfaceNotify.View {
    private List<ObjNotify> mList;
    private AdapterListNotify adapter;
    @BindView(R.id.rcv_list_report_pay)
    RecyclerView recycle_service;
    RecyclerView.LayoutManager mLayoutManager;
    private PresenterNotify mPresenter;
    int page = 1;
    int number = 30;

    @Override
    public int setContentViewId() {
        return R.layout.activity_recycleview_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterNotify(this);
        initAppbar();
        init();
        initData();
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
        txt_title.setText("Thông báo");
    }

    private void initData() {
        showDialogLoading();
        String sUser = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        mPresenter.api_get_list_notify(sUser, "" + page, "" + number);
    }

    private void init() {
        mList = new ArrayList<>();
        adapter = new AdapterListNotify(mList, this);
        mLayoutManager = new GridLayoutManager(this, 1);
        recycle_service.setNestedScrollingEnabled(false);
        recycle_service.setHasFixedSize(true);
        recycle_service.setLayoutManager(mLayoutManager);
        recycle_service.setItemAnimator(new DefaultItemAnimator());
        recycle_service.setAdapter(adapter);

        adapter.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {

            }
        });

    }

    @Override
    public void show_error_api() {

    }

    @Override
    public void show_get_notify(ResponGetnotify obj) {
        hideDialogLoading();
        if (obj != null && obj.getsERROR() != null && obj.getsERROR().equals("0000")) {

        } else if (obj != null && obj.getsRESULT() != null) {
            showDialogNotify("Thông báo", obj.getsRESULT());
        }
    }
}
