package neo.vn.imbeautiful.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseFragment;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 22-April-2019
 * Time: 10:30
 * Version: 1.0
 */
public class FragmentSetup extends BaseFragment {
    private static final String TAG = "FragmentSetup";
    public static FragmentSetup fragment;

    public static FragmentSetup getInstance() {
        if (fragment == null) {
            synchronized (FragmentSetup.class) {
                if (fragment == null)
                    fragment = new FragmentSetup();
            }
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bonus, container, false);
        ButterKnife.bind(this, view);
        Log.e(TAG, "onCreateView: Setup");
        return view;
    }

}
