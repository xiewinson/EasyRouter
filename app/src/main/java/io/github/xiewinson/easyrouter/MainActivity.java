package io.github.xiewinson.easyrouter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.core.EasyRouterManager;


@Router(path = "/main")
public class MainActivity extends AppCompatActivity {
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

        final HashMap<U, String> map = new HashMap<>();
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                EasyRouter.activity().mainBuilder(MainActivity.this).build();
//                EasyRouter
//                        .userBuilder(MainActivity.this)
////                        .images(views)
//                        .name("哈哈哈")
////                        .is(map)
//                        .build()
//                        .navigation();
//                new EasyRouter.ActivityRouter().mainBuilder();
                EasyRouterManager.activity().userBuilder(MainActivity.this).bitmap(null).build();
                EasyRouterManager.fragment().studentFragmentBuilder().bulid().asFragment();
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
