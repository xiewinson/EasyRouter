package io.github.xiewinson.easyrouter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.xiewinson.easyrouter.annotation.Route;
import io.github.xiewinson.easyrouter.core.AppRouterTable;
import io.github.xiewinson.easyrouter.library.EasyRouter;
import io.github.xiewinson.easyrouter.library.Interceptor;
import io.github.xiewinson.easyrouter.library.callback.IntentListener;

@Route("/main")
public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final ArrayList<View> views = new ArrayList<>();
        TextView tv = new TextView(this);
        views.add(tv);
        findViewById(R.id.fab)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        AppRouterTable
//                                .activity()
//                                .userBuilder()
//                                .age(25)
//                                .id(123)
//                                .name("哈哈哈")
//                                .build()
//                                .navigation(MainActivity.this);
                        EasyRouter.activity("user").withParam("user_name", "谢豪").build().navigation(v.getContext());
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
