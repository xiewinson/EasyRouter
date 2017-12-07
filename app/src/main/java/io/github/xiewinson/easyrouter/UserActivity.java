package io.github.xiewinson.easyrouter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EasyRouter.injectParams(this);

        Context context = this;

        EasyRouter.activity(Uri.parse("app://home/discovery/trade?id=257&name=winson"))
                .withParam("p0", "winson")
                .build()
                .navigation(context);


    }
}
