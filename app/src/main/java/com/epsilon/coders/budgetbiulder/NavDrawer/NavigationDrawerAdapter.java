package com.epsilon.coders.budgetbiulder.NavDrawer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsilon.coders.budgetbiulder.R;

import java.util.List;

/**
 * Created by Raufur on 9/12/14.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {
    private List<NavDrawerItem> list;
    private Context context;
    private int itemChecked;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> list){
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView bank;
        public ImageView icon;
        public int type;

        public ViewHolder(View v, int type) {
            super(v);

            this.type = type;

            switch(type){
                case NavDrawerItem.HEADER:
                    name = (TextView) v.findViewById(R.id.account_name);
                    bank = (TextView) v.findViewById(R.id.account_bank);
                    icon = (ImageView) v.findViewById(R.id.circleView);
                    break;
                case NavDrawerItem.ITEM:
                    name = (TextView) v.findViewById(R.id.navItemName);
                    icon = (ImageView) v.findViewById(R.id.navItemIcon);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;

        switch(viewType){
            case NavDrawerItem.HEADER:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_drawer_header, viewGroup, false);
                break;
            case NavDrawerItem.ITEM:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_drawer_row, viewGroup, false);
                break;
            case NavDrawerItem.DIVIDER:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.divider, viewGroup, false);
                break;
        }
        return new ViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        switch(viewHolder.type){
            case NavDrawerItem.HEADER:
                viewHolder.name.setText(list.get(position).getName());
                viewHolder.bank.setText(list.get(position).getBank());
                String imageUri = list.get(position).getImage();
                if(imageUri.length() != 0){
                    viewHolder.icon.setImageURI(Uri.parse(list.get(position).getImage()));
                } else{
                    viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image));
                }
                break;
            case NavDrawerItem.ITEM:
                viewHolder.name.setText(list.get(position).getName());
                viewHolder.icon.setImageDrawable(list.get(position).getIcon());
                if(position == itemChecked){
                    viewHolder.name.setTextColor(context.getResources().getColor(R.color.pink));
                } else{
                    viewHolder.name.setTextColor(context.getResources().getColor(R.color.app_text));
                }
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    public void setItemChecked(int position){
        itemChecked = position;
        notifyDataSetChanged();
    }
}
