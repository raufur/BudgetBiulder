package com.epsilon.coders.budgetbiulder.Entity;
/**
 * Created by Raufur on 9/12/14.
 */
public class RecurringTransaction extends Transaction {
    public final static int MONTH = 1;
    public final static int YEAR = 2;

    private int numberOfPaymentPaid;
    private int numberOfPaymentTotal; // -1 if recurring transaction has no end date
    private int distanceBetweenPayment; //ex: 1 (year), 28 (days)
    private int typeOfRecurrent; // day,week,month or year

    public RecurringTransaction(){}

    public RecurringTransaction(int id, double amount, int category, long date, String commentary, int account, int numberOfPaymentPaid, int numberOfPaymentTotal, int distanceBetweenPayment, int typeOfRecurrent){
        super(id,amount,category,true,date,commentary, account);
        this.numberOfPaymentPaid = numberOfPaymentPaid;
        this.numberOfPaymentTotal = numberOfPaymentTotal;
        this.distanceBetweenPayment = distanceBetweenPayment;
        this.typeOfRecurrent = typeOfRecurrent;
    }

    public RecurringTransaction(double amount, int category, long date, String commentary, int account, int numberOfPaymentPaid, int numberOfPaymentTotal, int distanceBetweenPayment, int typeOfRecurrent){
        super(amount,category,true,date,commentary, account);
        this.numberOfPaymentPaid = numberOfPaymentPaid;
        this.numberOfPaymentTotal = numberOfPaymentTotal;
        this.distanceBetweenPayment = distanceBetweenPayment;
        this.typeOfRecurrent = typeOfRecurrent;
    }

    public int getNumberOfPaymentPaid() {
        return numberOfPaymentPaid;
    }

    public void setNumberOfPaymentPaid(int numberOfPaymentPaid) {
        this.numberOfPaymentPaid = numberOfPaymentPaid;
    }

    public int getDistanceBetweenPayment() {
        return distanceBetweenPayment;
    }

    public void setDistanceBetweenPayment(int distanceBetweenPayment) {
        this.distanceBetweenPayment = distanceBetweenPayment;
    }

    public int getTypeOfRecurrent() {
        return typeOfRecurrent;
    }

    public void setTypeOfRecurrent(int typeOfRecurrent) {
        this.typeOfRecurrent = typeOfRecurrent;
    }

    public int getNumberOfPaymentTotal() {
        return numberOfPaymentTotal;
    }

    public void setNumberOfPaymentTotal(int numberOfPaymentTotal) {
        this.numberOfPaymentTotal = numberOfPaymentTotal;
    }
}
