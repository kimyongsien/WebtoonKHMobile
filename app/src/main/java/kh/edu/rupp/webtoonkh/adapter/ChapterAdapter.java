package kh.edu.rupp.webtoonkh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kh.edu.rupp.webtoonkh.R;
import kh.edu.rupp.webtoonkh.ReaderActivity;
import kh.edu.rupp.webtoonkh.model.Chapter;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private List<Chapter> chapterList;

    public ChapterAdapter(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);

        holder.txtChapterNumber.setText(String.valueOf(chapter.getChapter_number()));
        holder.txtChapterTitle.setText(chapter.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ReaderActivity.class);
            intent.putExtra("chapter_id", chapter.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {

        TextView txtChapterNumber;
        TextView txtChapterTitle;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);

            txtChapterNumber = itemView.findViewById(R.id.txtChapterNumber);
            txtChapterTitle = itemView.findViewById(R.id.txtChapterTitle);
        }
    }
}