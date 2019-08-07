package neo.vn.imbeautiful.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.callback.ItemClickCartListener;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.models.PropetiObj;
import neo.vn.imbeautiful.untils.StringUtil;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.TopicViewHoder> {
    private List<Products> mList;
    private Context context;
    private ItemClickCartListener OnIListener;
    private ItemClickListener itemClickListener;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ItemClickCartListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickCartListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterCart(List<Products> listAirport, Context context) {
        this.mList = listAirport;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(final TopicViewHoder holder, final int position) {
        final Products obj = mList.get(position);
        if (obj != null) {
            if (obj.getmLisPropeti() != null && obj.getmLisPropeti().size() > 0) {
                String sThuoctinh = "";
                for (PropetiObj.PropetiDetail objDetail : obj.getmLisPropeti()) {
                    sThuoctinh = sThuoctinh + objDetail.getNAME_PARENT() + ":" + objDetail.getSUB_PROPERTIES() + "  ";
                }
                holder.txt_thuoctinh.setText(sThuoctinh);
            }
            if (obj.isVisibleDelete()) {
                holder.img_delete.setVisibility(View.VISIBLE);
            } else
                holder.img_delete.setVisibility(View.GONE);
            if (obj.getsName() != null && obj.getsName().length() > 0)
                holder.txt_name.setText(obj.getsName());
            else
                holder.txt_name.setText("...");
            if (obj != null && obj.getsPrice().length() > 0)
                holder.txt_prime.setText(StringUtil.conventMonney(obj.getsPrice()));
            else
                holder.txt_prime.setText("...");
            if (obj.getsUrlImage() != null) {
                Glide.with(context).load(obj.getsUrlImage()).asBitmap()
                        .placeholder(R.drawable.img_defaul)
                        .into(new BitmapImageViewTarget(holder.img_product) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                //   progressBar.setVisibility(View.GONE);
                            }
                        });
            } else
                Glide.with(context).load(R.drawable.img_defaul).into(holder.img_product);

            if (obj.getsQuantum() != null && obj.getsQuantum().length() > 0) {
                if (obj.getsQuantum().matches("[0-9]+")) {
                    holder.txt_value_cart.setText(obj.getsQuantum());
                } else {
                    holder.txt_value_cart.setText("0");
                }
  /*              if (obj.getCOMMISSION() != null) {
                    int commission = Integer.parseInt(obj.getCOMMISSION());
                    int price = Integer.parseInt(obj.getsQuantum()) * Integer.parseInt(obj.getsPrice());
                    long com_price = (commission * price) / 100;
                    holder.txt_commission.setText("Hoa hồng: " + StringUtil.conventMonney_Long("" + com_price));
                } else
                    holder.txt_commission.setText("Hoa hồng: 0đ");*/

            } else {
                holder.txt_value_cart.setText("0");
                //  holder.txt_commission.setText("Hoa hồng: 0đ");
            }

            holder.txt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnIListener.onClickItem_Add(position, obj);
                }
            });
            holder.txt_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnIListener.onClickItem_Minus(position, obj);
                }
            });
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnIListener.onClickItem(position, obj);
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
        @BindView(R.id.txt_name_product_cart)
        TextView txt_name;
        @BindView(R.id.txt_price_cart)
        TextView txt_prime;
        @BindView(R.id.img_product_cart)
        ImageView img_product;
        @BindView(R.id.txt_minus)
        TextView txt_minus;
        @BindView(R.id.txt_value_cart)
        TextView txt_value_cart;
        @BindView(R.id.txt_add_cart)
        TextView txt_add;
        @BindView(R.id.img_delete)
        ImageView img_delete;
        @BindView(R.id.txt_thuoctinh)
        TextView txt_thuoctinh;

        public TopicViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClickItem(getLayoutPosition(), mList.get(getLayoutPosition()));
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
