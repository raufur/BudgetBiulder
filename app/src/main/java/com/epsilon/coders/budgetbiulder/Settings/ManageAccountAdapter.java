package com.epsilon.coders.budgetbiulder.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsilon.coders.budgetbiulder.Entity.Account;
import com.epsilon.coders.budgetbiulder.R;

import java.util.List;

/**
 * Created by Raufur on 9/12/14.
 */
public class ManageAccountAdapter extends RecyclerView.Adapter<ManageAccountAdapter.ViewHolder> {
    private static final String TAG = "ManageAccountAdapter";
    private List<Account> list;
    private Context context;
    private Account account;

    public ManageAccountAdapter(Context context, List<Account> list){
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView bank;
        public ImageView image;
        public ImageView editButton;

        public ViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.account_name);
            bank = (TextView) v.findViewById(R.id.account_bank);
            image = (ImageView) v.findViewById(R.id.account_image);
            editButton = (ImageView) v.findViewById(R.id.account_edit);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.manage_account_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int posisiton) {
        account = list.get(posisiton);

        viewHolder.name.setText(account.getName());
        viewHolder.bank.setText(account.getBank());
        String imageUri = account.getThumbUrl();
        if(imageUri.length() != 0){
            viewHolder.image.setImageURI(Uri.parse(imageUri));
        } else{
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image));
        }

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAccount.class);
                ((Activity) context).startActivityForResult(intent, 0);

//                    account.setName(viewHolder.name.getText().toString());
//                    account.setBank(viewHolder.bank.getText().toString());
//                    DatabaseHandler.getInstance(context).editAccount(account);
            }
        });

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pick an image").
                        setItems(R.array.chosse_image, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case ManageAccountActivity.CHOOSE_IMAGE:
                                        Log.d(TAG, "Pick an image");
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);

                                        // Prevent crash if no app can handle the intent
                                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                                            ((Activity) context).startActivityForResult(intent, ManageAccountActivity.CHOOSE_IMAGE);
                                        }
                                        break;
                                    case ManageAccountActivity.TAKE_PHOTO:
                                        Log.d(TAG, "Take a photo");
                                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                        if (intent2.resolveActivity(context.getPackageManager()) != null) {
                                            ((Activity) context).startActivityForResult(intent2, ManageAccountActivity.TAKE_PHOTO);

                                        }
                                        break;
                                }
                            }

                        });
                builder.create().show();
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//
//                wmlp.gravity = Gravity.TOP | Gravity.LEFT;
//                wmlp.x = 100;   //x position
//                wmlp.y = 100;   //y position
//
//                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
