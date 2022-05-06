package de.articdive.articdata.datagen.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GenerationInfos.class)
public @interface GeneratorEntry {
    String name();

    String description() default "";

    boolean supported();
}

