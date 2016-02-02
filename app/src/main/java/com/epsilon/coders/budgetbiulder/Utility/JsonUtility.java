package com.epsilon.coders.budgetbiulder.Utility;

import android.content.Context;
import android.util.JsonWriter;


import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.Entity.RecurringTransaction;
import com.epsilon.coders.budgetbiulder.Entity.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Raufur on 10/6/15.
 */
public class JsonUtility {

    public static void writeJsonStream(Context context, OutputStream out) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");

        writer.beginObject();

        // Write all transactions
        writer.name("transactions");
        writeAllTransactions(writer, context);

        // Write all recurring transactions
        writer.name("recurringTransactions");
        writeAllRecurringTransactions(writer, context);

        writer.endObject();
        writer.close();
    }

    public static void writeAllTransactions(JsonWriter writer, Context context) throws IOException {
        writer.beginArray();
        DatabaseHandler db = DatabaseHandler.getInstance(context);
        List<Transaction> list = db.getAllTransactionsGlobal();
        for(Transaction t : list){
            writeTransaction(writer, t);
        }
        writer.endArray();
    }

    public static void writeTransaction(JsonWriter writer, Transaction t) throws IOException {
        writer.beginObject();
        writer.name("amount").value(t.getAmount());
        writer.name("category").value(t.getCategory());
        writer.name("isDone").value(t.isDone());
        writer.name("date").value(t.getDate());
        writer.name("commentary").value(t.getCommentary());
        writer.name("account").value(t.getAccount());
        writer.endObject();
    }

    public static void writeAllRecurringTransactions(JsonWriter writer, Context context) throws IOException {
        writer.beginArray();
        DatabaseHandler db = DatabaseHandler.getInstance(context);
        List<RecurringTransaction> list = db.getAllRecurringTransactionsGlobal();
        for(RecurringTransaction r : list){
            writeRecurringTransaction(writer, r);
        }
        writer.endArray();
    }

    public static void writeRecurringTransaction(JsonWriter writer, RecurringTransaction r) throws IOException {
        writer.beginObject();
        writer.name("amount").value(r.getAmount());
        writer.name("category").value(r.getCategory());
        writer.name("date").value(r.getDate());
        writer.name("commentary").value(r.getCommentary());
        writer.name("account").value(r.getAccount());
        writer.name("numberOfPaymentPaid").value(r.getNumberOfPaymentPaid());
        writer.name("numberOfPaymentTotal").value(r.getNumberOfPaymentTotal());
        writer.name("distanceBetweenPayment").value(r.getDistanceBetweenPayment());
        writer.name("typeOfRecurrent").value(r.getTypeOfRecurrent());
        writer.endObject();
    }

    public static JSONObject readJsonStream(InputStream input){
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

            //returns the json object
            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void readJsonToDatabase(Context context, InputStream input) throws JSONException {
        DatabaseHandler db = DatabaseHandler.getInstance(context);

        JSONObject root = readJsonStream(input);
        JSONObject t;
        Transaction newTransaction;
        RecurringTransaction newRecurringTransaction;

        db.deleteAllTransactions();
        JSONArray transactions = root.getJSONArray("transactions");
        for(int i=0; i<transactions.length(); i++){
            t = transactions.getJSONObject(i);
            newTransaction = new Transaction(t.getDouble("amount"),
                    t.getInt("category"),
                    t.getBoolean("isDone"),
                    t.getLong("date"),
                    t.getString("commentary"),
                    t.getInt("account"));
            db.addTransaction(newTransaction);
        }

        db.deleteAllRecurringTransactions();
        JSONArray recurringTransactions = root.getJSONArray("recurringTransactions");
        for(int i=0; i<recurringTransactions.length(); i++){
            t = recurringTransactions.getJSONObject(i);
            newRecurringTransaction = new RecurringTransaction(t.getDouble("amount"),
                    t.getInt("category"),
                    t.getLong("date"),
                    t.getString("commentary"),
                    t.getInt("account"),
                    t.getInt("numberOfPaymentPaid"),
                    t.getInt("numberOfPaymentTotal"),
                    t.getInt("distanceBetweenPayment"),
                    t.getInt("typeOfRecurrent"));
            db.addRecurringTransaction(newRecurringTransaction);
        }
    }
}
