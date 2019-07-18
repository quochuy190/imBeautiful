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
import neo.vn.imbeautiful.models.ObjNotify;


/**
 * Created by QQ on 7/7/2017.
 */

public class AdapterListNotify extends RecyclerView.Adapter<AdapterListNotify.TopicViewHoder> {
    private List<ObjNotify> mList;
    private Context context;
    private ItemClickListener OnIListener;


    public ItemClickListener getOnIListener() {
        return OnIListener;
    }

    public void setOnIListener(ItemClickListener onIListener) {
        OnIListener = onIListener;
    }

    public AdapterListNotify(List<ObjNotify> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public TopicViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notify, parent, false);
        return new TopicViewHoder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHoder holder, int position) {
        ObjNotify obj = mList.get(position);
        if (obj != null && obj.getCONTENT() != null && obj.getCONTENT().length() > 0)
            holder.txt_name.setText(obj.getCONTENT());
        if (obj != null && obj.getSENT_TIME() != null && obj.getSENT_TIME().length() > 0)
            holder.txt_notify_time.setText(obj.getSENT_TIME());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TopicViewHoder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_notify_time)
        TextView txt_notify_time;

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

    public void updateList(List<ObjNotify> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
