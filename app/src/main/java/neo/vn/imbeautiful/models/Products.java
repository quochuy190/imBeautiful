package neo.vn.imbeautiful.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 08-May-2019
 * Time: 16:57
 * Version: 1.0
 */
public class Products implements Serializable {
    @SerializedName("ID")
    String ID;
    @SerializedName("COMMISSION_PRICE")
    String COMMISSION_PRICE;
    @SerializedName("STATUS_EDIT")
    String STATUS_EDIT;
    @SerializedName("COMMISSION")
    String COMMISSION;
    @SerializedName("IMAGE_COVER")
    String sUrlImage;
    @SerializedName("PRODUCT_NAME")
    String sName;
    @SerializedName("PRICE")
    String sPrice;
    @SerializedName("IMG1")
    String IMG1;
    @SerializedName("IMG2")
    String IMG2;
    @SerializedName("IMG3")
    String IMG3;
    @SerializedName("ID_PRODUCT_PROPERTIES")
    String ID_PRODUCT_PROPERTIES;
    @SerializedName("PROPERTIES_NAME")
    String PROPERTIES_NAME;
    @SerializedName("PROPERTIES")
    String PROPERTIES;
    @SerializedName("CODE_PRODUCT")
    String CODE_PRODUCT;
    @SerializedName("DESCRIPTION")
    String DESCRIPTION;
    @SerializedName("NUM")
    String NUM;
    @SerializedName("MONEY")
    String MONEY;
    @SerializedName("ID_CODE_ORDER")
    String ID_CODE_ORDER;
    String sQuantum;
    @SerializedName("MEDIA_FB")
    String MEDIA_FB;
    @SerializedName("VIDEO_FB")
    String VIDEO_FB;
    @SerializedName("CONTENT_FB")
    String CONTENT_FB;
    @SerializedName("CONTENT_WEB")
    String CONTENT_WEB;
    @SerializedName("LINK_AFFILIATE")
    String LINK_AFFILIATE;
    @SerializedName("DESCRIPTION_HTML")
    String DESCRIPTION_HTML;
    @SerializedName("STATUS_TREND")
    String STATUS_TREND;
    @SerializedName("OD_PRODUCT_PROPERTIES")
    String OD_PRODUCT_PROPERTIES;

    String sThuoctinh;
    List<PropetiObj.PropetiDetail> mLisPropeti;
    List<PropetiObj> mListThuoctinhTong;
    private boolean isVisibleDelete;
    private boolean isVisibleButtonAdd;

    public Products(String sName, String sPrice, String sUrlImage) {
        this.sName = sName;
        this.sPrice = sPrice;
        this.sUrlImage = sUrlImage;
    }

    public String getOD_PRODUCT_PROPERTIES() {
        return OD_PRODUCT_PROPERTIES;
    }

    public void setOD_PRODUCT_PROPERTIES(String OD_PRODUCT_PROPERTIES) {
        this.OD_PRODUCT_PROPERTIES = OD_PRODUCT_PROPERTIES;
    }

    public List<PropetiObj> getmListThuoctinhTong() {
        return mListThuoctinhTong;
    }

    public void setmListThuoctinhTong(List<PropetiObj> mListThuoctinhTong) {
        this.mListThuoctinhTong = mListThuoctinhTong;
    }

    public List<PropetiObj.PropetiDetail> getmLisPropeti() {
        return mLisPropeti;
    }

    public void setmLisPropeti(List<PropetiObj.PropetiDetail> mLisPropeti) {
        this.mLisPropeti = mLisPropeti;
    }

    public String getsThuoctinh() {
        return sThuoctinh;
    }

    public void setsThuoctinh(String sThuoctinh) {
        this.sThuoctinh = sThuoctinh;
    }

    public String getSTATUS_TREND() {
        return STATUS_TREND;
    }

    public void setSTATUS_TREND(String STATUS_TREND) {
        this.STATUS_TREND = STATUS_TREND;
    }

    public boolean isVisibleButtonAdd() {
        return isVisibleButtonAdd;
    }

    public void setVisibleButtonAdd(boolean visibleButtonAdd) {
        isVisibleButtonAdd = visibleButtonAdd;
    }

    public String getCOMMISSION_PRICE() {
        return COMMISSION_PRICE;
    }


    public void setCOMMISSION_PRICE(String COMMISSION_PRICE) {
        this.COMMISSION_PRICE = COMMISSION_PRICE;
    }

