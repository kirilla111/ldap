import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ldap {
    private DirContext authContext;
    private ArrayList<person> people = new ArrayList<person>();
    private cnf conf;

    private void getConnection() throws NamingException {
        System.out.println("CONNECTING TO LDAP..");
        Hashtable<String, String> environment = new Hashtable<String, String>();

        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, "ldap://" + conf.ipAddress + ":389");
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, conf.username + "@" + conf.domain);
        environment.put(Context.SECURITY_CREDENTIALS, conf.password);

        authContext = new InitialDirContext(environment);
    }

    private final boolean getAllUsers(final DirContext dirContext,
                                      final String searchFilter,
                                      final String searchBase) throws NamingException {

        boolean retVal = false;

        final SearchControls constraints = new SearchControls();

        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        final NamingEnumeration<?> searchResults = dirContext.search(searchBase, searchFilter, constraints);

        while (searchResults != null && searchResults.hasMoreElements()) {
            retVal = true;
            person person_ = new person();

            final SearchResult searchResult = (SearchResult) searchResults.next();

            Attributes attr = searchResult.getAttributes();

            try {
                person_.setCN(attr.get("CN").get(0).toString());
                System.out.println(person_.getCN());
            } catch (Exception e) {
                person_.setCN("");
            }
            try {
                person_.setDepartment(attr.get("department").get(0).toString());
                System.out.println(person_.getDepartment());
            } catch (Exception e) {
                person_.setDepartment("");
            }
            try {
                person_.setEmployeeNumber(attr.get("employeeNumber").get(0).toString());
                System.out.println(person_.getEmployeeNumber());
            } catch (Exception e) {
                person_.setEmployeeNumber("");
            }
            try {
                person_.setPostalCode(attr.get("postalCode").get(0).toString());
                System.out.println(person_.getPostalCode());
            } catch (Exception e) {
                person_.setPostalCode("");
            }
            person_ = getGroups("memberOf", searchResult.getAttributes(),person_);
            people.add(person_);
        }

        return retVal;
    }
    private static person getGroups(String attrName, final Attributes attributes, person person_) throws NamingException{

        if (attributes!=null){
            for (NamingEnumeration enums = attributes.getAll(); enums.hasMore();) {
                final Attribute attribute = (Attribute)enums.next();

                if(attribute.getID().equals(attrName)){
                    for (NamingEnumeration namingEnu = attribute.getAll();namingEnu.hasMore();) {
                        String group = ("" + namingEnu.next()).split(",")[0];
                        group = group.replace("CN=","");
                            person_.setGroup(group);
                    }
                    break;
                }
            }
        }
        return person_;
    }

    public static void main(String[] args) {
        ldap myLdap = new ldap();
        try {
            myLdap.conf = new cnf();
            System.out.println("READING CONF FILE..");
        } catch (IOException e) {
            System.out.println("CONF FILE DONT EXIST");
            e.printStackTrace();
            return;
        }
        try {
            myLdap.getConnection();
            String[] dmn = myLdap.conf.domain.split("\\.");
            String searchBase = "dc="+dmn[0];
            for (int i = 1; i < dmn.length; i++) {
                searchBase+=" ,dc="+dmn[i];
            }
            if (myLdap.getAllUsers(myLdap.authContext,
                    "(&(objectCategory=person)(objectClass=user)(SAMAccountName=*))",
                    searchBase)) {

            } else {
                System.out.println("RESULT NOT FOUND ");
            }
        } catch (NamingException e) {
            System.out.println("LDAP Connection: FAILED");
            e.printStackTrace();
            return;
        }
        DB db = new DB();
        try {
            System.out.println("CONNECTING TO DATABASE..");
            db.getDbConnection(myLdap.conf.dbHost,myLdap.conf.dbPort,myLdap.conf.dbName,myLdap.conf.dbUser,myLdap.conf.dbPass);
            System.out.println("CONNECTED TO DB");
            try{
                System.out.println("INSERTING DATA..");
                db.insertUsers(myLdap.people,myLdap.conf.user_table_name,myLdap.conf.username_column_name,
                        myLdap.conf.department_column_name,myLdap.conf.employeeNumber_column_name,myLdap.conf.postalCode_column_name,myLdap.conf.schema_name);

                db.insertGroups(myLdap.people,myLdap.conf.username_column_name,myLdap.conf.user_group_table_name,myLdap.conf.group_column_name,myLdap.conf.schema_name);
                System.out.println("INSERTING DATA COMPLETE");
            } catch (SQLException e) {
                System.out.println("INSERTING FAILED");
                e.printStackTrace();
                return;
            }
        } catch (SQLException throwables) {
            System.out.println("CONNECTION TO DATABASE FAILED");
            throwables.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            System.out.println("CONNECTION TO DATABASE FAILED");
            e.printStackTrace();
            return;
        }


    }
}
