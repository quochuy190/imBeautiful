package neo.vn.imbeautiful.activity.tintuc;

import android.util.Log;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

import neo.vn.imbeautiful.apiservice_base.ApiServicePost;
import neo.vn.imbeautiful.callback.CallbackData;
import neo.vn.imbeautiful.models.respon_api.ResponseInfomation;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 10:44
 * Version: 1.0
 */
public class PresenterTintuc implements InterfaceTintuc.Presenter {
    private static final String TAG = "PresenterProduct";
    ApiServicePost mApiService;
    InterfaceTintuc.View mView;

    public PresenterTintuc(InterfaceTintuc.View mView) {
        this.mView = mView;
        mApiService = new ApiServicePost();
    }

    @Override
    public void api_get_infomation(String USERNAME, String TYPES, String CATEGORY) {
        Map<String, String> mMap = new LinkedHashMap<>();
        String sService = "get_infomation";
        mMap.put("USERNAME", USERNAME);
        mMap.put("TYPES", TYPES);
        mMap.put("CATEGORY", CATEGORY);
        mApiService.getApiPostResfull_ALL(new CallbackData<String>() {
            @Override
            public void onGetDataErrorFault(Exception e) {
                mView.show_error_api();
                Log.i(TAG, "onGetDataErrorFault: " + e);
            }

            @Override
            public void onGetDataSuccess(String objT) {
                Log.i(TAG, "onGetDataSuccess: " + objT);
                try {
                    ResponseInfomation obj = new Gson().fromJson(objT, ResponseInfomation.class);
                    if (TYPES.equals("3")) {
                        mView.show_api_infomation_daotao(obj);
                    } else
                        mView.show_api_infomation(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.show_error_api();
                }
            }
        }, sService, mMap);
    }
}
