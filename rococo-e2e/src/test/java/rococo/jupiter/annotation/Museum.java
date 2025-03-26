package rococo.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Museum {
    String title() default "";
    String city() default "Москва";
    String description() default "Create by auto-test";
    String countryId() default "11f0003d-5d8f-20cd-969b-569bd7e2abf7";
}
