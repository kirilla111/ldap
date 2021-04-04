import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {

    private Connection dbConnection;

    public void getDbConnection(String dbHost, String dbPort, String dbName, String dbUser, String dbPass) throws ClassNotFoundException, SQLException { // подключение к бд
        Class.forName("org.postgresql.Driver");
        String connection = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
        dbConnection = DriverManager.getConnection(connection, dbUser, dbPass);
    }

    public void insertUsers(ArrayList<person> people, String table_name, String username_column_name,
                            String department_column_name, String employeeNumber_column_name, String postalCode_column_name, String schema) throws SQLException {
        Statement statement = dbConnection.createStatement();
        for (person prs : people) {
            String insert = "INSERT INTO " + schema + "." + table_name + "(" + username_column_name + ") VALUES ('" + prs.getCN() + "')";
            statement.execute(insert);
            if (prs.getDepartment() != "") {
                String update = "UPDATE " + table_name + " SET " + department_column_name + " = '" +
                        prs.getDepartment() + "' WHERE " + username_column_name + " = '" + prs.getCN() + "'";
                statement.execute(update);
            }
            if (prs.getEmployeeNumber() != "") {
                String update = "UPDATE " + table_name + " SET " + employeeNumber_column_name + " = '" +
                        prs.getEmployeeNumber() + "' WHERE " + username_column_name + " = '" + prs.getCN() + "'";
                statement.execute(update);
            }
            if (prs.getPostalCode() != "") {
                String update = "UPDATE " + table_name + " SET " + postalCode_column_name + " = '" +
                        prs.getPostalCode() + "' WHERE " + username_column_name + " = '" + prs.getCN() + "'";
                statement.execute(update);
            }
        }


    }

    public void insertGroups(ArrayList<person> people, String username_column_name, String table_name, String group_column_name, String schema) throws SQLException {
        Statement statement = dbConnection.createStatement();
        for (person prs : people) {
            if (!prs.isEmpty()) {
                for (int i = 0; i < prs.getAllGropus().size(); i++) {
                    String insert = "INSERT INTO " + schema + "." + table_name + "(\"" +
                            username_column_name + "\",\"" + group_column_name + "\") VALUES ('" + prs.getCN() + "','" + prs.getGroup(i) + "')";
                    statement.execute(insert);
                }
            }
        }
    }
}
