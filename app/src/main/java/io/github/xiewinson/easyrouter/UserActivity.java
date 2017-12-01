package io.github.xiewinson.easyrouter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.xiewinson.easyrouter.annotation.IntentParam;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.core.EasyRouterManager;

@Router(path = "user")
public class UserActivity extends AppCompatActivity {

    @IntentParam("user_name")
    String name;

    @IntentParam
    Bitmap bitmap;

    @IntentParam
    int[] data;

    @IntentParam
    ArrayList<View> images;

//    @IntentParam
//    HashMap<U, String> is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EasyRouterManager.injectIntentParams(this);
        Class<ArrayList> arrayListClass = ArrayList.class;
        List<View> zzz = new ArrayList<>();
        Type type = zzz.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();

        Bundle bundle;
        Log.d("winson", "result ->" + actualTypeArguments[0]);

    }
}
