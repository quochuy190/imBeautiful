package neo.vn.imbeautiful.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ErrorApi {
    @SerializedName("ERROR")
    String sERROR;
    @SerializedName("MESSAGE")
    String sMESSAGE;
    @SerializedName("MESSGE")
    String MESSGE;
    @SerializedName("RESULT")
    String sRESULT;

    public ErrorApi() {
    }

    private static ErrorApi getObject(JSONObject jsonObject) {
        return new Gson().fromJson(jsonObject.toString(), ErrorApi.class);
    }

    public static ArrayList<ErrorApi> getList(String jsonArray) throws JSONException {
        ArrayList<ErrorApi> arrayList = new ArrayList<>();
        Type type = new TypeToken<List<ErrorApi>>() {
        }.getType();
        Gson gson = new Gson();
        arrayList = gson.fromJson(jsonArray, type);
        return arrayList;
    }

    public String getMESSGE() {
        return MESSGE;
    }

    public void setMESSGE(String MESSGE) {
        this.MESSGE = MESSGE;
    }

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
}
