package io.github.xiewinson.easyrouter.library.inner;

import io.github.xiewinson.easyrouter.library.FragmentRequest;

/**
 * Created by winson on 2017/12/5.
 */

public final class FragmentV4RequestBuilder extends FragmentRequest.Builder<android.support.v4.app.Fragment, FragmentV4RequestBuilder> {
    public FragmentV4RequestBuilder(Class<android.support.v4.app.Fragment> cls) {
        super(cls);
    }
}

