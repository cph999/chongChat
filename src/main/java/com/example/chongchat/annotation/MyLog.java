package com.example.chongchat.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 崇鹏豪
 * @Date: 2022/08/02/17:18
 * @Description:
 */
@Retention(RetentionPolicy.SOURCE)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
public @interface MyLog {
    String value() default "";
}
