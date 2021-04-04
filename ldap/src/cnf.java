import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class cnf {
    //ldap
    public String username;
    public String password;
    public String ipAddress;
    public String domain;

    //db-con

    public String dbHost;
    public String dbPort;
    public String dbUser;
    public String dbPass;
    public String dbName;

    //sql

    public String schema_name;
    public String user_table_name;
    public String user_group_table_name;
    public String username_column_name;
    public String department_column_name;
    public String employeeNumber_column_name;
    public String postalCode_column_name;
    public String group_column_name;


    public cnf() throws IOException {
        File f = new File("../config/properties.conf");
        if (!(f.exists() && !f.isDirectory())) {
            f = new File("../properties.conf");
        }
        if (!(f.exists() && !f.isDirectory())) {
            f = new File("properties.conf");
        }
        Properties props = new Properties();
        props.load(new FileInputStream(f));


        username = props.getProperty("username", "Administrator");
        password = props.getProperty("password", "Kirilla111");
        ipAddress = props.getProperty("ipAddress", "192.168.0.105");
        domain = props.getProperty("domain", "MyDomain.ua");


        dbHost = props.getProperty("dbHost", "127.0.0.1");
        dbPort = props.getProperty("dbPort", "5432");
        dbUser = props.getProperty("dbUser", "postgres");
        dbPass = props.getProperty("dbPass", "2012");
        dbName = props.getProperty("dbName", "ldap");


        schema_name = props.getProperty("schema_name", "public");
        user_table_name = props.getProperty("user_table_name", "users");
        user_group_table_name = props.getProperty("user_group_table_name", "user_groups");
        username_column_name = props.getProperty("username_column_name", "cn");
        department_column_name = props.getProperty("department_column_name", "department");
        employeeNumber_column_name = props.getProperty("employeeNumber_column_name", "employeenumber");
        postalCode_column_name = props.getProperty("postalCode_column_name", "postalcode");
        group_column_name = props.getProperty("group_column_name", "group");
    }
}