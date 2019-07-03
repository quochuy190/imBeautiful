package neo.vn.imbeautiful;

import android.app.Application;

import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;


import java.util.ArrayList;
import java.util.List;

import neo.vn.imbeautiful.models.City;
import neo.vn.imbeautiful.models.Districts;
import neo.vn.imbeautiful.models.ObjCTV;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.ReportProduct;
import neo.vn.imbeautiful.models.respon_api.ResponGetReportProduct;
import okhttp3.OkHttpClient;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 11:28
 * Version: 1.0
 */
public class App extends Application {
    public static Products mProduct;
    public static ObjCTV mObjCTV;
    private static App sInstance;
    private Gson mGSon;
    public static List<City> mLisCity;
    public static List<ReportProduct> mLisReportProduct;
    public static ResponGetReportProduct mReportProduct;
    public static City mCity;
    public static Districts mDistrict;
    public static Boolean isLoginHome;

    public static App self() {
        return sInstance;
    }

    public static Boolean isLoadOrder;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mGSon = new Gson();
        mLisCity = new ArrayList<>();
        mLisReportProduct = new ArrayList<>();
        isLoginHome = false;
        isLoadOrder = false;
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }




    public Gson getGSon() {
        return mGSon;
    }
}
