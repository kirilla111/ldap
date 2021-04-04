import java.util.ArrayList;

public class person {
    private String postalCode;
    private String employeeNumber;
    private String CN;
    private String department;
    private ArrayList<String> groups;

    public person(){
        groups = new ArrayList<String>();
    }

    public String getCN() {
        return CN;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setGroup(String group){
        this.groups.add(group);
    }

    public ArrayList<String> getAllGropus(){
        return this.groups;
    }
    public String getGroup(int i){
        return groups.get(i);
    }
    public boolean isEmpty(){
        return groups.isEmpty();
    }
}
