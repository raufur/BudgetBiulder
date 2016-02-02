package com.epsilon.coders.budgetbiulder;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;


import com.epsilon.coders.budgetbiulder.CustomAdapter.TransactionLineAdapter;
import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.Entity.Transaction;
import com.epsilon.coders.budgetbiulder.Utility.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewFragment extends Fragment {
    private TransactionLineAdapter adapter;
    private DatabaseHandler db;
    private int categoryId, account;

    public static CategoryViewFragment newInstance() {
        return new CategoryViewFragment();
    }
    public CategoryViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        db = DatabaseHandler.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_view, container, false);

        //Retrieve account number
        account = PreferencesUtility.getAccount(getActivity());

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(db.getCategory(categoryId).getName());

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        List<Transaction> list = db.getTransactionsOfCategory(categoryId, account);

        adapter = new TransactionLineAdapter(getActivity(), list, db);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
                intent.putExtra("typeOfDialog", TransactionActionActivity.VIEW_TRANSACTION);
                intent.putExtra("transactionId", adapter.getItemId(position));
                startActivityForResult(intent, TransactionActionActivity.VIEW_TRANSACTION);
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int number = 0;
            private ArrayList<Transaction> listToDelete = new ArrayList<Transaction>();
            private ArrayList<Integer> checkedPosition = adapter.getCheckedPositions();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                if(checked){
                    number++;
                    listToDelete.add(db.getTransaction((int) id));
                    checkedPosition.add(position);
                }
                else{
                    number--;
                    listToDelete.remove(db.getTransaction((int) id));
                    checkedPosition.remove(checkedPosition.indexOf(position));
                }
                mode.setTitle(number + " selected");
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.deleteTransaction:
                        for(int i=0; i<listToDelete.size(); i++){
                            db.deleteTransaction(listToDelete.get(i));
                        }
                        adapter.refresh(db.getAllTransactions(account));
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.transaction_list_context_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
                checkedPosition.clear();
                adapter.notifyDataSetChanged();
                number=0;
                listToDelete.clear();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

        return view;
    }

    public void updateContent(int categoryId){
        this.categoryId = categoryId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            switch (requestCode){
                case TransactionActionActivity.VIEW_TRANSACTION:
                    adapter.refresh(db.getTransactionsOfCategory(categoryId, account));
                    break;
            }

        }
    }
}
