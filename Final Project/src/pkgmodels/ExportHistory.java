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
public class ExportHistory {

    private final SimpleStringProperty manager;
    private final SimpleIntegerProperty year;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty amount;
    private final SimpleStringProperty country;

    public ExportHistory(Integer year, Double amount, Double price, String country, String manager) {
        this.year = new SimpleIntegerProperty(year);
        this.amount = new SimpleDoubleProperty(amount);
        this.price = new SimpleDoubleProperty(price);
        this.manager = new SimpleStringProperty(manager);
        this.country = new SimpleStringProperty(country);
    }

    public String getManager() {
        return manager.get();
    }

    public void setManager(String mgr) {
        manager.set(mgr);
    }

    public StringProperty managerProperty() {
        return manager;
    }

    public Integer getYear() {
        return year.get();
    }

    public void setYear(Integer yr) {
        year.set(yr);
    }

    public IntegerProperty yearProperty() {
        return year;
    }
     public Double getPrice() {
        return price.get();
    }

    public void setPrice(Double prc) {
        price.set(prc);
    }

    public DoubleProperty priceProperty() {
        return price;
    }
     public Double getAmount() {
        return amount.get();
    }

    public void setAmount(Double amt) {
        amount.set(amt);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }
     public String getCountry() {
        return country.get();
    }

    public void setCountry(String ctry) {
        country.set(ctry);
    }

    public StringProperty countryProperty() {
        return country;
    }
}
