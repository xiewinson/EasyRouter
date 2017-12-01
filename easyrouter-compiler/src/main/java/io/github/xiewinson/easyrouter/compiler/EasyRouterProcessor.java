package io.github.xiewinson.easyrouter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import io.github.xiewinson.easyrouter.annotation.Constants;
import io.github.xiewinson.easyrouter.annotation.IntentParam;
import io.github.xiewinson.easyrouter.annotation.Router;

//@AutoService(EasyRouterProcessor.class)
public class EasyRouterProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private Types typeUtl;
    private Elements elementUtils;
    private ProcessingEnvironment environment;

    private List<String> paths = new ArrayList<>();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.messager = processingEnvironment.getMessager();
        this.environment = processingEnvironment;
        print(processingEnvironment.getOptions().toString());
        filer = processingEnvironment.getFiler();
        typeUtl = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        handleElements(roundEnvironment, filer);
        paths.clear();
        return true;
    }

    private TypeSpec.Builder buildInnerRouter(String innerRouterClsName) {
        ClassName className = ClassName.get("", innerRouterClsName);
        return TypeSpec.classBuilder(innerRouterClsName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
                .addField(FieldSpec.builder(className, "instance", Modifier.PRIVATE, Modifier.STATIC).build())
                .addMethod(MethodSpec.methodBuilder("getInstance")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED)
                        .returns(className)
                        .beginControlFlow("if(instance == null)")
                        .addStatement("instance = new $L()", innerRouterClsName)
                        .endControlFlow()
                        .addStatement("return instance")
                        .build());

    }

    private void handleElements(RoundEnvironment roundEnvironment, Filer filer) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        if (elements.isEmpty()) return;

        //建立EasyRouter类
        ClassName easyRouterClsName = ClassName.get(Constants.PACKAGE_NAME, Constants.EASY_ROUTER_MANAGER);
        TypeSpec.Builder easyRouter = TypeSpec.classBuilder(easyRouterClsName)
                .superclass(ClassName.get(Constants.LIBRARY_PACKAGE_NAME, Constants.BASE_EASY_ROUTER))
                .addField(FieldSpec.builder(easyRouterClsName, "instance", Modifier.PRIVATE, Modifier.STATIC).build())
                .addMethod(MethodSpec.methodBuilder("getInstance")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED)
                        .returns(easyRouterClsName)
                        .beginControlFlow("if(instance == null)")
                        .addStatement("instance = new $L()", easyRouterClsName.simpleName())
                        .endControlFlow()
                        .addStatement("return instance")
                        .build())
                .addMethod(MethodSpec.methodBuilder("injectIntentParams")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(TypeName.OBJECT, "obj")
                        .returns(void.class)
                        .addStatement("getInstance().injectIntentParamsInternal(obj)")
                        .build())
                .addModifiers(Modifier.PUBLIC);

        //ActivityRouter、FragmentRouter
        TypeSpec.Builder activityRouterBuilder = buildInnerRouter(Constants.ACTIVITY_ROUTER);
        TypeSpec.Builder fragmentRouterBuilder = buildInnerRouter(Constants.FRAGMENT_ROUTER);

        easyRouter.addMethod(MethodSpec.methodBuilder("activity")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .returns(ClassName.get("", Constants.ACTIVITY_ROUTER))
                .addStatement("return $L.getInstance()", Constants.ACTIVITY_ROUTER)
                .build());

        easyRouter.addMethod(MethodSpec.methodBuilder("fragment")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .returns(ClassName.get("", Constants.FRAGMENT_ROUTER))
                .addStatement("return $L.getInstance()", Constants.FRAGMENT_ROUTER)
                .build());

        for (Element routerClassElem : elements) {

            if (routerClassElem.getKind() != ElementKind.CLASS) continue;

            TypeUtil.RouterClass routerClassType = TypeUtil.getRouterClassType(processingEnv, routerClassElem.asType());
            if (routerClassType == TypeUtil.RouterClass.OTHRER) continue;

            Router annotation = routerClassElem.getAnnotation(Router.class);
            String path = annotation.path();
            if (TextUtil.isEmpty(path)) {
                path = routerClassElem.getSimpleName().toString();
            }

            if (paths.contains(path.toLowerCase())) {
                error("不能在注解中声明相同名称的path(不区分大小写)");
                return;
            } else {
                paths.add(path.toLowerCase());
            }

            ClassName intent = ClassName.get("android.content", "Intent");
            ClassName context = ClassName.get("android.content", "Context");


            //建立Activity和Fragment的Builder类
            ClassName innerClsName = ClassName.get("", TextUtil.path2ClassName(path) + "Builder");
            TypeSpec.Builder requestBuilder = TypeSpec.classBuilder(innerClsName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                requestBuilder
                        .superclass(ClassName.get(Constants.LIBRARY_PACKAGE_NAME + Constants.DOT + Constants.ACTIVITY_REQUEST, Constants.BUILDER))
                        .addMethod(
                                MethodSpec.constructorBuilder()
                                        .addModifiers(Modifier.PRIVATE)
                                        .addParameter(context, "context")
                                        .addStatement("super(context, new $L(context, $L.class))", intent.reflectionName(), routerClassElem.asType())
                                        .build());
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                requestBuilder
                        .superclass(ParameterizedTypeName
                                .get(ClassName.get(Constants.LIBRARY_PACKAGE_NAME + Constants.DOT + Constants.FRAGMENT_REQUEST, Constants.BUILDER),
                                        ClassName.get(routerClassElem.asType())))
                        .addMethod(
                                MethodSpec.constructorBuilder()
                                        .addModifiers(Modifier.PRIVATE)
                                        .addStatement("super($L.class, new android.os.Bundle())", routerClassElem.getSimpleName())
                                        .build());
            }

            //intent/arguments参数解析
            TypeSpec.Builder targetParamInjector = TypeSpec.classBuilder(routerClassElem.getSimpleName().toString() + Constants._INTENT_PARAM_INJECTOR);
            routerClassElem.getSimpleName();
            TypeName target = ClassName.get(routerClassElem.asType());
            MethodSpec.Builder activityConstructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(target, "target");
            if (TypeUtil.isActivity(processingEnv, routerClassElem.asType())) {
                activityConstructor.addStatement("$L intent = target.getIntent()", intent.toString())
                        .addStatement("android.os.Bundle extras = intent.getExtras()");
            } else if (TypeUtil.isFragment(processingEnv, routerClassElem.asType()) || TypeUtil.isFragmentV4(processingEnv, routerClassElem.asType())) {
                activityConstructor.addStatement("android.os.Bundle extras = target.getArguments()");

            } else {
                error("inject intent param target must be activity or fragment");
            }

            for (Element var : routerClassElem.getEnclosedElements()) {
                if (var.getKind() != ElementKind.FIELD) continue;
                IntentParam param = var.getAnnotation(IntentParam.class);
                if (param != null) {
                    String paramName = var.getSimpleName().toString();
                    String paramAlias = param.value();
                    paramAlias = paramAlias.length() == 0 ? paramName : paramAlias;

                    //生成放置参数的方法
                    MethodSpec.Builder innerMethed = MethodSpec.methodBuilder(paramName)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(ClassName.get(var.asType()), paramAlias)
                            .returns(innerClsName);

                    //添加extra
                    if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                        innerMethed.addStatement("getIntent().putExtra($S, $L)", paramAlias, paramAlias);
                    } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                    }
                    requestBuilder.addMethod(innerMethed.addStatement("return this").build());

                    //intent/arguments参数解析
                    activityConstructor
                            .addStatement("Object $L = extras.get($S)", paramAlias, paramAlias)
                            .beginControlFlow("if($L != null) ", paramAlias)
                            .addStatement("target.$L = ($L)$L", paramName, var.asType(), paramAlias)
                            .endControlFlow();
                }
            }

            MethodSpec.Builder quickMethod = MethodSpec.methodBuilder(TextUtil.lowerCaseFirstChar(innerClsName.simpleName()))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(innerClsName);

            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                quickMethod
                        .addParameter(context, "context")
                        .addStatement("return new $L(context)", innerClsName.simpleName());
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                quickMethod.addStatement("return new $L()", innerClsName.simpleName());

            }

            //构建Builder类的快捷方法
            TypeSpec requestBulder = requestBuilder.build();
            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                activityRouterBuilder.addMethod(quickMethod.build());
                activityRouterBuilder.addType(requestBulder);
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                fragmentRouterBuilder.addMethod(quickMethod.build());
                fragmentRouterBuilder.addType(requestBulder);
            }
            try {
                //intent/arguments参数解析
                targetParamInjector.addMethod(activityConstructor.build());
                JavaFile.builder(target.toString().replace("." + routerClassElem.getSimpleName().toString(), ""),
                        targetParamInjector.build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            JavaFile.builder(easyRouterClsName.packageName(), activityRouterBuilder.build()).build().writeTo(filer);
            JavaFile.builder(easyRouterClsName.packageName(), fragmentRouterBuilder.build()).build().writeTo(filer);
            JavaFile.builder(easyRouterClsName.packageName(), easyRouter.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Router.class.getCanonicalName());
        return set;
    }

    private void print(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void error(String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
