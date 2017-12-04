package io.github.xiewinson.easyrouter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.type.TypeMirror;
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
    private String routerTableName;
    private List<String> paths = new ArrayList<>();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.messager = processingEnvironment.getMessager();
        this.environment = processingEnvironment;
        filer = processingEnvironment.getFiler();
        typeUtl = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();

        routerTableName = TextUtil.upperCaseFirstChar(processingEnvironment.getOptions().get(Constants.MODULE_NAME))
                + Constants.ROUTER_TABLE;
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
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
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
        ClassName easyRouterClsName = ClassName.get("", routerTableName);
        TypeSpec.Builder easyRouter = TypeSpec.classBuilder(easyRouterClsName)
                .addSuperinterface(ClassName.get(Constants.LIBRARY_PACKAGE_NAME, Constants.I_ROUTER_TABLE))
                .addField(FieldSpec.builder(easyRouterClsName, "instance", Modifier.PRIVATE, Modifier.STATIC).build())
                .addMethod(MethodSpec.methodBuilder("getInstance")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED)
                        .returns(easyRouterClsName)
                        .beginControlFlow("if(instance == null)")
                        .addStatement("instance = new $L()", easyRouterClsName.simpleName())
                        .endControlFlow()
                        .addStatement("return instance")
                        .build())
////                .addMethod(MethodSpec.methodBuilder("injectIntentParams")
////                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
////                        .addParameter(TypeName.OBJECT, "obj")
////                        .returns(void.class)
////                        .addStatement("getInstance().injectIntentParamsInternal(obj)")
//                        .build())
                .addModifiers(Modifier.PUBLIC);

        //急需putRoutes方法
        MethodSpec.Builder putRoutesBuilder = MethodSpec.methodBuilder(Constants.PUT_ROUTES)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class),
                        ClassName.get(String.class),
                        ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class))),
                        "map");

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

            String key = path;
            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                key = Constants.ACTIVITY_PREFIX + path;
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                key = Constants.FRAGMENT_PREFIX + path;
            }

            if (paths.contains(key.toLowerCase())) {
                error("不能在注解中声明相同名称的path(不区分大小写)");
                return;
            } else {
                paths.add(key.toLowerCase());
            }

            putRoutesBuilder.addStatement("map.put($S, $T.class)", key, routerClassElem.asType());

            ClassName intent = ClassName.get("android.content", "Intent");
            ClassName context = ClassName.get("android.content", "Context");

            //建立Activity和Fragment的Builder类
            ClassName innerClsName = ClassName.get("", TextUtil.path2ClassName(path) + "Builder");
            TypeSpec.Builder requestBuilder = TypeSpec.classBuilder(innerClsName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                requestBuilder
                        .superclass(ParameterizedTypeName.get(ClassName.get(Constants.LIBRARY_PACKAGE_NAME + Constants.DOT + Constants.ACTIVITY_REQUEST, Constants.BUILDER), innerClsName))
                        .addMethod(
                                MethodSpec.constructorBuilder()
                                        .addModifiers(Modifier.PRIVATE)
                                        .addStatement("super($T.class)", routerClassElem.asType())
                                        .build());
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                requestBuilder
                        .superclass(ParameterizedTypeName
                                .get(ClassName.get(Constants.LIBRARY_PACKAGE_NAME + Constants.DOT + Constants.FRAGMENT_REQUEST, Constants.BUILDER),
                                        ClassName.get(routerClassElem.asType()), innerClsName))
                        .addMethod(
                                MethodSpec.constructorBuilder()
                                        .addModifiers(Modifier.PRIVATE)
                                        .addStatement("super($L.class)", routerClassElem.getSimpleName())
                                        .build());
            }

            //intent/arguments参数解析
            TypeSpec.Builder targetParamInjector = TypeSpec.classBuilder(routerClassElem.getSimpleName().toString() + Constants._INTENT_PARAM_INJECTOR);
            routerClassElem.getSimpleName();
            TypeName target = ClassName.get(routerClassElem.asType());
            MethodSpec.Builder activityConstructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(target, "target");
            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                activityConstructor.addStatement("$L intent = target.getIntent()", intent.toString())
                        .addStatement("android.os.Bundle extras = intent.getExtras()");
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
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
                    TypeMirror paramType = var.asType();
                    TypeName paramTypeName = ClassName.get(paramType);
                    MethodSpec.Builder innerMethed = MethodSpec.methodBuilder(paramName)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(paramTypeName.isBoxedPrimitive() ? paramTypeName.unbox() : paramTypeName, paramAlias)
                            .returns(innerClsName);

                    //添加extra
                    innerMethed.addStatement("getBundle().put$L($S, $L)", BundleStatementHelper.buildPutExtraStatement(processingEnv, paramType), paramAlias, paramAlias);
                    requestBuilder.addMethod(innerMethed.addStatement("return this").build());

                    //intent/arguments参数解析
                    activityConstructor.addStatement("Object $L = extras.get($S)", paramAlias, paramAlias);
                    activityConstructor.beginControlFlow("if($L != null) ", paramAlias);
                    if (TypeUtil.isParcelableArray(processingEnv, paramType.toString())) {
                        String arrayHoldClassName = TypeUtil.getArrayHoldClassName(paramType.toString());
                        activityConstructor
                                .addStatement("$L array = ($L)$L", TypeUtil.ARRAY_PARCELABLE, TypeUtil.ARRAY_PARCELABLE, paramAlias)
                                .addStatement("int length = array.length")
                                .addStatement("target.$L = new $L[length]", paramName, arrayHoldClassName)
                                .beginControlFlow("for(int i = 0; i < length; i++) ", TypeUtil.PARCELABLE)
                                .addStatement("target.$L[i] = ($L)array[i]", paramName, arrayHoldClassName)
                                .endControlFlow();
                    } else {
                        activityConstructor.addStatement("target.$L = ($L)$L", paramName, paramType, paramAlias);
                    }
                    activityConstructor.endControlFlow();

//                    activityConstructor.addStatement("target.$L = " + castStr + "extras.get$L($S)", paramName, BundleStatementHelper.buildPutExtraStatement(processingEnv, paramType), paramAlias);
                }
            }

            MethodSpec.Builder quickMethod = MethodSpec.methodBuilder(TextUtil.lowerCaseFirstChar(innerClsName.simpleName()))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(innerClsName);

            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                quickMethod
                        .addStatement("return new $T()", innerClsName);
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                quickMethod.addStatement("return new $T()", innerClsName);
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
            easyRouter.addMethod(putRoutesBuilder.build());
            easyRouter.addType(activityRouterBuilder.build());
            easyRouter.addType(fragmentRouterBuilder.build());
            JavaFile.builder(Constants.ROUTER_TABLE_PACKAGE_NAME, easyRouter.build()).build().writeTo(filer);
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
