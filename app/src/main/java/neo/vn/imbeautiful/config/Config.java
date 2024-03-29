package neo.vn.imbeautiful.config;

import java.util.List;

import neo.vn.imbeautiful.models.ObjConfigCommis;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 22-April-2019
 * Time: 10:35
 * Version: 1.0
 */
public class Config {
    public static String BASE_URL_API = "http://node.f5sell.com";

    public static String STATUS_ORDER_DANGXULY = "1";
    public static String STATUS_ORDER_DATIEPNHAN = "2";
    public static String STATUS_ORDER_DANGVANCHUYEN = "3";
    public static String STATUS_ORDER_DAHOANTHANH = "0";
    public static String STATUS_ORDER_DAHUY = "4";

    public static String GROUP_ADMIN = "0";
    public static String GROUP_SHOP = "3";
    public static String GROUP_QUANLY = "4";
    public static String GROUP_CONGTACVIEN = "5";
    public static String GROUP_BIENTAPVIEN = "6";

    public static String ID_SHOP = "ABC123";

    public static List<ObjConfigCommis> mLisConfigCommis;
}
