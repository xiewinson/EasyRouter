package io.github.xiewinson.easyrouter.fragment;


import android.support.v4.app.Fragment;

import io.github.xiewinson.easyrouter.annotation.BundleParam;
import io.github.xiewinson.easyrouter.annotation.Router;

/**
 * Created by winson on 2017/11/30.
 */

@Router()
public class StudentFragment extends Fragment {

    @BundleParam
    int sid;

    @BundleParam
    String schoolName;
}
