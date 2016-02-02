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
import com.epsilon.coders.budgetbiulder.Entity.Category;
import com.epsilon.coders.budgetbiulder.R;
import com.epsilon.coders.budgetbiulder.Utility.DateUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Raufur on 9/12/14.
 */
public class CategoryLineAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Category> list;
    private DatabaseHandler db;
    private ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
    private ArrayList<Double> listAmount;
    private ArrayList<Long> listDate;
    private ArrayList<Integer> colors;

    public CategoryLineAdapter(Context context, List<Category> list, DatabaseHandler db, ArrayList<Double> listAmount, ArrayList<Long> listDate){
        this.context = context;
        this.list = list;
        this.db = db;
        this.listAmount = listAmount;
        this.listDate = listDate;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        colors = new ArrayList<Integer>(Arrays.asList(R.color.pink, R.color.green, R.color.orange, R.color.purple, R.color.red, R.color.blue));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Category getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void refresh(List<Category> list, ArrayList<Double> listAmount, ArrayList<Long> listDate) {
        this.list = list;
        this.listAmount = listAmount;
        this.listDate = listDate;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Category c = list.get(position);

        if(view == null){
            ViewHolder cache = new ViewHolder();
            view = inflater.inflate(R.layout.category_list_row, null);
            cache.background = (ImageView) view.findViewById(R.id.background);
            cache.icon = (ImageView) view.findViewById(R.id.icon);
            cache.name = (TextView) view.findViewById(R.id.categoryName);
            cache.amount = (TextView) view.findViewById(R.id.categoryAmount);
            cache.lastTransaction = (TextView) view.findViewById(R.id.lastTransaction);

            view.setTag(cache);
        }

        ViewHolder cache = (ViewHolder) view.getTag();
        int color = context.getResources().getColor(colors.get(position % colors.size()));

        cache.background.setBackground(context.getResources().getDrawable(R.drawable.oval));
        ((GradientDrawable)cache.background.getBackground()).setColor(color);

        cache.icon.setImageURI(Uri.parse(c.getThumbUrl()));

        cache.name.setText(c.getName());

        double amount = listAmount.get(position);
        if(amount != 0) {
            cache.amount.setText(amount + " " + context.getResources().getString(R.string.euro));
        }
        else {
            cache.amount.setText("");
        }

        long date = listDate.get(position);
        if(date != 0) {
            cache.lastTransaction.setText(context.getResources().getString(R.string.last_transaction) + " " + DateUtility.getDate(date, "EEEE dd MMM yyyy"));
        }
        else {
            cache.lastTransaction.setText("");
        }

        return view;
    }

    private static class ViewHolder{
        public ImageView background;
        public ImageView icon;
        public TextView name;
        public TextView amount;
        public TextView lastTransaction;
    }
}
