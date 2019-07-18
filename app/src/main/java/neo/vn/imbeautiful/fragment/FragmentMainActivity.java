package neo.vn.imbeautiful.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.base.BaseFragment;
import neo.vn.imbeautiful.fragment.products.FragmentCommissionCTV;
import neo.vn.imbeautiful.models.ObjLogin;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 22-April-2019
 * Time: 10:30
 * Version: 1.0
 */
public class FragmentMainActivity extends BaseFragment {
    private static final String TAG = "FragmentMainActivity";
    public static FragmentMainActivity fragment;

    public static FragmentMainActivity getInstance() {
        if (fragment == null) {
            synchronized (FragmentMainActivity.class) {
                if (fragment == null)
                    fragment = new FragmentMainActivity();
            }
        }
        return fragment;
    }

    FragmentHome fragmentHome;
    FragmentOrder fragmentOrder;
    FragmentProduct fragmentProduct;
    FragmentCommissionsHome fragmentCommissions;
    FragmentCommissionCTV fragmentCommissions_CTV;
    Fragment fragmentCurrent;
    ObjLogin objLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);
        ButterKnife.bind(this, view);
        Log.e(TAG, "onCreateView: FragmentMainActivity");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // loadFragmentHome();
    }




}
