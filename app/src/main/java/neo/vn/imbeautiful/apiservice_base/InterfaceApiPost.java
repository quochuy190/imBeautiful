package neo.vn.imbeautiful.apiservice_base;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import neo.vn.imbeautiful.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by QQ on 7/4/2017.
 */

public interface InterfaceApiPost {
    //Log info action user
    @FormUrlEncoded
    @Headers("content-type: application/x-www-form-urlencoded")
    @POST("/{service}")
    Call<ResponseBody> getApiServiceRest(@Path("service") String service, @FieldMap Map<String, String> data);

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit_restful = new Retrofit.Builder()
            .baseUrl(Config.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

}
