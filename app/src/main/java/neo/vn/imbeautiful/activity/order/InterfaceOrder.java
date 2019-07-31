package neo.vn.imbeautiful.activity.order;

import neo.vn.imbeautiful.models.ErrorApi;
import neo.vn.imbeautiful.models.ObjOrder;
import neo.vn.imbeautiful.models.respon_api.ResponGetProduct;
import neo.vn.imbeautiful.models.respon_api.ResponHistoryOrder;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 30-May-2019
 * Time: 17:14
 * Version: 1.0
 */
public interface InterfaceOrder {
    interface Presenter {
        void api_get_order_history(String USERNAME, String START_TIME, String END_TIME, String USER_CTV,
                                   String STATUS, String PAGE, String NUMOFPAGE);

        void api_get_order_history_detail(String USERNAME, String CODE_ORDER);

        void api_get_order_history_detail_pd(String USERNAME, String CODE_ORDER);

        void api_edit_order_product(String USERNAME, String CODE_PRODUCT, String AMOUNT, String PRICE,
                                    String MONEY, String BONUS, String FULL_NAME, String MOBILE_RECEIVER,
                                    String ID_CITY, String ID_DISTRICT, String ADDRESS, String CODE_ORDER, String STATUS,
                                    String EXTRA_SHIP, String TIME_RECEIVER, String NOTE);

        void api_order_product(String USERNAME, String CODE_PRODUCT, String AMOUNT, String PRICE,
                               String MONEY, String BONUS, String FULL_NAME, String MOBILE_RECEIVER,
                               String ID_CITY, String ID_DISTRICT, String ADDRESS);

        void api_get_config_commission(String USERNAME, String VALUES);
    }

    interface View {
        void show_error_api();

        void show_order_history(ResponHistoryOrder obj);

        void show_order_history_detail(ObjOrder obj);

        void show_order_history_detail_pd(ResponGetProduct obj);

        void show_edit_order_product(ErrorApi obj);

        void show_order_product(ErrorApi obj);

        void show_config_commission(ErrorApi obj);
    }
}
