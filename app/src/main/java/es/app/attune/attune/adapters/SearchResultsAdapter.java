package es.app.attune.attune.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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

import es.app.attune.attune.R;
import es.app.attune.attune.database.Song;
import es.app.attune.attune.modules.Tools;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private final List<Song> mItems = new ArrayList<>();
    private final Context mContext;
    private final ItemSelectedListener mListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView title;
        public final TextView subtitle;
        public final ImageView image;
        public final TextView tempo;
        public final TextView date;
        public final TextView duration;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.entity_title);
            subtitle = itemView.findViewById(R.id.entity_subtitle);
            image = itemView.findViewById(R.id.entity_image);
            tempo = itemView.findViewById(R.id.entity_tempo);
            date = itemView.findViewById(R.id.entity_date);
            duration = itemView.findViewById(R.id.entity_duration);
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manual_mode_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song item = mItems.get(position);

        holder.title.setText(item.getName());

        String artists = item.getArtist();
        
        Joiner joiner = Joiner.on(", ");
        holder.subtitle.setText(artists);

        String image = item.getImage();
        if (image != null && !image.equals("")) {
            Picasso.with(mContext).load(image).placeholder(R.drawable.ic_image_gray_48dp).into(holder.image);
        }else{
            Picasso.with(mContext).load(R.drawable.ic_image_gray_48dp).into(holder.image);
        }
        holder.tempo.setText("Bpm: " + String.valueOf(item.getTempo()));
        holder.date.setText(mContext.getString(R.string.date) + " " + item.getDate());
        holder.duration.setText(mContext.getString(R.string.duration) + " " + Tools.timeConversion((int) item.getDuration()/1000));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
