package io.github.xiewinson.easyrouter.fragment;

import android.app.Fragment;

import io.github.xiewinson.easyrouter.annotation.IntentParam;
import io.github.xiewinson.easyrouter.annotation.Router;

/**
 * Created by winson on 2017/11/30.
 */

@Router()
public class StudentFragment extends Fragment {

    @IntentParam
    int sid;

    @IntentParam
    String schoolName;
}
