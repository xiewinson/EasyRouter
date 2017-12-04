package io.github.xiewinson.easyrouter.compiler;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

/**
 * Created by winson on 2017/11/30.
 */

public class BundleStatementHelper {
    public static String buildPutExtraStatement(ProcessingEnvironment environment, TypeMirror typeMirror) {
        StringBuilder sb = new StringBuilder();
        sb.append(getMethodByType(environment, typeMirror));
        return sb.toString();
    }

    private static String getMethodByType(ProcessingEnvironment environment, TypeMirror typeMirror) {
        switch (TypeUtil.getType(environment, typeMirror)) {
            case INT:
                return "Int";
            case LONG:
                return "Long";
            case FLOAT:
                return "Float";
            case DOUBLE:
                return "Double";
            case SHORT:
                return "Short";
            case BYTE:
                return "Byte";
            case BOOLEAN:
                return "Boolean";
            case STRING:
                return "String";
            case CHAR_SEQUENCE:
                return "CharSequence";
            case ARRAY_INT:
                return "IntArray";
            case ARRAY_LONG:
                return "LongArray";
            case ARRAY_FLOAT:
                return "FloatArray";
            case ARRAY_DOUBLE:
                return "DoubleArray";
            case ARRAY_SHORT:
                return "ShortArray";
            case ARRAY_BYTE:
                return "ByteArray";
            case ARRAY_BOOLEAN:
                return "BooleanArray";
            case ARRAY_STRING:
                return "StringArray";
            case ARRAY_CHAR_SEQUENCE:
                return "CharSequenceArray";
            case SIZE:
                return "Size";
            case SIZEF:
                return "SizeF";
            case ARRAY_LIST_INTEGER:
                return "IntegerArrayList";
            case ARRAY_LIST_STRING:
                return "StringArrayList";
            case ARRAY_LIST_CHAR_SEQUENCE:
                return "CharSequenceArrayList";

            case BUNDLE:
                return "Bundle";
            case PARCELABLE:
                return "Parcelable";
            case ARRAY_PARCELABLE:
                return "ParcelableArray";
            case ARRAY_LIST_PARCELABLE:
                return "ParcelableArrayList";
            case SERIALIZABLE:
                return "Serializable";
        }
        return "";
    }
}
