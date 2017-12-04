package io.github.xiewinson.easyrouter.library.callback;

import android.content.Intent;

/**
 * Created by winson on 2017/12/4.
 */

public interface NavigateCallback {
    void call(Intent intent, Exception e);
}
