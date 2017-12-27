package io.github.xiewinson.easyrouter;

import android.app.Application;
import android.net.Uri;
import android.support.v4.app.Fragment;

import dalvik.system.DexFile;
import io.github.xiewinson.easyrouter.core.AppRouterTable;
import io.github.xiewinson.easyrouter.core.MlRouterTable;
import io.github.xiewinson.easyrouter.fragment.StudentFragment;
import io.github.xiewinson.easyrouter.library.EasyRouter;

/**
 * Created by winson on 2017/12/4.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        EasyRouter.init(AppRouterTable.class, MlRouterTable.class);
    }
}
