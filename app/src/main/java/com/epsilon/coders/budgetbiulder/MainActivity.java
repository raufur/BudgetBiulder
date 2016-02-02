package com.epsilon.coders.budgetbiulder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.Entity.RecurringTransaction;
import com.epsilon.coders.budgetbiulder.Entity.Transaction;
import com.epsilon.coders.budgetbiulder.NavDrawer.NavigationDrawerFragment;
import com.epsilon.coders.budgetbiulder.Settings.SettingActivity;
import com.epsilon.coders.budgetbiulder.Utility.DateUtility;

import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, CategoryFragment.OnCategorySelectedListener {
    private static final String TAG = "MainActivity";
    public static final String PREF = "Preferences";
    public static final int OVERVIEW_FRAGMENT = 1;
    public static final int TRANSACTION_FRAGMENT = 2;
    public static final int RECURRING_TRANSACTION_FRAGMENT = 3;
    public static final int CATEGORY_FRAGMENT = 4;
    public static final int SETTINGS_ACTIVITY = 6;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHandler.getInstance(this);

        //Add recurring transaction of the month
        createRecurringTransaction();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (position){
            case OVERVIEW_FRAGMENT:
                ft.replace(R.id.container, OverviewFragment.newInstance());
                break;
            case TRANSACTION_FRAGMENT:
                ft.replace(R.id.container, TransactionListFragment.newInstance());
                break;
            case RECURRING_TRANSACTION_FRAGMENT:
                ft.replace(R.id.container, RecurringTransactionListFragment.newInstance());
                break;
            case CATEGORY_FRAGMENT:
                ft.replace(R.id.container, CategoryFragment.newInstance());
                break;
            case SETTINGS_ACTIVITY:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, SETTINGS_ACTIVITY);
                break;
        }

        ft.commit();
    }

    private void createRecurringTransaction(){
        SharedPreferences settings = getSharedPreferences(PREF, 0);

        int currentMonth = DateUtility.getCurrentMonth();
        int currentMonthModulo = currentMonth % 12;
        int currentYear = DateUtility.getCurrentYear();
        Log.d(TAG, "It's the " + currentMonth + "th month of the year " + currentYear);
        Log.d(TAG, "In settings, it's the " + settings.getInt("month", currentMonth-1) + "th month of the year " + settings.getInt("year", currentYear-1));
        long ms;
        int day, month, year;

        if(currentMonth != settings.getInt("month", currentMonth-1) ||
                currentYear != settings.getInt("year", currentYear-1)){ // we add all the recurring transactions once every month
            Log.d(TAG, "A new month: adding all recurring transactions");
            List<RecurringTransaction> list = db.getAllRecurringTransactionsGlobal();
            for(RecurringTransaction t : list){
                ms = t.getDate();
                day = DateUtility.getDay(ms);

                if(t.getNumberOfPaymentTotal()-t.getNumberOfPaymentPaid() > 0 || t.getNumberOfPaymentTotal() == -1){ // if there are payments left
                    switch (t.getTypeOfRecurrent()){
                        case RecurringTransaction.MONTH:
                            month = (DateUtility.getMonth(ms)+ (t.getNumberOfPaymentPaid()+1)*t.getDistanceBetweenPayment())  % 12;
                            Log.d(TAG, "Required month is " + month);
                            year = currentYear;
                            if(currentMonthModulo == month){
                                addRecurringTransactionOfTheMonth(day, currentMonth, year, t);
                            }
                            break;
                        case RecurringTransaction.YEAR:
                            month = DateUtility.getMonth(ms);
                            year = DateUtility.getYear(ms) + (t.getNumberOfPaymentPaid()+1)*t.getDistanceBetweenPayment();
                            if(currentYear == year && currentMonthModulo == month){
                                addRecurringTransactionOfTheMonth(day, currentMonth, year, t);
                            }
                            break;
                    }
                }
                else{
                    db.deleteRecurringTransaction(t);
                }
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("month", currentMonth);
            editor.putInt("year", currentYear);
            editor.apply();
        }
    }

    private void addRecurringTransactionOfTheMonth(int day, int month, int year, RecurringTransaction t){
        Log.d(TAG, "day "+day+" month "+month+ " year "+year);
        Transaction ts = new Transaction(t.getAmount(), t.getCategory(), false, DateUtility.dayToMillisecond(day,month,year), t.getCommentary(), t.getAccount());
        db.addTransaction(ts);
        Log.d(TAG, "Added :   Id:" + ts.getId() + "   Amount=" + ts.getAmount() + "   Done:" + ts.isDone() + "   Date:" + DateUtility.getDate(ts.getDate(), "EEEE dd MMM yyyy") + "  Commentary:" + ts.getCommentary()
                + "    Category:" + db.getCategory(ts.getCategory()).getName() + "   Account:"+db.getAccount(ts.getAccount()).getName());

        // Number of payment paid ++
        t.setNumberOfPaymentPaid(t.getNumberOfPaymentPaid()+1);
        db.updateRecurringTransaction(t);
    }


    @Override
    public void onCategorySelected(int categoryId) {
        CategoryViewFragment fragment = CategoryViewFragment.newInstance();
        fragment.updateContent(categoryId);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
