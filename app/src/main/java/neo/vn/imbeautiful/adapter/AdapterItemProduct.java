package neo.vn.imbeautiful.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.callback.setOnItemClickListener;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.untils.StringUtil;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterItemProduct extends RecyclerView.Adapter<AdapterItemProduct.FlightInfoViewHoder> {
    private List<Products> mLisObjService;
    private Context context;
    private setOnItemClickListener OnIListener;
    private ItemClickListener onListenerItemClickObjService;

    public setOnItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(setOnItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterItemProduct(Context context, ItemClickListener onListenerItemClickObjService) {
        this.context = context;
        this.onListenerItemClickObjService = onListenerItemClickObjService;
    }


    @NonNull
    @Override
    public FlightInfoViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_home, parent, false);
        return new FlightInfoViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightInfoViewHoder holder, final int position) {
        Products objService = mLisObjService.get(position);

        if (objService.getsName() != null)
            holder.txt_name.setText(objService.getsName());
        if (objService.getsPrice() != null)
            holder.txt_prime.setText(StringUtil.conventMonney(objService.getsPrice()));
        if (objService.getsUrlImage() != null) {
            Glide.with(context).load(objService.getsUrlImage()).asBitmap()
                    .placeholder(R.drawable.img_defaul)
                    .into(new BitmapImageViewTarget(holder.img_product) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            //   progressBar.setVisibility(View.GONE);
                        }
                    });
            // Glide.with(context).load(objService.getsUrlImage()).into(holder.img_product);
        }

        holder.img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerItemClickObjService.onClickItem(position, mLisObjService.get(position));
            }
        });
        /*holder.txt_more_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerItemClickObjService.onItemXemthemClick(mLisObjService.get(position));
            }
        });*/
    }


    @Override
    public int getItemCount() {
        if (mLisObjService == null) {
            return 0;
        }
        return mLisObjService.size();
    }

    public class FlightInfoViewHoder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.img_product)
        ImageView img_product;
        @BindView(R.id.txt_prime)
        TextView txt_prime;
        @BindView(R.id.txt_name)
        TextView txt_name;

        public FlightInfoViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListenerItemClickObjService.onClickItem(getLayoutPosition(), mLisObjService.get(getLayoutPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            //  OnIListener.OnLongItemClickListener(getLayoutPosition());
            return false;
        }
    }

    public void setData(List<Products> data) {
        if (mLisObjService != data) {
            mLisObjService = data;
            notifyDataSetChanged();
        }
    }
}
