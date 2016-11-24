package pkgmodels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class absentees {

    private final SimpleIntegerProperty empid;
    private final SimpleStringProperty names;
    private final SimpleStringProperty job;

    public absentees(Integer empid, String names, String job) {
        this.empid = new SimpleIntegerProperty(empid);
        this.names = new SimpleStringProperty(names);
        this.job = new SimpleStringProperty(job);
    }

    public int getEmpid() {
        return empid.get();
    }

    public void setEmpid(int emp_id) {
        empid.set(emp_id);
    }

    public IntegerProperty empidProperty() {
        return empid;
    }

    public String getNames() {
        return names.get();
    }

    public void getNames(String name_s) {
        names.set(name_s);
    }

    public StringProperty namesProperty() {
        return names;
    }

    public String getJob() {
        return job.get();
    }

    public void setJob(String jo_b) {
        job.set(jo_b);
    }

    public StringProperty jobProperty() {
        return job;
    }

    public Integer toInt() {
        return getEmpid();
    }
}
