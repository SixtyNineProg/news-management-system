package by.klimov.commentservice.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtil {

  public static <T extends Annotation> List<String> getAnnotatedFieldNames(
      Class<?> searchableClass, Class<T> annotationClass) {
    Field[] fields = searchableClass.getDeclaredFields();

    return Arrays.stream(fields)
        .filter(field -> field.getAnnotation(annotationClass) != null)
        .map(Field::getName)
        .toList();
  }
}
