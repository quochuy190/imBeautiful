package neo.vn.imbeautiful.activity.login.Menu_Search;

import android.util.Log;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

import neo.vn.imbeautiful.apiservice_base.ApiServicePost;
import neo.vn.imbeautiful.callback.CallbackData;
import neo.vn.imbeautiful.models.respon_api.ResponGetDistrict;
import neo.vn.imbeautiful.models.respon_api.ResponGetProvince;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 11:46
 * Version: 1.0
 */
public class PresenterSearchMenu implements InterfaceSearchMenu.Presenter {
    private static final String TAG = "PresenterSearchMenu";
    ApiServicePost mApiService;
    InterfaceSearchMenu.View mView;

    public PresenterSearchMenu(InterfaceSearchMenu.View mView) {
        mApiService = new ApiServicePost();
        this.mView = mView;
    }

    @Override
    public void api_get_citys() {
        Map<String, String> mMap = new LinkedHashMap<>();
        String sService = "get_city";
        mApiService.getApiService(new CallbackData<String>() {
            @Override
            public void onGetDataErrorFault(Exception e) {
                mView.show_error_api();
                Log.i(TAG, "onGetDataErrorFault: " + e);
            }

            @Override
            public void onGetDataSuccess(String objT) {
                Log.i(TAG, "onGetDataSuccess: " + objT);
                try {
                    //jArray = new JSONArray(c);
                    ResponGetProvince obj = new Gson().fromJson(objT, ResponGetProvince.class);
                    //   List<Baitap_Tuan> mLis = Baitap_Tuan.getList(objT);
                    mView.show_api_get_city(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.show_error_api();
                }
            }
        }, sService, mMap);
    }

    @Override
    public void api_get_district(String sCity_Id) {
        Map<String, String> mMap = new LinkedHashMap<>();
        String sService = "get_district";
        mMap.put("ID_CITY", sCity_Id);
        mApiService.getApiService(new CallbackData<String>() {
            @Override
            public void onGetDataErrorFault(Exception e) {
                mView.show_error_api();
                Log.i(TAG, "onGetDataErrorFault: " + e);
            }

            @Override
            public void onGetDataSuccess(String objT) {
                Log.i(TAG, "onGetDataSuccess: " + objT);
                try {
                    //jArray = new JSONArray(c);
                    ResponGetDistrict obj = new Gson().fromJson(objT, ResponGetDistrict.class);
                    //   List<Baitap_Tuan> mLis = Baitap_Tuan.getList(objT);
                    mView.show_api_get_district(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.show_error_api();
                }
            }
        }, sService, mMap);
    }
}
