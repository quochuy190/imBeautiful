package neo.vn.imbeautiful.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.models.ObjCommissions;
import neo.vn.imbeautiful.untils.StringUtil;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterRequestPay extends RecyclerView.Adapter<AdapterRequestPay.TopicViewHoder> {
    private List<ObjCommissions> mList;
    private Context context;
    private ItemClickListener OnIListener;


    public ItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterRequestPay(List<ObjCommissions> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report_pay_com, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHoder holder, final int position) {
        ObjCommissions obj = mList.get(position);
        if (obj.getNAMECTV() != null)
            holder.txt_name_ctv.setText("Tên CTV: " + obj.getNAMECTV());
        else
            holder.txt_name_ctv.setText("Tên CTV: ...");
        if (obj.getUSER_CODE() != null)
            holder.txt_usercode_ctv.setText("Mã CTV: " + obj.getUSER_CODE());
        else
            holder.txt_usercode_ctv.setText("Mã CTV: ...");
        if (obj.getUPDATE_TIME() != null)
            holder.txt_time.setText("Thời gian: " + obj.getUPDATE_TIME());
        else
            holder.txt_time.setText("Thời gian: ...");
        if (obj.getTOTAL_HH() != null)
            holder.txt_sodu.setText("Số dư tài khoản: " + StringUtil.conventMonney_Long(obj.getTOTAL_HH()));
        else
            holder.txt_sodu.setText("Số dư tài khoản: ...");
        if (obj.getAMOUNT() != null)
            holder.txt_num_rut.setText(StringUtil.conventMonney_Long(obj.getAMOUNT()));
        else
            holder.txt_num_rut.setText("");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TopicViewHoder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.txt_name_ctv)
        TextView txt_name_ctv;
        @BindView(R.id.txt_usercode_ctv)
        TextView txt_usercode_ctv;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_sodu)
        TextView txt_sodu;
        @BindView(R.id.txt_num_rut)
        TextView txt_num_rut;

        public TopicViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OnIListener.onClickItem(getLayoutPosition(), mList.get(getLayoutPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void updateList(List<ObjCommissions> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
