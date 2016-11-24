package pkgmodels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class inputs {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty type;
    private final IntegerProperty remaining;

    public inputs(Integer id, String name, String type, Integer remaining) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.remaining = new SimpleIntegerProperty(remaining);
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer i) {
        id.set(i);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String s) {
        name.set(s);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String s) {
        type.set(s);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public Integer getRemaining() {
        return remaining.get();
    }

    public void setRemaining(Integer i) {
        remaining.set(i);
    }

    public IntegerProperty remainingProperty() {
        return remaining;
    }

}
