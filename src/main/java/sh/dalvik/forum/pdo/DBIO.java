package sh.dalvik.forum.pdo;

import org.mariadb.jdbc.MariaDbConnection;
import org.mariadb.jdbc.MariaDbStatement;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DBIO {
    private static final String URL = "jdbc:mariadb://10.20.0.41:3306/forumcli";
    private static final String USER = "forumcli";
    private static final String PASSWORD = "9C6rwcUEyZzRWDcr";

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
        connection = (MariaDbConnection) DriverManager.getConnection(URL, USER, PASSWORD);
        statement = (MariaDbStatement) connection.createStatement();
    }

    public static class Builder {

        private String query = "";
        private Map<Integer, Object> parameters = new HashMap<>();

        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder setParameter(int position, Object parameter) {
            parameters.put(position, parameter);
            return this;
        }

        public PreparedStatement build() throws Exception {
            PreparedStatement ptmt = getInstance().connection.prepareStatement(getQuery());
            getParameters().forEach((k, v) -> {
                try {
                    if (v instanceof String) {
                        ptmt.setString(k, (String) v);
                    } else if (v instanceof Integer) {
                        ptmt.setInt(k, (int) v);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            return ptmt;
        }

        public ResultSet query() throws Exception {
            return build().executeQuery();
        }

        public boolean exec() throws Exception {
            return build().execute();
        }

        public String getQuery() {
            return query;
        }

        public Map<Integer, Object> getParameters() {
            return parameters;
        }
    }
}