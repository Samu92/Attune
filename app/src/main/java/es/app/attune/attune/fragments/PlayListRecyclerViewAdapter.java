package es.app.attune.attune.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import es.app.attune.attune.R;
import es.app.attune.attune.classes.DatabaseFunctions;
import es.app.attune.attune.database.AttPlaylist;
import es.app.attune.attune.fragments.PlayListFragment.OnListFragmentInteractionListener;
import es.app.attune.attune.modules.Tools;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AttPlaylist} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter<PlayListRecyclerViewAdapter.ViewHolder> {

    private final List<AttPlaylist> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    PlayListRecyclerViewAdapter(List<AttPlaylist> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).getId());
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mTempoView.setText(context.getResources().getString(R.string.txt_tempo) + String.valueOf(mValues.get(position).getTempo()));
        holder.mGenreView.setText(context.getResources().getString(R.string.txt_genre) + mValues.get(position).getGenre());
        holder.mSongsView.setText(context.getResources().getString(R.string.txt_songs) + String.valueOf(mValues.get(position).getSongs().size()));
        int duration = mValues.get(position).getDuration()/1000;
        holder.mDurationView.setText("Duración: " + Tools.timeConversion(duration));

        holder.itemView.setLongClickable(true);

        Glide.with(context)
                .load(mValues.get(position).getImage())
                .into(holder.mImagePlaylistView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, false);
                }
            }
        });
    }

    void deleteItem(int index, DatabaseFunctions db) {
        db.removePlaylist(mValues.get(index).getId());
        notifyDataSetChanged();

        mValues.remove(index);
        notifyItemRemoved(index);
    }

    void moveItem(int oldIndex, int newIndex, DatabaseFunctions db){
        AttPlaylist itemA = mValues.get(oldIndex);
        AttPlaylist itemB = mValues.get(newIndex);
        mValues.set(oldIndex, itemB);
        mValues.set(newIndex, itemA);

        db.changePosition(itemA, itemB, newIndex, oldIndex);

        notifyItemMoved(oldIndex,newIndex);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        //public final TextView mIdView;
        final TextView mNameView;
        final TextView mTempoView;
        final TextView mGenreView;
        final TextView mSongsView;
        final ImageView mImagePlaylistView;
        final TextView mDurationView;
        public AttPlaylist mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = view.findViewById(R.id.name);
            mTempoView = view.findViewById(R.id.tempo);
            mGenreView = view.findViewById(R.id.genre);
            mSongsView = view.findViewById(R.id.songs);
            mImagePlaylistView = view.findViewById(R.id.image_playlist);
            mDurationView = view.findViewById(R.id.duration);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
