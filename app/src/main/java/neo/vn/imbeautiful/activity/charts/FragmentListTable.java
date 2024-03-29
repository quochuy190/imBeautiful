package neo.vn.imbeautiful.activity.charts;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.adapter.AdapterReportProduct;
import neo.vn.imbeautiful.base.BaseFragment;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.models.ReportProduct;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 22-April-2019
 * Time: 10:30
 * Version: 1.0
 */
public class FragmentListTable extends BaseFragment {
    private static final String TAG = "FragmentSetup";
    public static FragmentListTable fragment;
    private List<ReportProduct> mList;
    private AdapterReportProduct adapterService;
    @BindView(R.id.rcv_list_table)
    RecyclerView rcv_list_table;
    RecyclerView.LayoutManager mLayoutManager;

    public static FragmentListTable getInstance() {
        if (fragment == null) {
            synchronized (FragmentListTable.class) {
                if (fragment == null)
                    fragment = new FragmentListTable();
            }
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
        ButterKnife.bind(this, view);
        Log.e(TAG, "onCreateView: Setup");
        init();
        initData();
        return view;
    }

    private void initData() {
        mList.clear();
        if (App.mLisReportProduct.size() > 0)
            mList.addAll(App.mLisReportProduct);
        adapterService.notifyDataSetChanged();
    }

    private void init() {
        mList = new ArrayList<>();
        adapterService = new AdapterReportProduct(mList, getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        rcv_list_table.setNestedScrollingEnabled(false);
        rcv_list_table.setHasFixedSize(true);
        rcv_list_table.setLayoutManager(mLayoutManager);
        rcv_list_table.setItemAnimator(new DefaultItemAnimator());
        rcv_list_table.setAdapter(adapterService);
        adapterService.updateList(mList);
        adapterService.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {

            }
        });

    }

}
