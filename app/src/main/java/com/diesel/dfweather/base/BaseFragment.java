package com.diesel.dfweather.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

/**
 * Comments：
 *
 * @author wangj
 *         Time: 2016/8/13
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }
}
