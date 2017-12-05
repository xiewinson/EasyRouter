package io.github.xiewinson.easyrouter.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.github.xiewinson.easyrouter.annotation.BundleParam;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.library.EasyRouter;

/**
 * Created by winson on 2017/11/30.
 */

@Router
public class StudentFragment extends Fragment {

    @BundleParam
    int sid;

    @BundleParam
    String schoolName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyRouter.injectParams(this);
    }
}
