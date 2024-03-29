package neo.vn.imbeautiful.activity.charts;

import android.os.Bundle;

import com.anychart.AnyChartView;

import java.util.Calendar;

import butterknife.BindView;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseActivity;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.respon_api.ResponGetReportDefault;
import neo.vn.imbeautiful.models.respon_api.ResponGetReportListCTV;
import neo.vn.imbeautiful.models.respon_api.ResponGetReportProduct;
import neo.vn.imbeautiful.untils.SharedPrefs;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 05-August-2019
 * Time: 09:12
 * Version: 1.0
 */
public class Activity_Fluctuations extends BaseActivity implements InterfaceReport.View {
    @BindView(R.id.chart_line_donhang)
    AnyChartView chartView;
    PresenterReport mPresenter;
    @Override
    public int setContentViewId() {
        return R.layout.activity_fluctuations;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PresenterReport(this);
        initData();

    }
    private String sUserName;
    private int page = 1, number = 30;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    private void initData() {
        sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
        mPresenter.api_get_report_fluctuations(sUserName,  "" + year,
                "", "" , "1" , "1");
    }

    @Override
    public void show_error_api() {

    }

    @Override
    public void show_report_item(ResponGetReportProduct objRespon) {

    }

    @Override
    public void show_report_default(ResponGetReportDefault obj) {

    }

    @Override
    public void show_get_sub_product(ResponGetReportDefault obj) {

    }

    @Override
    public void show_get_sub_product_child(ResponGetReportDefault obj) {

    }

    @Override
    public void show_get_report_ctv(ResponGetReportListCTV obj) {

    }

    @Override
    public void show_get_report_ctv_detail(ResponGetReportListCTV obj) {

    }

    @Override
    public void show_get_report_fluctuations(ResponGetReportListCTV obj) {

    }
}
