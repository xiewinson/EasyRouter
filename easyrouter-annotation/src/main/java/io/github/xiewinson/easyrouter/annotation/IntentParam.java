package io.github.xiewinson.easyrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by winson on 2017/11/28.
 */


@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface IntentParam {
    String value() default "";
}
