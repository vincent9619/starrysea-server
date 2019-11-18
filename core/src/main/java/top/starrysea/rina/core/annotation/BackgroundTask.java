package top.starrysea.rina.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BackgroundTask {
	int time() default 1;
	TimeUnit timeUnit() default TimeUnit.SECONDS;
}
