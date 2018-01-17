package io.github.xiewinson.easyrouter;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import io.github.xiewinson.easyrouter.annotation.Param;
import io.github.xiewinson.easyrouter.annotation.Route;
import io.github.xiewinson.easyrouter.library.EasyRouter;

@Route(value = "user", interceptors = {SimpleInterceptor.class})
public class UserActivity extends BaseActivity {

    @Param
    int age;

    @Param("user_name")
    String name;

    @Param
    long id;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EasyRouter.injectParams(this);

        Toast.makeText(this, "id->" + id + " name->" + name + " age->" + age, Toast.LENGTH_SHORT).show();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000000);

    }
}
