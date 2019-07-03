package neo.vn.imbeautiful.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import neo.vn.imbeautiful.R;



/**
 * @author Quốc Huy
 * @version 1.0.0
 * @description
 * @desc Developer NEO Company.
 * @created 6/29/2018
 * @updated 6/29/2018
 * @modified by
 * @updated on 6/29/2018
 * @since 1.0
 */
public class AdapterImaViewPager extends PagerAdapter {
    private Activity mContext;
    private List<String> mLisUrl = new ArrayList<>();

    public AdapterImaViewPager(Activity mContext, List<String> ints) {
        this.mContext = mContext;
        this.mLisUrl = ints;
    }

    @Override
    public int getCount() {
        return mLisUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==  object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mContext.getLayoutInflater().inflate(R.layout.item_adrapter_image_vp, container,
                false);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(mContext).load(mLisUrl.get(position)).asBitmap()
                .placeholder(R.drawable.img_defaul)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        //   progressBar.setVisibility(View.GONE);
                    }
                });
        container.addView(view);
        return view;
    }
}
