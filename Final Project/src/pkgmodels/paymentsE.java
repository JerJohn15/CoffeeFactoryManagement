/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgmodels;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author malware
 */
public class paymentsE {

    private final IntegerProperty empid;
    private final DoubleProperty totalwage;
    private final DoubleProperty penalty;
    private final IntegerProperty days;
    private final DoubleProperty netwage;

    public paymentsE(Integer empid, Double totalwage, Double penalty, Integer days, Double netwage) {
        this.empid = new SimpleIntegerProperty(empid);
        this.totalwage = new SimpleDoubleProperty(totalwage);
        this.penalty = new SimpleDoubleProperty(penalty);
        this.days = new SimpleIntegerProperty(days);
        this.netwage = new SimpleDoubleProperty(netwage);
    }

    public Integer getEmpid() {
        return empid.get();
    }

    public void setEmpid(int i) {
        empid.set(i);
    }

    public IntegerProperty empidProperty() {
        return empid;
    }

    public Integer getDays() {
        return days.get();
    }

    public void setDays(int i) {
        days.set(i);
    }

    public IntegerProperty daysProperty() {
        return days;
    }

    public Double getTotalwage() {
        return totalwage.get();
    }

    public void setTotalwage(double i) {
        totalwage.set(i);
    }

    public DoubleProperty totalwageProperty() {
        return totalwage;
    }

    public Double getPenalty() {
        return penalty.get();
    }

    public void setPenalty(double i) {
        penalty.set(i);
    }

    public DoubleProperty penaltyProperty() {
        return penalty;
    }

    public Double getNetwage() {
        return netwage.get();
    }

    public void setNetwage(double i) {
        netwage.set(i);
    }

    public DoubleProperty netwageProperty() {
        return netwage;
    }

}
