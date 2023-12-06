package io.github.dengchen2020.mybatis.extension.annotation;

import java.lang.annotation.*;

/**
 * 指定别名
 * @author dengchen
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AS {

    String value() default "";

}