    public String getSTATUS_EDIT() {
        return STATUS_EDIT;
    }

    public void setSTATUS_EDIT(String STATUS_EDIT) {
        this.STATUS_EDIT = STATUS_EDIT;
    }

    public String getCOMMISSION() {
        return COMMISSION;
    }

    public void setCOMMISSION(String COMMISSION) {
        this.COMMISSION = COMMISSION;
    }

    public String getsQuantum() {
        return sQuantum;
    }

    public void setsQuantum(String sQuantum) {
        this.sQuantum = sQuantum;
    }


    public String getNUM() {
        return NUM;
    }

    public String getMONEY() {
        return MONEY;
    }

    public void setMONEY(String MONEY) {
        this.MONEY = MONEY;
    }

    public String getID_CODE_ORDER() {
        return ID_CODE_ORDER;
    }

    public void setID_CODE_ORDER(String ID_CODE_ORDER) {
        this.ID_CODE_ORDER = ID_CODE_ORDER;
    }

    public void setNUM(String NUM) {
        this.NUM = NUM;
    }

    public String getsName() {
        return sName;
    }

    public boolean isVisibleDelete() {
        return isVisibleDelete;
    }

    public void setVisibleDelete(boolean visibleDelete) {
        isVisibleDelete = visibleDelete;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIMG1() {
        return IMG1;
    }

    public void setIMG1(String IMG1) {
        this.IMG1 = IMG1;
    }

    public String getIMG2() {
        return IMG2;
    }

    public void setIMG2(String IMG2) {
        this.IMG2 = IMG2;
    }

    public String getIMG3() {
        return IMG3;
    }

    public void setIMG3(String IMG3) {
        this.IMG3 = IMG3;
    }

    public String getID_PRODUCT_PROPERTIES() {
        return ID_PRODUCT_PROPERTIES;
    }

    public void setID_PRODUCT_PROPERTIES(String ID_PRODUCT_PROPERTIES) {
        this.ID_PRODUCT_PROPERTIES = ID_PRODUCT_PROPERTIES;
    }

    public String getPROPERTIES_NAME() {
        return PROPERTIES_NAME;
    }

    public void setPROPERTIES_NAME(String PROPERTIES_NAME) {
        this.PROPERTIES_NAME = PROPERTIES_NAME;
    }

    public String getPROPERTIES() {
        return PROPERTIES;
    }

    public void setPROPERTIES(String PROPERTIES) {
        this.PROPERTIES = PROPERTIES;
    }

    public String getCODE_PRODUCT() {
        return CODE_PRODUCT;
    }

    public void setCODE_PRODUCT(String CODE_PRODUCT) {
        this.CODE_PRODUCT = CODE_PRODUCT;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsPrice() {
        return sPrice;
    }

    public void setsPrice(String sPrice) {
        this.sPrice = sPrice;
    }

    public String getsUrlImage() {
        return sUrlImage;
    }

    public void setsUrlImage(String sUrlImage) {
        this.sUrlImage = sUrlImage;
    }

    public String getMEDIA_FB() {
        return MEDIA_FB;
    }

    public void setMEDIA_FB(String MEDIA_FB) {
        this.MEDIA_FB = MEDIA_FB;
    }

    public String getVIDEO_FB() {
        return VIDEO_FB;
    }

    public void setVIDEO_FB(String VIDEO_FB) {
        this.VIDEO_FB = VIDEO_FB;
    }

    public String getCONTENT_FB() {
        return CONTENT_FB;
    }

    public void setCONTENT_FB(String CONTENT_FB) {
        this.CONTENT_FB = CONTENT_FB;
    }

    public String getCONTENT_WEB() {
        return CONTENT_WEB;
    }

    public void setCONTENT_WEB(String CONTENT_WEB) {
        this.CONTENT_WEB = CONTENT_WEB;
    }

    public String getLINK_AFFILIATE() {
        return LINK_AFFILIATE;
    }

    public void setLINK_AFFILIATE(String LINK_AFFILIATE) {
        this.LINK_AFFILIATE = LINK_AFFILIATE;
    }

    public String getDESCRIPTION_HTML() {
        return DESCRIPTION_HTML;
    }

    public void setDESCRIPTION_HTML(String DESCRIPTION_HTML) {
        this.DESCRIPTION_HTML = DESCRIPTION_HTML;
    }
}
