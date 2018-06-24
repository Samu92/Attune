package es.app.attune.attune.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import es.app.attune.attune.Adapters.SearchResultsAdapter;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Classes.ResultListScrollListener;
import es.app.attune.attune.Classes.SearchFunctions;
import es.app.attune.attune.Classes.SearchInterfaces;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.R;
import kaaes.spotify.webapi.android.models.Track;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManualMode extends Fragment {

    private static DatabaseFunctions db;
    private SearchView searchView;
    private static SearchInterfaces.ActionListener mActionListener;

    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private LinearLayoutManager mLayoutManager;
    private ScrollListener mScrollListener;
    private SearchResultsAdapter mAdapter;

    public ManualMode() {
        // Required empty public constructor
    }

    public void setAdapter(List<Song> items) {
        mAdapter.addData(items);
    }

    private class ScrollListener extends ResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mActionListener.loadMoreResults();
        }
    }

    public static ManualMode newInstance(DatabaseFunctions database, SearchInterfaces.ActionListener mActionListenerArg) {
        ManualMode fragment = new ManualMode();
        db = database;
        mActionListener = mActionListenerArg;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_mode, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getContext());
        mScrollListener =  new ScrollListener(mLayoutManager);

        searchView = (SearchView) getView().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                reset();
                mActionListener.search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // Setup search results list
        mAdapter = new SearchResultsAdapter(getActivity(), new SearchResultsAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, Song item) {
                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView resultsList = (RecyclerView) view.findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        // If fragment was recreated wit active search restore it
        String currentQuery = mActionListener.getRecordedQuery(KEY_CURRENT_QUERY);
        mActionListener.search(currentQuery);
        searchView.clearFocus();
    }

    private void reset() {
        mScrollListener.reset();
        mAdapter.clearData();
    }
}
