package es.app.attune.attune.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Objects;

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
    private static SongListRecyclerViewAdapter adapter;
    private static String playlistId;
    private TextView empty;
    private MaterialDialog question;
    private TextView txt_question;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        }
        return view;
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView1, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                adapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition(), db);
                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                question = new MaterialDialog.Builder(Objects.requireNonNull(getActivity()))
                        .customView(R.layout.question_layout, false)
                        .cancelable(true)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                adapter.deleteItem(viewHolder.getAdapterPosition(), db);
                                if (adapter.getItemCount() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    empty.setVisibility(View.GONE);
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .build();
                txt_question = question.getView().findViewById(R.id.txt_question);
                txt_question.setText(R.string.song_delete);
                question.show();
            }
        };
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

    public static void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Song item, boolean manual);
    }

}
