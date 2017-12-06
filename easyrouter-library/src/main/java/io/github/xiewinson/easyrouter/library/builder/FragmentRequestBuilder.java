package io.github.xiewinson.easyrouter.library.builder;

import android.app.Fragment;

import io.github.xiewinson.easyrouter.library.request.FragmentRequest;

/**
 * Created by winson on 2017/12/5.
 */

public final class FragmentRequestBuilder extends FragmentRequest.Builder<Fragment, FragmentRequestBuilder> {
    public FragmentRequestBuilder(Class<Fragment> cls) {
        super(cls);
    }
}