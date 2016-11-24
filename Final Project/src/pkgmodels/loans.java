package pkgmodels;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class loans {

    private final IntegerProperty inputid;
    private final IntegerProperty farmerid;
    private final DoubleProperty quantity;

    public loans(Integer inputid, Integer farmerid, double quantity) {
        this.inputid = new SimpleIntegerProperty(inputid);
        this.farmerid = new SimpleIntegerProperty(farmerid);
        this.quantity = new SimpleDoubleProperty(quantity);
    }

    public Integer getInputid() {
        return inputid.get();
    }

    public void setInputid(int i) {
        inputid.set(i);
    }

    public IntegerProperty inputidProperty() {
        return inputid;
    }

    public Integer getFarmerid() {
        return farmerid.get();
    }

    public void setFarmerid(int i) {
        farmerid.set(i);
    }

    public IntegerProperty farmeridProperty() {
        return farmerid;
    }

    public Double getQuantity() {
        return quantity.get();
    }

    public void setQuantity(double i) {
        quantity.set(i);
    }

    public DoubleProperty quantityProperty() {
        return quantity;
    }

}
