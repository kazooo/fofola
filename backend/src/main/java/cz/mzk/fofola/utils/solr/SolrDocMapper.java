package cz.mzk.fofola.utils.solr;

import cz.mzk.fofola.constants.AccessType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.common.SolrDocument;

import java.lang.reflect.Field;
import java.util.Date;

@Slf4j
public class SolrDocMapper {

    @SneakyThrows
    public static <T> T convert(SolrDocument doc, Class<T> clazz) {
        if (doc == null) {
            return null;
        }

        if (clazz == null) {
            throw new IllegalArgumentException("class of Solr document can't be null!");
        }

        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            boolean accessibility = field.canAccess(instance);
            field.setAccessible(true);

            if (field.isAnnotationPresent(SolrDocField.class)) {
                SolrDocField annotation = field.getAnnotation(SolrDocField.class);
                String name = annotation.value();
                Object value = doc.getFieldValue(name);
                field.set(instance, cast(value, field.getType()));
            }

            field.setAccessible(accessibility);
        }

        return instance;
    }

    public static <T> String getIdFieldName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(SolrDocId.class)) {
            SolrDocId annotation = clazz.getAnnotation(SolrDocId.class);
            return annotation.value();
        } else {
            throw new IllegalArgumentException("Class [" + clazz.getSimpleName() + "] doesn't have annotation SolrDocId!");
        }
    }

    @SneakyThrows
    public static <T> Object getValueByFieldName(T doc, String fieldName, Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(SolrDocField.class)) {
                SolrDocField annotation = field.getAnnotation(SolrDocField.class);
                String name = annotation.value();
                if (name.equals(fieldName)) {
                    return field.get(doc);
                }
            }
        }
        return null;
    }

    private static <T> T cast(final Object object, final Class<T> type) {
        try {
            if (object instanceof String && type == AccessType.class) {
                return (T) AccessType.of((String) object);
            }
            if (object instanceof Date && type == String.class) {
                return (T) object.toString();
            }
            return type.cast(object);
        } catch (ClassCastException | NullPointerException e) {
            log.warn(String.format("Can't cast object [%s] to type [%s]!", object, type));
            return null;
        }
    }
}
