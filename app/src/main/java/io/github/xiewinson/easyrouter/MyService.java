package io.github.xiewinson.easyrouter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.github.xiewinson.easyrouter.annotation.Param;
import io.github.xiewinson.easyrouter.annotation.Route;
import io.github.xiewinson.easyrouter.library.EasyRouter;

@Route
public class MyService extends Service {
    @Param
    String taskName;

    @Param
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
