package io.github.xiewinson.easyrouter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.xiewinson.easyrouter.annotation.IntentParam;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.library.EasyRouter;

@Router(path = "/user")
public class UserActivity extends BaseActivity {

    @IntentParam
    int age;

    @IntentParam
    Integer age1;

    @IntentParam("user_name")
    String name;

    @IntentParam
    Bitmap bitmap;

    @IntentParam
    Bitmap[] data;

    @IntentParam
    ArrayList<Bitmap> images;

    @IntentParam
    U<Bitmap> u;

    @IntentParam
    HashMap<Integer, String> us;

//    @IntentParam
//    HashMap<U, String> is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyRouter.injectIntentParams(this);

        setContentView(R.layout.activity_user);
        Class<ArrayList> arrayListClass = ArrayList.class;
        List<View> zzz = new ArrayList<>();
        Type type = zzz.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
    }
}
