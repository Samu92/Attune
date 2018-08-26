package es.app.attune.attune.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Objects;

import es.app.attune.attune.R;
import es.app.attune.attune.classes.DatabaseFunctions;
import es.app.attune.attune.database.AttPlaylist;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PlayListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static DatabaseFunctions db;
    private static RecyclerView recyclerView;
    private static PlayListRecyclerViewAdapter adapter;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private TextView empty;
    private MaterialDialog question;
    private TextView txt_question;

    public PlayListFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlayListFragment newInstance(DatabaseFunctions database) {
        PlayListFragment fragment = new PlayListFragment();
        db = database;
        return fragment;
    }

    public static void update() {
        adapter.notifyDataSetChanged();
    }

    public static void scrollToLastPosition() {
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the adapter
        if (view instanceof RelativeLayout) {

            if (getArguments() != null) {
                mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            }

            adapter = new PlayListRecyclerViewAdapter(db.getPlaylists(), mListener, getContext());

            Context context = view.getContext();
            recyclerView = view.findViewById(R.id.list);

            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);

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

            empty = Objects.requireNonNull(getView()).findViewById(R.id.empty_view);

            if(adapter.getItemCount() == 0){
                recyclerView.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
            }
        }
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView1, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
                adapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition(),db);
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
                                adapter.deleteItem(viewHolder.getAdapterPosition(),db);
                                if(adapter.getItemCount() == 0){
                                    recyclerView.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                }else{
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
                txt_question.setText(R.string.playlist_delete);
                question.show();
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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

    @Override
    public void onResume() {
        recountSongsAndDuration();
        super.onResume();
    }

    @Override
    public void onStart() {
        recountSongsAndDuration();
        super.onStart();
    }

    private void recountSongsAndDuration() {
        adapter.notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AttPlaylist item, boolean reproducir);
    }
}
