package es.app.attune.attune.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Modules.Tools;
import es.app.attune.attune.R;

public class SongListRecyclerViewAdapter extends RecyclerView.Adapter<SongListRecyclerViewAdapter.ViewHolder> {
    private final List<Song> mValues;
    private final SongsListFragment.OnListFragmentInteractionListener mListener;
    private Context context;

    public SongListRecyclerViewAdapter(List<Song> items, SongsListFragment.OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_songs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mNameView.setText(mValues.get(position).getName());

        holder.mArtistView.setText(context.getResources().getString(R.string.txt_artist) + mValues.get(position).getArtist());

        holder.mTempoView.setText(context.getResources().getString(R.string.txt_tempo) + String.valueOf(mValues.get(position).getTempo()));

        int duration = (int) (mValues.get(position).getDuration()/1000);
        holder.mDurationView.setText("Duración: " + Tools.timeConversion(duration));

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

        if(!mValues.get(position).getImage().equals("")){
            Picasso.with(context).
                    load(mValues.get(position).getImage())
                    .placeholder(R.drawable.baseline_add_photo_alternate_white_48)
                    .into(holder.image);
        }else{
            Picasso.with(context).
                    load(R.drawable.baseline_add_photo_alternate_white_48)
                    .placeholder(R.drawable.baseline_add_photo_alternate_white_48)
                    .into(holder.image);
        }

        holder.mDateView.setText(context.getResources().getString(R.string.date) + mValues.get(position).getDate());

        switch (mValues.get(position).getEffect_type()){
            case 0:
                holder.mEffectView.setText(R.string.effect_no_effect);
                break;
            case 1:
                holder.mEffectView.setText(R.string.effect_fade);
                break;
            case 2:
                holder.mEffectView.setText(R.string.effect_overlap);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    void deleteItem(int index, DatabaseFunctions db) {
        db.removeSong(mValues.get(index).getId());
        db.recalculatePlaylistDuration(mValues.get(index).getIdPlaylist());

        mValues.remove(index);
        notifyItemRemoved(index);
    }

    void moveItem(int oldIndex, int newIndex, DatabaseFunctions db){
        Song itemA = mValues.get(oldIndex);
        Song itemB = mValues.get(newIndex);
        mValues.set(oldIndex, itemB);
        mValues.set(newIndex, itemA);

        db.changeSongPosition(itemA, itemB, newIndex, oldIndex);

        notifyItemMoved(oldIndex,newIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mArtistView;
        public final TextView mTempoView;
        public final TextView mDurationView;
        public final ImageView image;
        public final TextView mDateView;
        public final TextView mEffectView;
        public Song mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.song_name);
            mArtistView = view.findViewById(R.id.song_artist);
            mTempoView = view.findViewById(R.id.song_tempo);
            mDurationView = view.findViewById(R.id.song_duration);
            image = view.findViewById(R.id.entity_song_image);
            mDateView = view.findViewById(R.id.song_date);
            mEffectView = view.findViewById(R.id.song_effect);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
