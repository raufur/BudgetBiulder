package com.epsilon.coders.budgetbiulder.CustomAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.Entity.RecurringTransaction;
import com.epsilon.coders.budgetbiulder.R;
import com.epsilon.coders.budgetbiulder.Utility.DateUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raufur on 9/12/14.
 **/
public class RecurringTransactionLineAdapter extends BaseAdapter {
    private Context context;
    private List<RecurringTransaction> list;
    private LayoutInflater inflater;
    private DatabaseHandler db;
    private String pathDebut;
    private ArrayList<Integer> checkedPositions = new ArrayList<Integer>();

    public RecurringTransactionLineAdapter(Context context, List<RecurringTransaction> list, DatabaseHandler db){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.db = db;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RecurringTransaction getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void refresh(List<RecurringTransaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        RecurringTransaction t = list.get(position);

        if(view == null){
            CacheView cache = new CacheView();
            view = inflater.inflate(R.layout.recurring_transaction_list_row, null);
            cache.categoryIcon = (ImageView) view.findViewById(R.id.categoryIcon);
            cache.category = (TextView) view.findViewById(R.id.category);
            cache.comment = (TextView) view.findViewById(R.id.comment);
            cache.amount = (TextView) view.findViewById(R.id.amount);
            cache.date = (TextView) view.findViewById(R.id.date);
            cache.recurrenceDetail = (TextView) view.findViewById(R.id.recurrenceDetail);

            view.setTag(cache);
        }

        CacheView cache = (CacheView) view.getTag();
        Resources resources = context.getResources();

        cache.categoryIcon.setImageURI(Uri.parse(db.getCategory(t.getCategory()).getThumbUrl()));
        cache.category.setText(db.getCategory(t.getCategory()).getName());
        cache.comment.setText(t.getCommentary());
        double amount = t.getAmount();
        if(amount>=0){
            cache.amount.setTextColor(resources.getColor(R.color.green));
        }
        else{
            cache.amount.setTextColor(resources.getColor(R.color.red));
        }
        cache.amount.setText(amount + " €");
        cache.date.setText(resources.getString(R.string.first_payment) + " " + DateUtility.getDate(t.getDate(), "EEEE dd MMM yyyy"));

        String details = resources.getString(R.string.every) + " " + t.getDistanceBetweenPayment();
        switch (t.getTypeOfRecurrent()){
            case RecurringTransaction.MONTH: details += t.getDistanceBetweenPayment() == 1 ? " "+resources.getString(R.string.month) : " "+resources.getString(R.string.months) ; break;
            case RecurringTransaction.YEAR: details += t.getDistanceBetweenPayment() == 1 ? " "+resources.getString(R.string.year) : " "+resources.getString(R.string.years) ; break;
        }
        details += "\n" + resources.getString(R.string.paid) + " " + t.getNumberOfPaymentPaid() + " " + resources.getString(R.string.of) + " ";
        int total = t.getNumberOfPaymentTotal();
        details += total == -1 ? resources.getString(R.string.infinite_payment) : total + " " + resources.getString(R.string.payments);
        cache.recurrenceDetail.setText(details);

        if(checkedPositions.contains(position)){
            view.setBackgroundColor(resources.getColor(R.color.pale_blue));
        }
        else{
            view.setBackgroundColor(resources.getColor(R.color.app_background));
        }

        return view;
    }

    private static class CacheView{
        public ImageView categoryIcon;
        public TextView category;
        public TextView comment;
        public TextView amount;
        public TextView date;
        public TextView recurrenceDetail;
    }
}
