package neo.vn.imbeautiful.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.untils.StringUtil;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterListProductHome extends RecyclerView.Adapter<AdapterListProductHome.TopicViewHoder> {
    private List<Products> mList;
    private Context context;
    private ItemClickListener OnIListener;


    public ItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterListProductHome(List<Products> listAirport, Context context) {
        this.mList = listAirport;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_home, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHoder holder, final int position) {
        final Products obj = mList.get(position);
        if (obj!=null){
            if (obj.getsName() != null && obj.getsName().length() > 0)
                holder.txt_name.setText(obj.getsName().toLowerCase());
            else
                holder.txt_name.setText("...");
            if (obj.getsPrice() != null && obj.getsPrice().length() > 0)
                holder.txt_prime.setText(StringUtil.conventMonney_Long(obj.getsPrice()));
            else
                holder.txt_prime.setText(StringUtil.conventMonney_Long("0"));
            if (obj.getsUrlImage() != null && obj.getsUrlImage().length() > 0) {
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
            holder.img_product.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_prime)
        TextView txt_prime;
        @BindView(R.id.img_product)
        ImageView img_product;

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
