package io.github.xiewinson.easyrouter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.github.xiewinson.easyrouter.annotation.BundleParam;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.library.EasyRouter;

@Router
public class MyService extends Service {
    @BundleParam
    String taskName;

    @BundleParam
    long taskId;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EasyRouter.injectParams(this, intent);
        return super.onStartCommand(intent, flags, startId);
    }
}
