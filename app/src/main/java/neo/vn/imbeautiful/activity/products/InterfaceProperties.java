package neo.vn.imbeautiful.activity.products;

import neo.vn.imbeautiful.models.respon_api.ResponGetCat;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 21-May-2019
 * Time: 10:09
 * Version: 1.0
 */
public interface InterfaceProperties {
    interface Presenter {
        void api_get_properties(String USERNAME, String LIST_PROPERTIES);

    }

    interface View {
        void show_error_api();

        void show_get_properties(ResponGetCat obj);

    }
}
