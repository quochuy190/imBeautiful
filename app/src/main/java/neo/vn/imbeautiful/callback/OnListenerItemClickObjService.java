package neo.vn.imbeautiful.callback;


import neo.vn.imbeautiful.models.ObjCategoryProduct;
import neo.vn.imbeautiful.models.ObjSucCategory;

public interface OnListenerItemClickObjService {
    void onClickListener(ObjCategoryProduct objService);

    void onItemXemthemClick(ObjCategoryProduct objService);
}