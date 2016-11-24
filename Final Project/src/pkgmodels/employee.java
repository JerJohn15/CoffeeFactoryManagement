package pkgmodels;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author malware
 */
public class employee {

    private final IntegerProperty employeeid;
    private final IntegerProperty nationalid;
    private final StringProperty surname;
    private final StringProperty midname;
    private final StringProperty lastname;
    private final IntegerProperty mobileno;

    private final StringProperty jobCategory;
    private final DoubleProperty wage;
    private final StringProperty empstatus;

    public employee(Integer employeeid, Integer nationalid, String surname, String midname, String lastname, Integer mobileno, String jobCategory, Double wage, String empstatus) {
        this.employeeid = new SimpleIntegerProperty(employeeid);
        this.nationalid = new SimpleIntegerProperty(nationalid);
        this.surname = new SimpleStringProperty(surname);
        this.midname = new SimpleStringProperty(midname);
        this.lastname = new SimpleStringProperty(lastname);
        this.mobileno = new SimpleIntegerProperty(mobileno);
        this.jobCategory = new SimpleStringProperty(jobCategory);
        this.wage = new SimpleDoubleProperty(wage);
        this.empstatus = new SimpleStringProperty(empstatus);
    }

    public int getEmployeeid() {
        return employeeid.get();
    }

    public void setEmployeeid(int value) {
        employeeid.set(value);
    }

    public IntegerProperty employeeidProperty() {
        return employeeid;
    }

    public int getNationalid() {
        return nationalid.get();
    }

    public void setNationalid(int value) {
        nationalid.set(value);
    }

    public IntegerProperty nationalidProperty() {
        return nationalid;
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String value) {
        surname.set(value);
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public String getMidname() {
        return midname.get();
    }

    public void setMidname(String value) {
        midname.set(value);
    }

    public StringProperty midnameProperty() {
        return midname;
    }

    public String getLastname() {
        return lastname.get();
    }

    public void setLastname(String value) {
        lastname.set(value);
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public int getMobileno() {
        return mobileno.get();
    }

    public void setMobileno(int value) {
        mobileno.set(value);
    }

    public IntegerProperty mobilenoProperty() {
        return mobileno;
    }

    public String getJobCategory() {
        return jobCategory.get();
    }

    public void setJobCategory(String value) {
        jobCategory.set(value);
    }

    public StringProperty jobCategoryProperty() {
        return jobCategory;
    }

    public double getWage() {
        return wage.get();
    }

    public void setWage(double value) {
        wage.set(value);
    }

    public DoubleProperty wageProperty() {
        return wage;
    }

    public String getEmpstatus() {
        return empstatus.get();
    }

    public void setEmpstatus(String value) {
        empstatus.set(value);
    }

    public StringProperty empstatusProperty() {
        return empstatus;
    }

}
