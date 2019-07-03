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
import neo.vn.imbeautiful.models.ObjOrder;
import neo.vn.imbeautiful.untils.StringUtil;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterListOrder extends RecyclerView.Adapter<AdapterListOrder.TopicViewHoder> {
    private List<ObjOrder> mList;
    private Context context;
    private ItemClickListener OnIListener;


    public ItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterListOrder(List<ObjOrder> listAirport, Context context) {
        this.mList = listAirport;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHoder holder, int position) {
        ObjOrder obj = mList.get(position);
        if (obj != null) {
            if (obj.getFULL_NAME_CTV() != null && obj.getFULL_NAME_CTV().length() > 0)
                holder.txt_name_CTV.setText("Tên CTV: " + obj.getFULL_NAME_CTV());
            else
                holder.txt_name_CTV.setText("...");
            if (obj.getCREATE_DATE() != null && obj.getCREATE_DATE().length() > 0)
                holder.txt_time_start_order.setText(obj.getCREATE_DATE());
            else
                holder.txt_time_start_order.setText("...");
            if (obj.getSTATUS() != null) {
                holder.txt_item_order_status.setText(obj.getSTATUS_NAME().toUpperCase());
            } else {
                holder.txt_item_order_status.setText("...");
            }
            if (obj.getCREATE_BY() != null) {
                holder.txt_item_order_id_CTV.setText("Mã CTV: " + obj.getUSER_CODE().toUpperCase());
            } else {
                holder.txt_item_order_id_CTV.setText("Mã CTV: ...");
            }
            if (obj.getCODE_ORDER() != null) {
                holder.txt_code_order.setText("Mã ĐH: " + obj.getCODE_ORDER().toUpperCase());
            } else {
                holder.txt_code_order.setText("...");
            }
            if (obj.getTOTAL_MONEY() != null) {
                holder.txt_item_order_total_prime.setText(StringUtil.conventMonney_Long(obj.getTOTAL_MONEY()));
            } else {
                holder.txt_item_order_total_prime.setText("0 đ");
            }
            if (obj.getFULLNAME_RECEIVER() != null) {
                holder.txt_item_order_name_customer.setText(obj.getFULLNAME_RECEIVER());
            } else {
                holder.txt_item_order_name_customer.setText("....");
            }
            if (obj.getMOBILE_RECCEIVER() != null) {
                holder.txt_phone_customer.setText(obj.getMOBILE_RECCEIVER());
            } else {
                holder.txt_phone_customer.setText("...");
            }
            if (obj.getSTATUS() != null) {
                switch (obj.getSTATUS()) {
                    case "0":
                        holder.txt_item_order_status.setText("Đã hoàn thành");
                        break;
                    case "1":
                        holder.txt_item_order_status.setText("Đang xử lý");
                        break;
                    case "2":
                        holder.txt_item_order_status.setText("Đã tiếp nhận");
                        break;
                    case "3":
                        holder.txt_item_order_status.setText("Đang vận chuyển");
                        break;
                    case "4":
                        holder.txt_item_order_status.setText("Đã huỷ");
                        break;

                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TopicViewHoder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.txt_item_order_name_CTV)
        TextView txt_name_CTV;
        @BindView(R.id.txt_item_order_time)
        TextView txt_time_start_order;
        @BindView(R.id.txt_item_order_status)
        TextView txt_item_order_status;
        @BindView(R.id.txt_phone_customer)
        TextView txt_phone_customer;
        @BindView(R.id.txt_code_order)
        TextView txt_code_order;
        @BindView(R.id.txt_item_order_id_CTV)
        TextView txt_item_order_id_CTV;
        @BindView(R.id.txt_item_order_name_customer)
        TextView txt_item_order_name_customer;
        @BindView(R.id.txt_item_order_total_prime)
        TextView txt_item_order_total_prime;

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

    public void updateList(List<ObjOrder> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
