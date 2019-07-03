package neo.vn.imbeautiful.models.respon_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import neo.vn.imbeautiful.models.ReportDefault;
import neo.vn.imbeautiful.models.ReportProduct;


public class ResponGetReportProduct {
    @SerializedName("ERROR")
    private String sERROR;
    @SerializedName("MESSAGE")
    private String sMESSAGE;
    @SerializedName("RESULT")
    private String sRESULT;
    @SerializedName("INFO")
    private List<ReportProduct> mList;

    public String getsERROR() {
        return sERROR;
    }

    public void setsERROR(String sERROR) {
        this.sERROR = sERROR;
    }

    public String getsMESSAGE() {
        return sMESSAGE;
    }

    public void setsMESSAGE(String sMESSAGE) {
        this.sMESSAGE = sMESSAGE;
    }

    public String getsRESULT() {
        return sRESULT;
    }

    public void setsRESULT(String sRESULT) {
        this.sRESULT = sRESULT;
    }

    public List<ReportProduct> getmList() {
        return mList;
    }

    public void setmList(List<ReportProduct> mList) {
        this.mList = mList;
    }
}
