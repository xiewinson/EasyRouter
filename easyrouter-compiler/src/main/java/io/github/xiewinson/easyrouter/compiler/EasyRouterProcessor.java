package io.github.xiewinson.easyrouter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import io.github.xiewinson.easyrouter.annotation.BaseRouter;
import io.github.xiewinson.easyrouter.annotation.Param;
import io.github.xiewinson.easyrouter.annotation.Router;

//@AutoService(EasyRouterProcessor.class)
public class EasyRouterProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private Types typeUtl;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        typeUtl = processingEnvironment.getTypeUtils();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        handleElements(roundEnvironment, filer);
        return true;
    }

    private void handleElements(RoundEnvironment roundEnvironment, Filer filer) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        if (elements.isEmpty()) return;

        TypeSpec.Builder easyRouter = TypeSpec.classBuilder("EasyRouter")
                .superclass(BaseRouter.class)
                .addModifiers(Modifier.PUBLIC);

        for (Element element : elements) {
            Router annotation = element.getAnnotation(Router.class);
            String path = annotation.path();
            ClassName intent = ClassName.get("android.content", "Intent");
            ClassName context = ClassName.get("android.content", "Context");

            MethodSpec.Builder mBuilder = MethodSpec.methodBuilder(path.replace("/", ""))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(context, "context")
                    .returns(intent)
                    .addStatement("Intent intent = new Intent(" + "context, " + element.asType() + ".class)");

            TypeSpec.Builder targetBinding = TypeSpec.classBuilder(element.getSimpleName().toString() + "_" + "IntentBinding");
            TypeName target = ClassName.get(element.asType());
            MethodSpec.Builder activityConstructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(target, "target")
                    .addStatement(intent.toString() + " intent = target.getIntent()");

            for (Element var : element.getEnclosedElements()) {
                Param param = var.getAnnotation(Param.class);
                if (param != null) {
                    String paramName = var.getSimpleName().toString();
                    String paramAlias = param.value();
                    paramAlias = paramAlias.length() == 0 ? paramName : paramAlias;

                    TypeName typeName = ClassName.get(var.asType());
                    mBuilder.addParameter(typeName, paramAlias);
                    mBuilder.addStatement("intent.putExtra(\"" + paramAlias + "\", " + paramAlias + ")");

                    activityConstructor.addStatement("Object " + paramAlias + " = intent.getExtras().get(\"" + paramAlias + "\")");
                    activityConstructor.addStatement("if(" + paramAlias + " != null) target." + paramName + " = (" + var.asType() + ")" + paramAlias);
                }
            }
            try {
                targetBinding.addMethod(activityConstructor.build());
                JavaFile.builder(target.toString().replace("." + element.getSimpleName().toString(), ""),
                        targetBinding.build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }


            mBuilder.addStatement("return intent");
            easyRouter.addMethod(mBuilder.build());
        }
        try {
            JavaFile.builder("io.github.xiewinson.easyrouter.router", easyRouter.build()).build().writeTo(filer);
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
}
