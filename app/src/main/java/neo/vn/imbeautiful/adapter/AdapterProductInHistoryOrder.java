package neo.vn.imbeautiful.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.callback.ItemClickCartListener;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.PropetiObj;
import neo.vn.imbeautiful.untils.StringUtil;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterProductInHistoryOrder extends RecyclerView.Adapter<AdapterProductInHistoryOrder.TopicViewHoder> {
    private List<Products> mList;
    private Context context;
    private ItemClickCartListener OnIListener;


    public ItemClickCartListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickCartListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterProductInHistoryOrder(List<Products> listAirport, Context context) {
        this.mList = listAirport;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_in_order_detail, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHoder holder, final int position) {
        final Products obj = mList.get(position);
        if (obj != null) {
            if (obj.isVisibleButtonAdd()) {
                holder.txt_add_num.setVisibility(View.VISIBLE);
                holder.txt_minus_num.setVisibility(View.VISIBLE);
                holder.txt_add_num.setText("+");
                holder.txt_minus_num.setText("-");
                holder.txt_add_num.setEnabled(true);
                holder.txt_minus_num.setEnabled(true);
            } else {
                holder.txt_add_num.setVisibility(View.VISIBLE);
                holder.txt_minus_num.setVisibility(View.VISIBLE);
                holder.txt_add_num.setText("");
                holder.txt_minus_num.setText("");
                holder.txt_add_num.setEnabled(false);
                holder.txt_minus_num.setEnabled(false);
            }
            if (obj.getsName() != null && obj.getsName().length() > 0)
                holder.txt_name.setText(obj.getsName());
            else
                holder.txt_name.setText("...");
            if (obj.getCODE_PRODUCT() != null)
                holder.txt_title_commission.setText("Mã SP: " + obj.getCODE_PRODUCT());
            if (obj.getmLisPropeti() != null && obj.getmLisPropeti().size() > 0) {
                String sThuoctinh = "";
                for (PropetiObj.PropetiDetail objDetail : obj.getmLisPropeti()) {
                    sThuoctinh = sThuoctinh + objDetail.getNAME_PARENT() + " - " + objDetail.getSUB_PROPERTIES() + ",";
                }
                if (sThuoctinh.length()>0){
                    sThuoctinh = sThuoctinh.substring(0, sThuoctinh.length()-1);
                    holder.txt_thuoctinh.setText("Thuộc tính: " + sThuoctinh);
                }
            } else if (obj.getPROPERTIES() != null)
                holder.txt_thuoctinh.setText("Thuộc tính: " + obj.getPROPERTIES());
            if (obj != null && obj.getsPrice().length() > 0)
                holder.txt_total_price.setText(StringUtil.conventMonney_Long(obj.getMONEY()));
            else
                holder.txt_total_price.setText("...");
            if (obj.getNUM() != null && obj.getNUM().length() > 0) {
                if (obj.getsPrice() != null) {
                    int num = Integer.parseInt(obj.getNUM());
                    int price = Integer.parseInt(obj.getsPrice());
                    int total = num * price;
                    holder.txt_total_price.setText(StringUtil.conventMonney("" + total));
                }
                holder.txt_sl_product.setText(obj.getNUM());
            /*    if (obj.getCOMMISSION() != null) {
                    int commission = Integer.parseInt(obj.getCOMMISSION());
                    int price = Integer.parseInt(obj.getNUM()) * Integer.parseInt(obj.getsPrice());
                    long com_price = (commission * price) / 100;
                    holder.txt_title_commission.setText("Hoa hồng: " + StringUtil.conventMonney_Long("" + com_price));
                } else
                    holder.txt_title_commission.setText("Hoa hồng: 0đ");*/
            } else {
              /*  holder.txt_title_commission.setText("Hoa hồng: 0đ");
                holder.txt_sl_product.setText("...");*/
            }
            if (obj.getsPrice() != null) {
                holder.txt_title_price.setText("Đơn giá: " + StringUtil.conventMonney_Long(obj.getsPrice()));
            } else
                holder.txt_title_price.setText("Đơn giá: 0đ");
            Glide.with(context).load(obj.getsUrlImage()).into(holder.img_product);
            holder.txt_minus_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnIListener.onClickItem_Minus(position, obj);
                }
            });
            holder.txt_add_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnIListener.onClickItem_Add(position, obj);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TopicViewHoder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.txt_name)
        TextView txt_name;
        /*   @BindView(R.id.txt_prime)
           TextView txt_prime;*/
        @BindView(R.id.txt_sl_product)
        TextView txt_sl_product;
        @BindView(R.id.txt_total_price)
        TextView txt_total_price;
        @BindView(R.id.txt_add_num)
        TextView txt_add_num;
        @BindView(R.id.txt_minus_num)
        TextView txt_minus_num;
        @BindView(R.id.img_product)
        ImageView img_product;
        @BindView(R.id.txt_title_commission)
        TextView txt_title_commission;
        @BindView(R.id.txt_title_price)
        TextView txt_title_price;
        @BindView(R.id.txt_thuoctinh)
        TextView txt_thuoctinh;

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

    public void updateList(List<Products> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
