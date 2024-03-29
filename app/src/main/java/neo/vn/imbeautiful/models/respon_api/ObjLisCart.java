package neo.vn.imbeautiful.models.respon_api;

import java.io.Serializable;
import java.util.List;

import neo.vn.imbeautiful.models.Products;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 31-May-2019
 * Time: 16:51
 * Version: 1.0
 */
public class ObjLisCart implements Serializable {
    private String sName;
    private List<Products> mList;

    public ObjLisCart(List<Products> mList) {
        this.mList = mList;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public List<Products> getmList() {
        return mList;
    }

    public void setmList(List<Products> mList) {
        this.mList = mList;
    }
}
