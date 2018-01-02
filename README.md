# EasyRouter
* 使用步骤
在基础 module 或 application module 中引入基础 library 
```
dependencies {
   // implementation方式只在本module可见，若是在基础 library 中引入，请使用api方式
   implementation  project(':easyrouter-library') 
}
```

    
在有需要生成路由的 module 的 gradle 中, 引入compiler library, 指定 moduleName
```
dependencies {
      // 此处请使用 annotationProcessor 方式引用，compiler用于编译时生成类，不用将此部分打包
      annotationProcessor project(':easyrouter-compiler')
  
}
defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                // 需要指定模块名称，因为会根据此名称生成此模块的 RouterTable 类，取名按照 Java 命名规范即可，若 module 取名为 'App'，则生成文件名最终为 AppRouterTable
                arguments = [moduleName : 'App']
            }
        }
    }
```
在 APP 入口初始化
```
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 此AppRouterTable即为上一步骤所指定名称，
        EasyRouter.init(AppRouterTable.class, MlRouterTable.class);
    }
}

```
在 Activity/Service/Fragment 中可使用 Route 和 Param 注解
```
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
    }
}
```

