package com.epsilon.coders.budgetbiulder.CustomAdapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.Entity.Transaction;
import com.epsilon.coders.budgetbiulder.R;
import com.epsilon.coders.budgetbiulder.Utility.DateUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raufur on 9/12/14.
 */
public class TransactionLineAdapter extends BaseAdapter {
    private Context context;
    private List<Transaction> list;
    private LayoutInflater inflater;
    private DatabaseHandler db;
    private ArrayList<Integer> checkedPositions = new ArrayList<Integer>();

    public TransactionLineAdapter(Context context, List<Transaction> list, DatabaseHandler db){
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
    public Transaction getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void refresh(List<Transaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Transaction t = list.get(position);

        if(view == null){
            CacheView cache = new CacheView();
            view = inflater.inflate(R.layout.transaction_list_row, null);
            cache.cleared = (ImageView) view.findViewById(R.id.cleared);
            cache.categoryIcon = (ImageView) view.findViewById(R.id.categoryIcon);
            cache.category = (TextView) view.findViewById(R.id.category);
            cache.comment = (TextView) view.findViewById(R.id.comment);
            cache.amount = (TextView) view.findViewById(R.id.amount);
            cache.headerSection = (TextView) view.findViewById(R.id.headerSection);

            view.setTag(cache);
        }

        CacheView cache = (CacheView) view.getTag();
        updateClearedIcon(cache.cleared,t);
        cache.cleared.setTag(position);
        cache.cleared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction t = getItem((Integer) v.getTag());
                t.setDone(!t.isDone());
                db.updateTransaction(t);
                updateClearedIcon((ImageView)v,t);
            }
        });

        cache.categoryIcon.setImageURI(Uri.parse(db.getCategory(t.getCategory()).getThumbUrl()));
        cache.category.setText(db.getCategory(t.getCategory()).getName());
        cache.comment.setText(t.getCommentary());
        double amount = t.getAmount();
        if(amount>=0){
            cache.amount.setTextColor(context.getResources().getColor(R.color.green));
        }
        else{
            cache.amount.setTextColor(context.getResources().getColor(R.color.red));
        }
        cache.amount.setText(amount + " €");

        if(checkedPositions.contains(position)){
            view.setBackgroundColor(context.getResources().getColor(R.color.pale_blue));
        }
        else{
            view.setBackgroundColor(context.getResources().getColor(R.color.app_background));
        }

        //Section header
        String date = DateUtility.getDate(t.getDate(), "EEEE dd MMM yyyy");
        String previousDate = position>0? DateUtility.getDate(list.get(position-1).getDate(), "EEEE dd MMM yyyy") : "";
        if(date.equals(previousDate)){
            cache.headerSection.setVisibility(View.GONE);
        }
        else{
            cache.headerSection.setVisibility(View.VISIBLE);
            cache.headerSection.setText(date);
        }

        return view;
    }

    private void updateClearedIcon(ImageView v, Transaction t){
        v.setBackground(context.getResources().getDrawable(R.drawable.oval));
        if(t.isDone()){
            ((GradientDrawable)v.getBackground()).setColor(context.getResources().getColor(R.color.blue));
        }
        else{
            ((GradientDrawable)v.getBackground()).setColor(context.getResources().getColor(R.color.red));
        }
    }

    private static class CacheView{
        public ImageView cleared;
        public ImageView categoryIcon;
        public TextView category;
        public TextView comment;
        public TextView amount;
        public TextView headerSection;
    }
}
