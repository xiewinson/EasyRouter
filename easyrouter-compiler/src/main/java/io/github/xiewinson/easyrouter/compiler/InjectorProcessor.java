package io.github.xiewinson.easyrouter.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import io.github.xiewinson.easyrouter.annotation.Constants;
import io.github.xiewinson.easyrouter.annotation.Param;
import io.github.xiewinson.easyrouter.annotation.Route;

/**
 * Created by winson on 2017/12/6.
 */

@AutoService(Processor.class)
public class InjectorProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEv;
    private ClassName intent;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEv = processingEnvironment;

        intent = TypeUtil.getIntentClassName();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        handleElements(roundEnvironment, processingEv.getFiler());
        return true;
    }

    private void handleElements(RoundEnvironment roundEnvironment, Filer filer) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Route.class);
        if (elements.isEmpty()) return;

        for (Element routeClassElem : elements) {
            TypeUtil.RouterClass routerClassType = TypeUtil.getRouterClassType(processingEv, routeClassElem.asType());

            //intent/arguments参数解析
            TypeSpec.Builder targetParamInjector = TypeSpec.classBuilder(routeClassElem.getSimpleName().toString() + Constants._INTENT_PARAM_INJECTOR);
            routeClassElem.getSimpleName();
            TypeName target = ClassName.get(routeClassElem.asType());
            MethodSpec.Builder targetConstructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(target, "target");
            if (routerClassType == TypeUtil.RouterClass.ACTIVITY) {
                targetConstructor.addStatement("$L intent = target.getIntent()", intent.toString())
                        .addStatement("android.os.Bundle extras = intent.getExtras()");
            } else if (TypeUtil.isFragmentOrV4(routerClassType)) {
                targetConstructor.addStatement("android.os.Bundle extras = target.getArguments()");
            } else {
                targetConstructor.addParameter(intent, "intent");
                targetConstructor.addStatement("android.os.Bundle extras = intent.getExtras()");
            }


            for (Element var : routeClassElem.getEnclosedElements()) {
                if (var.getKind() != ElementKind.FIELD) continue;
                Param param = var.getAnnotation(Param.class);
                if (param == null) continue;

                String paramName = var.getSimpleName().toString();
                String paramAlias = param.value();
                paramAlias = paramAlias.length() == 0 ? paramName : paramAlias;

                TypeMirror paramType = var.asType();
                TypeName paramTypeName = ClassName.get(paramType);

                //intent/arguments参数解析
                targetConstructor.addStatement("Object $L = extras.get($S)", paramAlias, paramAlias);
                targetConstructor.beginControlFlow("if($L != null) ", paramAlias);
                if (TypeUtil.isParcelableArray(processingEnv, paramType.toString())) {
                    String arrayHoldClassName = TypeUtil.getArrayHoldClassName(paramType.toString());
                    targetConstructor
                            .addStatement("$L array = ($L)$L", TypeUtil.ARRAY_PARCELABLE, TypeUtil.ARRAY_PARCELABLE, paramAlias)
                            .addStatement("int length = array.length")
                            .addStatement("target.$L = new $L[length]", paramName, arrayHoldClassName)
                            .beginControlFlow("for(int i = 0; i < length; i++) ", TypeUtil.PARCELABLE)
                            .addStatement("target.$L[i] = ($L)array[i]", paramName, arrayHoldClassName)
                            .endControlFlow();
                } else {
                    targetConstructor.addStatement("target.$L = ($L)$L", paramName, paramType, paramAlias);
                }
                targetConstructor.endControlFlow();
//                    activityConstructor.addStatement("target.$L = " + castStr + "extras.get$L($S)", paramName, BundleStatementHelper.buildPutExtraStatement(processingEnv, paramType), paramAlias);

                try {
                    //intent/arguments参数解析
                    targetParamInjector.addMethod(targetConstructor.build());
                    JavaFile.builder(target.toString().replace("." + routeClassElem.getSimpleName().toString(), ""),
                            targetParamInjector.build()).build().writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
