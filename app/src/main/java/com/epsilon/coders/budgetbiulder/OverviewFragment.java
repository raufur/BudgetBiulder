package com.epsilon.coders.budgetbiulder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.NavDrawer.NavigationDrawerFragment;
import com.epsilon.coders.budgetbiulder.Utility.PreferencesUtility;


public class OverviewFragment extends Fragment {
    private DatabaseHandler db;
    private TextView totalAmount, realAmount;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }
    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieve the fragment of the Navigation Drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        db = DatabaseHandler.getInstance(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Indicate that this fragment uses an ActionBar
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //Highlight in the navigation drawer
        NavigationDrawerFragment.setNavItemChecked(MainActivity.OVERVIEW_FRAGMENT);

        totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        realAmount = (TextView) view.findViewById(R.id.realAmount);
        ImageButton addButton = (ImageButton) view.findViewById(R.id.addButton);
        ((GradientDrawable)addButton.getBackground()).setColor(getResources().getColor(R.color.blue));

        refresh();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTransaction();
            }
        });

        totalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransactionFragment();
            }
        });

        realAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransactionFragment();
            }
        });

        return view;
    }

    private void openTransactionFragment(){
        Fragment newFragment = new TransactionListFragment();

        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createNewTransaction(){
        Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
        intent.putExtra("typeOfDialog", TransactionActionActivity.ADD_NEW_TRANSACTIION);
        startActivityForResult(intent, TransactionActionActivity.ADD_NEW_TRANSACTIION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case TransactionActionActivity.ADD_NEW_TRANSACTIION:
                refresh();
                break;
        }
    }

    private void refresh(){
        //Retrieve account number
        int account = PreferencesUtility.getAccount(getActivity());
        totalAmount.setText(db.getTotalAmount(account)+"");
        realAmount.setText(db.getRealAmount(account)+"");
    }

    public void restoreActionBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle(getString(R.string.title_fragment_overview));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
        }
    }
}
