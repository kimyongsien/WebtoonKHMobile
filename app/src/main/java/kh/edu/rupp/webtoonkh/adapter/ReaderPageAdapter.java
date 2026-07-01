package kh.edu.rupp.webtoonkh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kh.edu.rupp.webtoonkh.R;
import kh.edu.rupp.webtoonkh.model.ChapterPage;

public class ReaderPageAdapter extends RecyclerView.Adapter<ReaderPageAdapter.ReaderPageViewHolder> {

    private List<ChapterPage> pageList;

    public ReaderPageAdapter(List<ChapterPage> pageList) {
        this.pageList = pageList;
    }

    @NonNull
    @Override
    public ReaderPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reader_page, parent, false);
        return new ReaderPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReaderPageViewHolder holder, int position) {
        ChapterPage page = pageList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(page.getImage_url())
                .into(holder.imgReaderPage);
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }

    public static class ReaderPageViewHolder extends RecyclerView.ViewHolder {

        ImageView imgReaderPage;

        public ReaderPageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgReaderPage = itemView.findViewById(R.id.imgReaderPage);
        }
    }
}