package pkgmodels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class farmer {

    private final IntegerProperty farmerid;
    private final StringProperty surname;
    private final StringProperty midname;
    private final StringProperty lastname;
    private final IntegerProperty mobileno;
    private final IntegerProperty nationalidno;
    private final IntegerProperty accountno;
    private final StringProperty sacco;

    public farmer(Integer farmerid, String surname, String midname, String lastname, Integer mobileno, Integer nationalidno, Integer accountno, String sacco) {
        this.farmerid = new SimpleIntegerProperty(farmerid);
        this.surname = new SimpleStringProperty(surname);
        this.midname = new SimpleStringProperty(midname);
        this.lastname = new SimpleStringProperty(lastname);
        this.mobileno = new SimpleIntegerProperty(mobileno);
        this.nationalidno = new SimpleIntegerProperty(nationalidno);
        this.accountno = new SimpleIntegerProperty(accountno);
        this.sacco = new SimpleStringProperty(sacco);
    }

    public Integer getFarmerid() {
        return farmerid.get();
    }

    public void setFarmerid(int frmid) {
        farmerid.set(frmid);
    }

    public IntegerProperty farmeridProperty() {
        return farmerid;
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String snm) {
        surname.set(snm);
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public String getMidname() {
        return midname.get();
    }

    public void setMidname(String mnm) {
        midname.set(mnm);
    }

    public StringProperty midnameProperty() {
        return midname;
    }

    public String getLastname() {
        return lastname.get();
    }

    public void setLastname(String lnm) {
        lastname.set(lnm);
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public Integer getMobileno() {
        return mobileno.get();
    }

    public void setMobileno(int mno) {
        mobileno.set(mno);
    }

    public IntegerProperty mobilenoProperty() {
        return mobileno;
    }

    public Integer getNationalidno() {
        return nationalidno.get();
    }

    public void setNationalidno(int nid) {
        nationalidno.set(nid);
    }

    public IntegerProperty nationalidnoProperty() {
        return nationalidno;
    }

    public Integer getAccountno() {
        return accountno.get();
    }

    public void setAccountno(int acno) {
        accountno.set(acno);
    }

    public IntegerProperty accountnoProperty() {
        return accountno;
    }

    public String getSacco() {
        return sacco.get();
    }

    public void setSacco(String scc) {
        sacco.set(scc);
    }

    public StringProperty saccoProperty() {
        return sacco;
    }

}
