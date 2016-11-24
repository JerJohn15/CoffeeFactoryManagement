package pkgmodels;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class chemicals {

    private final IntegerProperty inputid;
    private final StringProperty inputname;
    private final StringProperty inputtype;
    private final DoubleProperty initamount;
    private final DoubleProperty remamount;
    private final DoubleProperty price;
    private final StringProperty date;
    private final StringProperty availability;

    public chemicals(Integer inputid, String inputname, String inputtype, Double initamount, Double remamount, Double price, String date, String availability) {
        this.inputid = new SimpleIntegerProperty(inputid);
        this.inputname = new SimpleStringProperty(inputname);
        this.inputtype = new SimpleStringProperty(inputtype);
        this.initamount = new SimpleDoubleProperty(initamount);
        this.remamount = new SimpleDoubleProperty(remamount);
        this.price = new SimpleDoubleProperty(price);
        this.date = new SimpleStringProperty(date);
        this.availability = new SimpleStringProperty(availability);
    }

    public int getInputid() {
        return inputid.get();
    }

    public void setInputid(int value) {
        inputid.set(value);
    }

    public IntegerProperty inputidProperty() {
        return inputid;
    }

    public String getInputname() {
        return inputname.get();
    }

    public void setInputname(String Inputname) {
        inputname.set(Inputname);
    }

    public StringProperty inputnameProperty() {
        return inputname;
    }

    public String getInputtype() {
        return inputtype.get();
    }

    public void setInputtype(String value) {
        inputtype.set(value);
    }

    public StringProperty inputtypeProperty() {
        return inputtype;
    }

    public double getInitamount() {
        return initamount.get();
    }

    public void setInitamount(double value) {
        initamount.set(value);
    }

    public DoubleProperty initamountProperty() {
        return initamount;
    }

    public double getRemamount() {
        return remamount.get();
    }

    public void setRemamount(double value) {
        remamount.set(value);
    }

    public DoubleProperty remamountProperty() {
        return remamount;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        price.set(value);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String value) {
        date.set(value);
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getAvailability() {
        return availability.get();
    }

    public void setAvailability(String value) {
        availability.set(value);
    }

    public StringProperty availabilityProperty() {
        return availability;
    }

}
