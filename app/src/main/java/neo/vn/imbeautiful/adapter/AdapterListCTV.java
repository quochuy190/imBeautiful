package neo.vn.imbeautiful.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.models.ObjCTV;
import neo.vn.imbeautiful.untils.StringUtil;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterListCTV extends RecyclerView.Adapter<AdapterListCTV.TopicViewHoder> {
    private List<ObjCTV> mList;
    private Context context;
    private ItemClickListener OnIListener;


    public ItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterListCTV(List<ObjCTV> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ctv, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHoder holder, int position) {
        ObjCTV obj = mList.get(position);
        if (position % 2 == 0) {
            holder.txt_sst_ctv_item.setBackgroundResource(R.color.green_holde_1_table);
            holder.txt_fullname_ctv_item.setBackgroundResource(R.color.green_holde_1_table);
            holder.txt_user_ctv_item.setBackgroundResource(R.color.green_holde_1_table);
            holder.txt_phone_ctv_item.setBackgroundResource(R.color.green_holde_1_table);
            holder.txt_balance_ctv_item.setBackgroundResource(R.color.green_holde_1_table);
        } else {
            holder.txt_sst_ctv_item.setBackgroundResource(R.color.green_holde_2_table);
            holder.txt_fullname_ctv_item.setBackgroundResource(R.color.green_holde_2_table);
            holder.txt_user_ctv_item.setBackgroundResource(R.color.green_holde_2_table);
            holder.txt_phone_ctv_item.setBackgroundResource(R.color.green_holde_2_table);
            holder.txt_balance_ctv_item.setBackgroundResource(R.color.green_holde_2_table);
        }
        if (obj.getFULL_NAME() != null && obj.getFULL_NAME().length() > 0)
            holder.txt_fullname_ctv_item.setText(obj.getFULL_NAME());
        else
            holder.txt_fullname_ctv_item.setText("....");
        if (obj.getUSER_CODE() != null && obj.getUSER_CODE().length() > 0)
            holder.txt_user_ctv_item.setText(obj.getUSER_CODE());
        else
            holder.txt_user_ctv_item.setText("....");
        holder.txt_sst_ctv_item.setText("" + (position + 1));
        if (obj.getUSERNAME() != null && obj.getUSERNAME().length() > 0)
            holder.txt_phone_ctv_item.setText(obj.getUSERNAME());
        else
            holder.txt_phone_ctv_item.setText("....");
        if (obj.getBALANCE() != null && obj.getBALANCE().length() > 0)
            holder.txt_balance_ctv_item.setText(StringUtil.conventMonney_Long(obj.getBALANCE()));
        else
            holder.txt_balance_ctv_item.setText("....");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TopicViewHoder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.txt_sst_ctv_item)
        TextView txt_sst_ctv_item;
        @BindView(R.id.txt_fullname_ctv_item)
        TextView txt_fullname_ctv_item;
        @BindView(R.id.txt_user_ctv_item)
        TextView txt_user_ctv_item;
        @BindView(R.id.txt_phone_ctv_item)
        TextView txt_phone_ctv_item;
        @BindView(R.id.txt_balance_ctv_item)
        TextView txt_balance_ctv_item;

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

    public void updateList(List<ObjCTV> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
