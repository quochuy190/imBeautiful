package neo.vn.imbeautiful.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.callback.ItemClickListener;
import neo.vn.imbeautiful.models.CategoryProductHome;


public class AdapterCategoryProductHome extends
        RecyclerView.Adapter<AdapterCategoryProductHome.CategoryServiceViewHolder> {
    public static Context mContext;
    private List<CategoryProductHome> mLisCateService;

    public static ItemClickListener onListenerItemClickObjService;
    private ItemClickListener OnIListener;

    public AdapterCategoryProductHome(Context context, List<CategoryProductHome> mLisCateService,
                                      ItemClickListener onListenerItemClickObjService) {
        this.onListenerItemClickObjService = onListenerItemClickObjService;
        mContext = context;
        this.mLisCateService = mLisCateService;
    }

    public ItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    @NonNull
    @Override
    public CategoryServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_category_product_home, parent, false);
        return new CategoryServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryServiceViewHolder holder, final int position) {
        holder.title.setText(mLisCateService.get(position).getsName());
        holder.horizontalAdapter.setData(mLisCateService.get(position).getmList()); // List of Strings
       // holder.horizontalAdapter.setRowIndex(position);
        holder.icon_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnIListener.onClickItem(position, mLisCateService.get(position));
            }
        });
       /* holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnIListener.onClickItem(position, mLisCateService.get(position));
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mLisCateService.size();
    }


    public static class CategoryServiceViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        private AdapterItemProduct horizontalAdapter;
        private RecyclerView horizontalList;
        private ImageView icon_down;

        public CategoryServiceViewHolder(View view) {
            super(view);
            Context context = itemView.getContext();
            title = (TextView) view.findViewById(R.id.txt_title_objservice);
            icon_down = (ImageView) view.findViewById(R.id.icon_down);
            horizontalList = (RecyclerView) itemView.findViewById(R.id.recycle_lis_objservice);
            horizontalList.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL,
                    false));
            horizontalAdapter = new AdapterItemProduct(mContext, onListenerItemClickObjService);
            horizontalList.setAdapter(horizontalAdapter);
        }
    }

    public void update_list(List<CategoryProductHome> list) {
        mLisCateService.clear();
        mLisCateService.addAll(list);
        notifyDataSetChanged();
    }

}
