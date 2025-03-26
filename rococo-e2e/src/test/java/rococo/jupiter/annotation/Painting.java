package rococo.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Painting {
    String title() default "";
    String description() default "Create by autotest!";
    Artist artist() default @Artist;
    Museum museum() default @Museum;
}
