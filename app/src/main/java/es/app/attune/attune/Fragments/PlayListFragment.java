package es.app.attune.attune.Fragments;

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
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import es.app.attune.attune.Activity.MainActivity;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Classes.SearchInterfaces;
import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PlayListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static DatabaseFunctions db;
    private static RecyclerView recyclerView;
    private static PlayListRecyclerViewAdapter adapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

            empty = getView().findViewById(R.id.empty_view);

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
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
                        adapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition(),db);
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                        question = new MaterialDialog.Builder(getActivity())
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
        return simpleCallback;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.playlist_context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        long itemID = info.position;
        menu.setHeaderTitle("lior" + itemID);
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

    public static void update() {
        adapter.notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AttPlaylist item, boolean reproducir);
    }
}
