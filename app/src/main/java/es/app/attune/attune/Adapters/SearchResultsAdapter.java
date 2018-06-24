package es.app.attune.attune.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.app.attune.attune.Database.Song;
import es.app.attune.attune.R;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private final List<Song> mItems = new ArrayList<>();
    private final Context mContext;
    private final ItemSelectedListener mListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView title;
        public final TextView subtitle;
        public final ImageView image;
        public final TextView features;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.entity_title);
            subtitle = (TextView) itemView.findViewById(R.id.entity_subtitle);
            image = (ImageView) itemView.findViewById(R.id.entity_image);
            features = (TextView) itemView.findViewById(R.id.entity_features);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(getLayoutPosition());
            mListener.onItemSelected(v, mItems.get(getAdapterPosition()));
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(View itemView, Song item);
    }

    public SearchResultsAdapter(Context context, ItemSelectedListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void clearData() {
        mItems.clear();
    }

    public void addData(List<Song> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manual_mode_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song item = mItems.get(position);

        holder.title.setText(item.getName());

        List<String> names = new ArrayList<>();
        /*for (ArtistSimple i : item.artists) {
            names.add(i.name);
        }*/

        names.add(item.getArtist());

        Joiner joiner = Joiner.on(", ");
        holder.subtitle.setText(joiner.join(names));

        String image = item.getImage();
        if (image != null) {
            Picasso.with(mContext).load(image).into(holder.image);
        }

        holder.features.setText("Bpm: " + String.valueOf(item.getTempo()) + " " + mContext.getString(R.string.date) + "  " + item.getDate());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
