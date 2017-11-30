package io.github.xiewinson.easyrouter.compiler;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by winson on 2017/11/30.
 */

public class TypeUtil {
    public static boolean isSubType(ProcessingEnvironment processingEnvironment, TypeMirror subType, String parentClassName) {
        TypeElement parentType = processingEnvironment.getElementUtils().getTypeElement(parentClassName);
        return parentType != null && processingEnvironment.getTypeUtils().isSubtype(subType, parentType.asType());
    }

    public static boolean isActivity(ProcessingEnvironment processingEnvironment, TypeMirror subType) {
        return isSubType(processingEnvironment, subType, "android.app.Activity");
    }


    public static boolean isFragment(ProcessingEnvironment processingEnvironment, TypeMirror subType) {
        return isSubType(processingEnvironment, subType, "android.app.Fragment");
    }

    public static boolean isFragmentV4(ProcessingEnvironment processingEnvironment, TypeMirror subType) {
        return isSubType(processingEnvironment, subType, "android.support.v4.app.Fragment");
    }

    public static RouterClass getRouterClassType(ProcessingEnvironment processingEnvironment, TypeMirror subType) {
        if (isActivity(processingEnvironment, subType)) return RouterClass.ACTIVITY;
        if (isFragment(processingEnvironment, subType)) return RouterClass.FRAGMENT;
        if (isFragmentV4(processingEnvironment, subType)) return RouterClass.FRAGMENT_V4;
        return RouterClass.OTHRER;
    }

    public enum RouterClass {
        ACTIVITY,
        FRAGMENT,
        FRAGMENT_V4,
        OTHRER
    }
}
