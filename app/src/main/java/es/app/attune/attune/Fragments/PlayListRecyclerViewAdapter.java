package es.app.attune.attune.Fragments;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Fragments.PlayListFragment.OnListFragmentInteractionListener;
import es.app.attune.attune.Modules.Tools;
import es.app.attune.attune.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AttPlaylist} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter<PlayListRecyclerViewAdapter.ViewHolder> {

    private final List<AttPlaylist> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PlayListRecyclerViewAdapter(List<AttPlaylist> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).getId());
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mTempoView.setText(String.valueOf(mValues.get(position).getTempo()));
        holder.mGenreView.setText(mValues.get(position).getGenre());
        holder.mSongsView.setText(String.valueOf(mValues.get(position).getSongs().size()));
        holder.mImagePlaylistView.setImageBitmap(Tools.StringToBitMap(mValues.get(position).getImage()) );

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mNameView;
        public final TextView mTempoView;
        public final TextView mGenreView;
        public final TextView mSongsView;
        public final ImageView mImagePlaylistView;
        public AttPlaylist mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = (TextView) view.findViewById(R.id.name);
            mTempoView = (TextView) view.findViewById(R.id.tempo);
            mGenreView = (TextView) view.findViewById(R.id.genre);
            mSongsView = (TextView) view.findViewById(R.id.songs);
            mImagePlaylistView = (ImageView) view.findViewById(R.id.image_playlist);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
