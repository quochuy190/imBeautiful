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
import neo.vn.imbeautiful.untils.TimeUtils;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterHistoryCommissions extends RecyclerView.Adapter<AdapterHistoryCommissions.TopicViewHoder> {
    private List<ObjCommissions> mList;
    private Context context;
    private ItemClickListener OnIListener;


    public ItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterHistoryCommissions(List<ObjCommissions> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_commission_history, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHoder holder, final int position) {
        ObjCommissions obj = mList.get(position);
        if (position % 2 == 0) {
            holder.txt_name_hh_item.setBackgroundResource(R.color.green_holde_1_table);
            holder.txt_user_hh_item.setBackgroundResource(R.color.green_holde_1_table);
            holder.txt_total_hh.setBackgroundResource(R.color.green_holde_1_table);
        } else {
            holder.txt_name_hh_item.setBackgroundResource(R.color.green_holde_2_table);
            holder.txt_user_hh_item.setBackgroundResource(R.color.green_holde_2_table);
            holder.txt_total_hh.setBackgroundResource(R.color.green_holde_2_table);
        }
        if (obj.getUPDATE_TIME() != null && obj.getUPDATE_TIME().length() > 0)
            holder.txt_name_hh_item.setText(TimeUtils.convent_date(obj.getUPDATE_TIME(),
                    "HH:mm:ss dd/MM/yyyy", "dd/MM/yyyy HH:mm"));
        if (obj.getTRANSACTION_TYPE() != null) {
            if (obj.getTRANSACTION_TYPE().equals("1")) {
                if (obj.getAMOUNT() != null && obj.getAMOUNT().length() > 0) {
                    holder.txt_user_hh_item.setText("+" + StringUtil.conventMonney(obj.getAMOUNT()));
                    holder.txt_total_hh.setText("");
                }
            } else if (obj.getAMOUNT() != null && obj.getAMOUNT().length() > 0) {
                holder.txt_total_hh.setText("- " + StringUtil.conventMonney(obj.getAMOUNT()));
                holder.txt_user_hh_item.setText("");
            }

        }
       /* holder.txt_name_hh_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnIListener.onClickItem(position, mList.get(position));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TopicViewHoder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.txt_time_history_hh)
        TextView txt_name_hh_item;
        @BindView(R.id.txt_value_add_history_hh)
        TextView txt_user_hh_item;
        @BindView(R.id.txt_value_total_history_hh)
        TextView txt_total_hh;


        public TopicViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  itemView.setOnClickListener(this);
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
