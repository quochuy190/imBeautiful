package neo.vn.imbeautiful.activity.notify;

import neo.vn.imbeautiful.models.respon_api.ResponGetCat;
import neo.vn.imbeautiful.models.respon_api.ResponGetnotify;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 21-May-2019
 * Time: 10:09
 * Version: 1.0
 */
public interface InterfaceNotify {
    interface Presenter {
        void api_get_list_notify(String USERNAME, String PAGE, String NUMOFPAGE);

    }

    interface View {
        void show_error_api();

        void show_get_notify(ResponGetnotify obj);

    }
}
