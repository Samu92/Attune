package es.app.attune.attune.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import es.app.attune.attune.Activity.MainActivity;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private SongsListFragment.OnListFragmentInteractionListener mListener;
    private static DatabaseFunctions db;
    private static RecyclerView recyclerView;
    private SongListRecyclerViewAdapter adapter;
    private static String playlistId;
    private TextView empty;

    public SongsListFragment() {
        // Required empty public constructor
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SongsListFragment newInstance(DatabaseFunctions database, String id) {
        SongsListFragment fragment = new SongsListFragment();
        db = database;
        playlistId = id;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        adapter = new SongListRecyclerViewAdapter(db.getSongs(playlistId), mListener, getContext());

        // Set the adapter
        if (view instanceof RelativeLayout) {
            Context context = view.getContext();
            recyclerView = view.findViewById(R.id.songs_list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
            itemTouchHelper.attachToRecyclerView(recyclerView);

            empty = view.findViewById(R.id.empty_song_view);

            if(adapter.getItemCount() == 0){
                recyclerView.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
            }

            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
            if(MainActivity.getSlideVisible()){
                marginLayoutParams.setMargins(0, 0, 0, 200);
                recyclerView.setLayoutParams(marginLayoutParams);
                recyclerView.requestLayout();
            }else{
                marginLayoutParams.setMargins(0, 0, 0, 0);
                recyclerView.setLayoutParams(marginLayoutParams);
                recyclerView.requestLayout();
            }
        }
        return view;
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        adapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition(),db);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        adapter.deleteItem(viewHolder.getAdapterPosition(),db);

                        if(adapter.getItemCount() == 0){
                            recyclerView.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                        }else{
                            recyclerView.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                        }
                    }
                };
        return simpleCallback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SongsListFragment.OnListFragmentInteractionListener) {
            mListener = (SongsListFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void setMarginBottomList(int mode){
        if(recyclerView != null){
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
            if(mode == 0){
                marginLayoutParams.setMargins(0, 0, 0, 0);
                recyclerView.setLayoutParams(marginLayoutParams);
                recyclerView.requestLayout();
            }else if(mode == 1){
                marginLayoutParams.setMargins(0, 0, 0, 200);
                recyclerView.setLayoutParams(marginLayoutParams);
                recyclerView.requestLayout();
            }
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Song item, boolean manual);
    }

}
