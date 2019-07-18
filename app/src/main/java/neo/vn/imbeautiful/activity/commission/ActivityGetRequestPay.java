package neo.vn.imbeautiful.activity.commission;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterRequestPay;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.ObjCommissions;
import neo.vn.imbeautiful.models.respon_api.ResponGetCommission;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 17-June-2019
 * Time: 15:58
 * Version: 1.0
 */
public class ActivityGetRequestPay extends BaseActivity implements InterfaceCommission.View {
    private List<ObjCommissions> mList;
    private AdapterRequestPay adapter;
    @BindView(R.id.rcv_list_report_pay)
    RecyclerView recycleview;
    RecyclerView.LayoutManager mLayoutManager;
    PresenterCommission mPresenter;

    @Override
    public int setContentViewId() {
        return R.layout.activity_recycleview_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterCommission(this);
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
        txt_title.setText("Yêu cầu rút hoa hồng");
    }
    private void initData() {
        showDialogLoading();
        String sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        mPresenter.api_get_withdrawal(sUserName, "1", "30");
    }

    private void init() {
        mList = new ArrayList<>();
        adapter = new AdapterRequestPay(mList, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleview.setNestedScrollingEnabled(false);
        recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(mLayoutManager);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setAdapter(adapter);
        adapter.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                Intent intent = new Intent(ActivityGetRequestPay.this, ActivityHhDetail.class);
                ObjCommissions obj = (ObjCommissions) item;
                intent.putExtra(Constants.KEY_SEND_OBJ_COMMISSION, obj);
                startActivity(intent);
            }
        });
    }

    @Override
    public void show_error_api() {
        hideDialogLoading();
    }

    @Override
    public void show_get_commission(ResponGetCommission obj) {

    }

    @Override
    public void show_get_withdrawal(ResponGetCommission obj) {
        hideDialogLoading();
        if (obj.getsERROR().equals("0000")) {
            if (obj.getmList() != null) {
                mList.clear();
                mList.addAll(obj.getmList());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void show_get_withdrawal_history(ResponGetCommission obj) {

    }

    @Override
    public void show_get_request_withdrawal(ErrorApi obj) {

    }

    @Override
    public void show_update_comission(ErrorApi obj) {

    }

    @Override
    public void show_update_withdrawal(ErrorApi obj) {

    }
}
