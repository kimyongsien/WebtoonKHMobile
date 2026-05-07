package kh.edu.rupp.webtoonkh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import kh.edu.rupp.webtoonkh.StoryDetailActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kh.edu.rupp.webtoonkh.R;
import kh.edu.rupp.webtoonkh.model.Webtoon;

public class WebtoonAdapter extends RecyclerView.Adapter<WebtoonAdapter.WebtoonViewHolder> {

    private final List<Webtoon> webtoonList;

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
                .centerCrop()
                .into(holder.imgCover);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), StoryDetailActivity.class);

            intent.putExtra("title", webtoon.getTitle());
            intent.putExtra("author", webtoon.getAuthor());
            intent.putExtra("genre", webtoon.getGenre());
            intent.putExtra("description", webtoon.getDescription());
            intent.putExtra("cover_url", webtoon.getCover_url());

            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return webtoonList.size();
    }

    static class WebtoonViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgCover;
        final TextView txtTitle;
        final TextView txtGenre;


        WebtoonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtGenre = itemView.findViewById(R.id.txtGenre);
        }
    }
}
