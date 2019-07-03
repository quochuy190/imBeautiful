package neo.vn.imbeautiful.activity.collaborators;

import android.util.Log;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

import neo.vn.imbeautiful.apiservice_base.ApiServicePost;
import neo.vn.imbeautiful.callback.CallbackData;
import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.respon_api.ResponGetLisCTV;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 10:44
 * Version: 1.0
 */
public class PresenterCTV implements InterfaceCollaborators.Presenter {
    private static final String TAG = "PresenterLogin";
    ApiServicePost mApiService;
    InterfaceCollaborators.View mView;

    public PresenterCTV(InterfaceCollaborators.View mView) {
        this.mView = mView;
        mApiService = new ApiServicePost();
    }


    @Override
    public void api_get_lis_ctv(String USERNAME, String SEARCH, String ID_CITY, String I_PAGE, String NUMOFPAGE) {
        Map<String, String> mMap = new LinkedHashMap<>();
        String sService = "get_list_ctv";
        mMap.put("USERNAME", USERNAME);
        mMap.put("SEARCH", SEARCH);
        mMap.put("ID_CITY", ID_CITY);
        mMap.put("ID_CITY", ID_CITY);
        mMap.put("I_PAGE", I_PAGE);
        mMap.put("NUMOFPAGE", NUMOFPAGE);
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
                    ResponGetLisCTV obj = new Gson().fromJson(objT, ResponGetLisCTV.class);
                    mView.show_get_list_ctv(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.show_error_api();
                }
            }
        }, sService, mMap);
    }

    @Override
    public void api_update_ctv(String USERNAME, String USER_CTV, String NAME, String DOB, String GENDER, String EMAIL,
                               String CITY_NAME, String DISTRICT_NAME, String ADDRESS, String STK, String TENTK,
                               String TENNH, String AVATA) {
        Map<String, String> mMap = new LinkedHashMap<>();
        String sService = "edit_info_ctv";
        mMap.put("USERNAME", USERNAME);
        mMap.put("USER_CTV", USER_CTV);
        mMap.put("NAME", NAME);
        mMap.put("DOB", DOB);
        mMap.put("GENDER", GENDER);
        mMap.put("EMAIL", EMAIL);
        mMap.put("CITY_NAME", CITY_NAME);
        mMap.put("DISTRICT_NAME", DISTRICT_NAME);
        mMap.put("ADDRESS", ADDRESS);
        mMap.put("STK", STK);
        mMap.put("TENTK", TENTK);
        mMap.put("TENNH", TENNH);
        mMap.put("AVATA", AVATA);
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
                    ErrorApi obj = new Gson().fromJson(objT, ErrorApi.class);
                    mView.show_update_ctv(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.show_error_api();
                }
            }
        }, sService, mMap);

    }

    @Override
    public void api_reset_pass_ctv(String USERNAME, String USER_CTV) {
        Map<String, String> mMap = new LinkedHashMap<>();
        String sService = "reset_pass_ctv";
        mMap.put("USERNAME", USERNAME);
        mMap.put("USER_CTV", USER_CTV);

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
                    ErrorApi obj = new Gson().fromJson(objT, ErrorApi.class);
                    mView.show_reset_pass_ctv(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.show_error_api();
                }
            }
        }, sService, mMap);
    }
}
