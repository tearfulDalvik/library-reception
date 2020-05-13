package sh.dalvik.forum.pdo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

public class DBProxy {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Query {
        String value() default "";
    }

    @SuppressWarnings("unchecked")
    public static <T> T proxy(Class<T> tClass) {
        if (!tClass.isInterface()) {
            throw new IllegalArgumentException("dao must be interfaces.");
        }
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),
                new Class[]{tClass}, (proxy, method, args) -> {
                    if (method.getDeclaringClass() == Object.class) {
                        return method.invoke(args);
                    }
                    args = args != null ? args : new Object[0];
                    return method.isDefault()
                            ? method.invoke(args)
                            : process(method, args);
                });
    }

    private static Object process(Method m, Object... args) throws Exception {
        Annotation[] annotation = m.getDeclaredAnnotations();
        if (annotation == null || annotation.length == 0) {
            throw new IllegalArgumentException("must include a Query annotation");
        }
        DBIO.Builder queryBuilder = new DBIO.Builder();
        for (Annotation a : annotation) {
            if (a instanceof Query) {
                queryBuilder.setQuery(((Query) a).value());
            }
        }
        for (int i = 0; i < m.getParameterCount(); i++) {
            queryBuilder.setParameter(i + 1, args[i]);
        }
        if (m.getReturnType() == ResultSet.class) {
            return queryBuilder.query();
        } else {
            return queryBuilder.exec();
        }
    }
}
