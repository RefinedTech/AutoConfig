package dev.refinedtech.configure.metadata.comment;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Comments.class)
public @interface Comment {

    String value();

}
