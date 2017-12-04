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

    public static boolean isSubType(ProcessingEnvironment processingEnvironment, String subClassName, String parentClassName) {
        TypeElement subType = processingEnvironment.getElementUtils().getTypeElement(subClassName);
        TypeElement parentType = processingEnvironment.getElementUtils().getTypeElement(parentClassName);
        return subType != null && parentType != null && processingEnvironment.getTypeUtils().isSubtype(subType.asType(), parentType.asType());
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

    public static boolean isFragmentOrV4(RouterClass routerClass) {
        return routerClass == RouterClass.FRAGMENT || routerClass == RouterClass.FRAGMENT_V4;
    }

    public enum RouterClass {
        ACTIVITY,
        FRAGMENT,
        FRAGMENT_V4,
        OTHRER
    }

    private static final String JAVA_LANG = "java.lang";
    public static final String ARRAY_LIST = "java.util.ArrayList";

    public static final String INT = "int";
    public static final String INTEGER_BOX = JAVA_LANG + ".Integer";
    public static final String LONG = "long";
    public static final String LONG_BOX = JAVA_LANG + ".Long";
    public static final String FLOAT = "float";
    public static final String FLOAT_BOX = JAVA_LANG + ".Float";
    public static final String DOUBLE = "double";
    public static final String DOUBLE_BOX = JAVA_LANG + ".Double";
    public static final String SHORT = "short";
    public static final String SHORT_BOX = JAVA_LANG + ".Short";
    public static final String BYTE = "byte";
    public static final String BYTE_BOX = JAVA_LANG + ".Byte";
    public static final String BOOLEAN = "boolean";
    public static final String BOOLEAN_BOX = JAVA_LANG + ".Boolean";
    public static final String STRING = JAVA_LANG + ".String";
    public static final String CHAR_SEQUENCE = JAVA_LANG + ".CharSequence";

    public static final String ARRAY_INT = INT + "[]";
    public static final String ARRAY_LONG = LONG + "[]";
    public static final String ARRAY_FLOAT = FLOAT + "[]";
    public static final String ARRAY_DOUBLE = DOUBLE + "[]";
    public static final String ARRAY_SHORT = SHORT + "[]";
    public static final String ARRAY_BYTE = BYTE + "[]";
    public static final String ARRAY_BOOLEAN = BOOLEAN + "[]";
    public static final String ARRAY_STRING = STRING + "[]";
    public static final String ARRAY_CHAR_SEQUENCE = CHAR_SEQUENCE + "[]";

    public static final String ARRAY_LIST_INTEGER = ARRAY_LIST + "<" + INTEGER_BOX + ">";
    public static final String ARRAY_LIST_LONG = ARRAY_LIST + "<" + LONG_BOX + ">";
    public static final String ARRAY_LIST_FLOAT = ARRAY_LIST + "<" + FLOAT_BOX + ">";
    public static final String ARRAY_LIST_DOUBLE = ARRAY_LIST + "<" + DOUBLE_BOX + ">";
    public static final String ARRAY_LIST_SHORT = ARRAY_LIST + "<" + SHORT_BOX + ">";
    public static final String ARRAY_LIST_BYTE = ARRAY_LIST + "<" + BYTE_BOX + ">";
    public static final String ARRAY_LIST_BOOLEAN = ARRAY_LIST + "<" + BOOLEAN_BOX + ">";
    public static final String ARRAY_LIST_STRING = ARRAY_LIST + "<" + STRING + ">";
    public static final String ARRAY_LIST_CHAR_SEQUENCE = ARRAY_LIST + "<" + CHAR_SEQUENCE + ">";

    public static final String BUNDLE = "android.os.Bundle";
    public static final String PARCELABLE = "android.os.Parcelable";
    public static final String ARRAY_PARCELABLE = PARCELABLE + "[]";
    public static final String ARRAY_LIST_PARCELABLE = ARRAY_LIST + "<" + PARCELABLE + ">";

    public static final String SERIALIZABLE = "java.io.Serializable";

    public static TYPE_KIND getType(ProcessingEnvironment environment, TypeMirror typeMirror) {
        String tm = typeMirror.toString();
        System.out.println("当前 ->" + tm + "<-");
        if (tm.equals(INT) || tm.equals(INTEGER_BOX)) return TYPE_KIND.INT;
        if (tm.equals(LONG) || tm.equals(LONG_BOX)) return TYPE_KIND.LONG;
        if (tm.equals(FLOAT) || tm.equals(FLOAT_BOX)) return TYPE_KIND.FLOAT;
        if (tm.equals(DOUBLE) || tm.equals(DOUBLE_BOX)) return TYPE_KIND.DOUBLE;
        if (tm.equals(SHORT) || tm.equals(SHORT_BOX)) return TYPE_KIND.SHORT;
        if (tm.equals(BYTE) || tm.equals(BYTE_BOX)) return TYPE_KIND.BYTE;
        if (tm.equals(BOOLEAN) || tm.equals(BOOLEAN_BOX)) return TYPE_KIND.BOOLEAN;
        if (tm.equals(STRING)) return TYPE_KIND.STRING;
        if (tm.equals(CHAR_SEQUENCE)) return TYPE_KIND.CHAR_SEQUENCE;

        if (tm.equals(ARRAY_INT)) return TYPE_KIND.ARRAY_INT;
        if (tm.equals(ARRAY_LONG)) return TYPE_KIND.ARRAY_LONG;
        if (tm.equals(ARRAY_FLOAT)) return TYPE_KIND.ARRAY_FLOAT;
        if (tm.equals(ARRAY_DOUBLE)) return TYPE_KIND.ARRAY_DOUBLE;
        if (tm.equals(ARRAY_SHORT)) return TYPE_KIND.ARRAY_SHORT;
        if (tm.equals(ARRAY_BYTE)) return TYPE_KIND.ARRAY_BYTE;
        if (tm.equals(ARRAY_BOOLEAN)) return TYPE_KIND.ARRAY_BOOLEAN;
        if (tm.equals(ARRAY_STRING)) return TYPE_KIND.ARRAY_STRING;
        if (tm.equals(ARRAY_CHAR_SEQUENCE)) return TYPE_KIND.ARRAY_CHAR_SEQUENCE;

        if (tm.equals(ARRAY_LIST_INTEGER)) return TYPE_KIND.ARRAY_LIST_INTEGER;
        if (tm.equals(ARRAY_LIST_STRING)) return TYPE_KIND.ARRAY_LIST_STRING;
        if (tm.equals(ARRAY_LIST_CHAR_SEQUENCE)) return TYPE_KIND.ARRAY_LIST_CHAR_SEQUENCE;

        if (tm.equals(BUNDLE)) return TYPE_KIND.BUNDLE;

        if (tm.equals(PARCELABLE) || isSubType(environment, typeMirror, PARCELABLE)) {
            return TYPE_KIND.PARCELABLE;
        }

        if (tm.equals(ARRAY_PARCELABLE) || isSubType(environment, getArrayHoldClassName(tm), PARCELABLE)) {
            return TYPE_KIND.ARRAY_PARCELABLE;
        }
        if (tm.equals(ARRAY_LIST_PARCELABLE) || (tm.startsWith(ARRAY_LIST) && isSubType(environment, getGenericClassName(tm), PARCELABLE))) {
            return TYPE_KIND.ARRAY_LIST_PARCELABLE;
        }

        if (tm.equals(SERIALIZABLE) || isSubType(environment, typeMirror, SERIALIZABLE)) {
            return TYPE_KIND.SERIALIZABLE;
        }
        return TYPE_KIND.PARCELABLE;
    }

    public static String getGenericClassName(String clsName) {
        char[] chars = clsName.toCharArray();
        int start = 0;
        for (char aChar : chars) {
            start++;
            if (aChar == '<') {
                break;
            }
        }
        if (start >= chars.length) return clsName;
        return clsName.substring(start, clsName.length() - 1);
    }

    public static String getArrayHoldClassName(String clsName) {
        return clsName.replace("[]", "");
    }


    public enum TYPE_KIND {
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        SHORT,
        BYTE,
        BOOLEAN,
        STRING,
        CHAR_SEQUENCE,
        ARRAY_INT,
        ARRAY_LONG,
        ARRAY_FLOAT,
        ARRAY_DOUBLE,
        ARRAY_SHORT,
        ARRAY_BYTE,
        ARRAY_BOOLEAN,
        ARRAY_STRING,
        ARRAY_CHAR_SEQUENCE,

        ARRAY_LIST_INTEGER,
        ARRAY_LIST_STRING,
        ARRAY_LIST_CHAR_SEQUENCE,

        BUNDLE,
        PARCELABLE,
        ARRAY_PARCELABLE,
        ARRAY_LIST_PARCELABLE,
        SERIALIZABLE,
    }

}
