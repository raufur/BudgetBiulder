package com.epsilon.coders.budgetbiulder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.Entity.RecurringTransaction;
import com.epsilon.coders.budgetbiulder.Entity.Transaction;
import com.epsilon.coders.budgetbiulder.Utility.DateUtility;
import com.epsilon.coders.budgetbiulder.Utility.PreferencesUtility;


public class TransactionActionActivity extends Activity {
    public static final int ADD_NEW_TRANSACTIION = 1;
    public static final int VIEW_TRANSACTION = 2;
    public static final int ADD_NEW_RECURRING_TRANSACTION = 3;
    public static final int EDIT_RECURRING_TRANSACTION = 4;

    private ImageView clearedIcon, categoryIcon, ok;
    private Spinner spinner, spinnerTypeRecurrence;
    private TextView title;
    private EditText editAmount, editComment, editDistanceRecurrence, editNumberOfRecurrence;
    private CheckBox checkbox;
    private LinearLayout recurrenceBlock, numberBlock;


    private DatabaseHandler db;

    private String commentText, amountText, distanceText;
    private boolean isCleared;
    private int category, transactionId, typeOfDialog, account, numberPaymentPaid;
    public static long transactionDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transaction_action);

        Intent intent = getIntent();
        typeOfDialog = intent.getIntExtra("typeOfDialog", 2);
        transactionId = (int) intent.getLongExtra("transactionId", 1);
        transactionDate = System.currentTimeMillis();

        db = DatabaseHandler.getInstance(this);

        clearedIcon = (ImageView) findViewById(R.id.clearedAddTransaction);
        categoryIcon = (ImageView) findViewById(R.id.categoryIconAddTransaction);
        spinner = (Spinner) findViewById(R.id.spinner);
        editComment = (EditText) findViewById(R.id.editComment);
        editAmount = (EditText) findViewById(R.id.editAmount);
        title = (TextView) findViewById(R.id.title);
        ok = (ImageView) findViewById(R.id.ok);
        checkbox = (CheckBox) findViewById(R.id.repeat);
        editDistanceRecurrence = (EditText) findViewById(R.id.distanceOfRecurrence);
        spinnerTypeRecurrence = (Spinner) findViewById(R.id.typeOfRecurrence);
        recurrenceBlock = (LinearLayout) findViewById(R.id.blockRecurring);
        numberBlock = (LinearLayout) findViewById(R.id.blockNumber);
        editNumberOfRecurrence = (EditText) findViewById(R.id.numberOfRecurrence);

        //Category
        SpinnerAdapter mSpinnerAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                db.getAllCategoriesCursor(true),
                new String[] { db.KEY_NAME },
                new int[] { android.R.id.text1 },
                0);
        spinner.setAdapter(mSpinnerAdapter);

        // Type of recurrence
        ArrayAdapter<CharSequence> recurrenceAdapter = ArrayAdapter.createFromResource(this, R.array.option_array, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeRecurrence.setAdapter(recurrenceAdapter);

        initLook();
    }

    private void initLook(){
        //Switch between normal and recurring transaction
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && typeOfDialog==ADD_NEW_TRANSACTIION){
                    typeOfDialog = ADD_NEW_RECURRING_TRANSACTION;
                    initLook();
                }
                else if(!isChecked && typeOfDialog==ADD_NEW_RECURRING_TRANSACTION){
                    typeOfDialog = ADD_NEW_TRANSACTIION;
                    initLook();
                }
            }
        });

        //Choose the category
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (int) id;
                categoryIcon.setImageURI(Uri.parse(db.getCategory((int)id).getThumbUrl()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = 1;
            }
        });

        switch (typeOfDialog){
            case ADD_NEW_TRANSACTIION:
                //Choose title
                title.setText(getResources().getString(R.string.title_activity_add_transaction));

                //Choose the transaction status (cleared/not cleared)
                isCleared = false;
                switchClearedStatus();
                clearedIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isCleared = (!isCleared);
                        switchClearedStatus();
                    }
                });

                recurrenceBlock.setVisibility(View.GONE);
                numberBlock.setVisibility(View.GONE);

                ok();

                break;

            case VIEW_TRANSACTION:
                //Disable buttons
                ok.setVisibility(View.GONE);
                checkbox.setVisibility(View.GONE);

                Transaction t =  db.getTransaction(transactionId);

                //Show the transaction status (cleared/not cleared)
                isCleared = t.isDone();
                switchClearedStatus();
                clearedIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchToEditTransaction();
                        isCleared = (!isCleared);
                        switchClearedStatus();
                    }
                });

                //Show category
                category = t.getCategory();
                categoryIcon.setImageURI(Uri.parse(db.getCategory(category).getThumbUrl()));

                //Spinner
                spinner.setSelection(category - 1);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position!=category-1)
                            switchToEditTransaction();
                        category = (int) id;
                        categoryIcon.setImageURI(Uri.parse(db.getCategory((int)id).getThumbUrl()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        category = 1;
                    }
                });

                //Amount
                editAmount.setText(t.getAmount() + "");
                editAmount.setFocusable(false);
                editAmount.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switchToEditTransaction();
                        return false;
                    }
                });

                //Comment
                editComment.setText(t.getCommentary());
                editComment.setFocusable(false);
                editComment.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switchToEditTransaction();
                        return false;
                    }
                });

                //Date
                transactionDate = t.getDate();

                //Title
                title.setText(DateUtility.getDate(transactionDate, "EEEE dd MMM yyyy"));

                ok();

                break;

            case ADD_NEW_RECURRING_TRANSACTION:
                title.setText(getResources().getString(R.string.title_activity_add_transaction));
                recurrenceBlock.setVisibility(View.VISIBLE);
                numberBlock.setVisibility(View.VISIBLE);
                clearedIcon.setBackground(getResources().getDrawable(R.drawable.oval));
                ((GradientDrawable)clearedIcon.getBackground()).setColor(getResources().getColor(R.color.red));
                clearedIcon.setEnabled(false);
                checkbox.setChecked(true);

                ok();
                break;

            case EDIT_RECURRING_TRANSACTION:
                title.setText(getResources().getString(R.string.edit_transaction));
                recurrenceBlock.setVisibility(View.VISIBLE);
                numberBlock.setVisibility(View.VISIBLE);
                clearedIcon.setBackground(getResources().getDrawable(R.drawable.oval));
                ((GradientDrawable)clearedIcon.getBackground()).setColor(getResources().getColor(R.color.red));
                clearedIcon.setEnabled(false);
                checkbox.setChecked(true);
                checkbox.setEnabled(false);

                RecurringTransaction r = db.getRecurringTransaction(transactionId);

                category = r.getCategory();
                categoryIcon.setImageURI(Uri.parse(db.getCategory(category).getThumbUrl()));
                spinner.setSelection(category - 1);
                editAmount.setText(r.getAmount() + "");
                editComment.setText(r.getCommentary());
                editNumberOfRecurrence.setText(r.getNumberOfPaymentTotal() + "");
                transactionDate = r.getDate();
                title.setText(DateUtility.getDate(transactionDate, "EEEE dd MMM yyyy"));
                numberPaymentPaid = r.getNumberOfPaymentPaid();

                editDistanceRecurrence.setText(r.getDistanceBetweenPayment() + "");
                int type = r.getTypeOfRecurrent();
                switch(type){
                    case RecurringTransaction.MONTH:
                        spinnerTypeRecurrence.setSelection(0);
                        break;
                    case RecurringTransaction.YEAR:
                        spinnerTypeRecurrence.setSelection(1);
                        break;
                }

                ok();
                break;
        }
    }

    private void switchClearedStatus(){
        clearedIcon.setBackground(getResources().getDrawable(R.drawable.oval));
        if(isCleared){
            ((GradientDrawable)clearedIcon.getBackground()).setColor(getResources().getColor(R.color.blue));
        }
        else{
            ((GradientDrawable)clearedIcon.getBackground()).setColor(getResources().getColor(R.color.red));
        }
    }

    private void switchToEditTransaction(){
        ok.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.edit_transaction));
        editAmount.setFocusable(true);
        editAmount.setFocusableInTouchMode(true);
        editComment.setFocusable(true);
        editComment.setFocusableInTouchMode(true);
    }

    private void ok(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Amount
            amountText = editAmount.getText().toString().trim();

            //Comment
            commentText = editComment.getText().toString().trim();

            //Recurring distance
            distanceText = editDistanceRecurrence.getText().toString().trim();

            //Account
            account = PreferencesUtility.getAccount(TransactionActionActivity.this);

            if (amountText.equals("")) {
                Toast.makeText(TransactionActionActivity.this, getResources().getString(R.string.error_edit_amount), Toast.LENGTH_SHORT).show();
            }
            else if(distanceText.equals("") && typeOfDialog==ADD_NEW_RECURRING_TRANSACTION){
                Toast.makeText(TransactionActionActivity.this, getResources().getString(R.string.error_edit_distance), Toast.LENGTH_SHORT).show();
            }
            else {
                switch (typeOfDialog) {
                    case ADD_NEW_TRANSACTIION:
                        Transaction t = new Transaction(Double.parseDouble(amountText), category, isCleared, transactionDate, commentText, account);
                        db.addTransaction(t);
                        break;
                    case VIEW_TRANSACTION:
                        Transaction t2 = new Transaction(Double.parseDouble(amountText), category, isCleared, transactionDate, commentText, account);
                        t2.setId(transactionId);
                        db.updateTransaction(t2);
                        break;
                    case ADD_NEW_RECURRING_TRANSACTION:
                        int distanceRecurrence = Integer.parseInt(editDistanceRecurrence.getText().toString());
                        String txt = editNumberOfRecurrence.getText().toString();
                        int numberOfRecurrence = txt.equals("") ? -1 : Integer.parseInt(txt);

                        spinnerTypeRecurrence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                view.setTag(position+1);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        RecurringTransaction r = new RecurringTransaction(Double.parseDouble(amountText), category, transactionDate, commentText, account, 1, numberOfRecurrence, distanceRecurrence, spinnerTypeRecurrence.getSelectedItemPosition()+1);
                        Transaction tr = new Transaction(Double.parseDouble(amountText), category, false, transactionDate, commentText, account);
                        db.addRecurringTransaction(r);
                        db.addTransaction(tr);
                        break;
                    case EDIT_RECURRING_TRANSACTION:
                        distanceRecurrence = Integer.parseInt(editDistanceRecurrence.getText().toString());
                        txt = editNumberOfRecurrence.getText().toString();
                        numberOfRecurrence = txt.equals("") ? -1 : Integer.parseInt(txt);
                        RecurringTransaction rEdited = new RecurringTransaction(Double.parseDouble(amountText), category, transactionDate, commentText, account, numberPaymentPaid, numberOfRecurrence, distanceRecurrence, spinnerTypeRecurrence.getSelectedItemPosition()+1);
                        rEdited.setId(transactionId);
                        db.updateRecurringTransaction(rEdited);
                        break;
                }
                TransactionActionActivity.this.setResult(RESULT_OK);
                finish();
            }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        switchToEditTransaction();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
}
