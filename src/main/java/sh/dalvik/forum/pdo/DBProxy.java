package sh.dalvik.forum.pdo;

import org.mariadb.jdbc.MariaDbConnection;
import org.mariadb.jdbc.MariaDbStatement;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    private static Object process(Method m, Object... args) {
        Query query = m.getDeclaredAnnotation(Query.class);
        if(query != null) {
            return "YES";
        } else {
            throw new IllegalArgumentException("must include a Query annotation");
        }
    }

    private static class DBIO {

        public static final String URL = "jdbc:mysql://localhost:3306/imooc";
        public static final String USER = "liulx";
        public static final String PASSWORD = "123456";

        private static DBIO instance = null;
        private MariaDbConnection connection = null;
        private MariaDbStatement statement = null;

        public static DBIO getInstance() throws Exception {
            if (instance == null) {
                instance = new DBIO();
            }
            return instance;
        }

        private DBIO() throws Exception {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = (MariaDbConnection) DriverManager.getConnection("jdbc:mariadb://10.20.0.41:3306/forumcli", "forumcli", "9C6rwcUEyZzRWDcr");
            statement = (MariaDbStatement) connection.createStatement();
        }
    }
}
