package neo.vn.imbeautiful.activity.login.Menu_Search;

import neo.vn.imbeautiful.models.respon_api.ResponGetDistrict;
import neo.vn.imbeautiful.models.respon_api.ResponGetProvince;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 02-May-2019
 * Time: 11:41
 * Version: 1.0
 */
public interface InterfaceSearchMenu {
    interface Presenter {
        void api_get_citys();

        void api_get_district(String sCity_Id);
    }

    interface View {
        void show_error_api();

        void show_api_get_city(ResponGetProvince objRespon);

        void show_api_get_district(ResponGetDistrict obj);
    }
}
