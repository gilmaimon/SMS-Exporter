package com.gil.smsexporter.Contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gil.smsexporter.R;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment {

    private List<String> mAllContacts = new ArrayList<>();

    private String mCurrentFilter = "";
    private List<String> mFilteredContacts = new ArrayList<>();

    private OnContactClickedListener mListener = null;
    private RecyclerView.Adapter mAdapter = null;


    public static ContactsFragment instance(List<String> contacts, final OnContactClickedListener listener) {
        ContactsFragment fragment = new ContactsFragment();

        fragment.setContacts(contacts);
        fragment.setListener(listener);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Contacts");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.recycler_view_layout, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView(
                (RecyclerView) getView().findViewById(R.id.recycler_view)
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                filterContacts(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                filterContacts(query);
                return false;
            }
        });
    }

    void initRecyclerView(RecyclerView recyclerView) {
        filterContacts(mCurrentFilter);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new ContactsRecyclerViewAdapter(getActivity(), mFilteredContacts, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mListener == null) return;
                mListener.onContactClicked(mFilteredContacts.get(i), i);
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    void filterContacts(String filter) {
        mCurrentFilter = filter;
        mFilteredContacts.clear();
        for(String contact : mAllContacts) {
            if(contact.toLowerCase().contains(filter.toLowerCase())) mFilteredContacts.add(contact);
        }

        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public List<String> getContacts() {
        return mFilteredContacts;
    }

    public void setContacts(List<String> contacts) {
        this.mAllContacts = contacts;
        filterContacts(mCurrentFilter);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setListener(OnContactClickedListener listener) {
        this.mListener = listener;
    }

    public void removeListener(OnContactClickedListener listener) {
        this.mListener = null;
    }
}
