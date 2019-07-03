package neo.vn.imbeautiful.activity.login;

import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.ObjLogin;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 10:37
 * Version: 1.0
 */
public interface InterfaceLogin {
    interface Presenter {
        void api_login(String sUserName, String sPassWord);

        void api_register(String FULL_NAME, String MOBILE, String EMAIL, String ID_CITY,
                          String ID_DISTRICT, String ADDRESS, String PASSWORD);

        void api_update_device(String USERNAME, String APP_VERSION, String MODEL_NAME, String TOKEN_KEY,
                               String DEVICE_TYPE, String OS_VERSION, String UUID);
    }

    interface View {
        void show_error_api();

        void show_login(ObjLogin obj);

        void show_register(ErrorApi obj);

        void show_update_device(ErrorApi obj);
    }
}
