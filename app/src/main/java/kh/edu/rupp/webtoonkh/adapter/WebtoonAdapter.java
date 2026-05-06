package kh.edu.rupp.webtoonkh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kh.edu.rupp.webtoonkh.R;
import kh.edu.rupp.webtoonkh.model.Webtoon;

public class WebtoonAdapter extends RecyclerView.Adapter<WebtoonAdapter.WebtoonViewHolder> {

    private List<Webtoon> webtoonList;

    public WebtoonAdapter(List<Webtoon> webtoonList) {
        this.webtoonList = webtoonList;
    }

    @NonNull
    @Override
    public WebtoonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_webtoon, parent, false);
        return new WebtoonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebtoonViewHolder holder, int position) {
        Webtoon webtoon = webtoonList.get(position);

        holder.txtTitle.setText(webtoon.getTitle());
        holder.txtGenre.setText(webtoon.getGenre());

        Glide.with(holder.itemView.getContext())
                .load(webtoon.getCover_url())
                .into(holder.imgCover);
    }

    @Override
    public int getItemCount() {
        return webtoonList.size();
    }

    public static class WebtoonViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView txtTitle;
        TextView txtGenre;

        public WebtoonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtGenre = itemView.findViewById(R.id.txtGenre);
        }
    }
}