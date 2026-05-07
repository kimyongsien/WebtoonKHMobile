package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class StoryDetailActivity extends AppCompatActivity {

    ImageView imgDetailCover;
    TextView txtDetailTitle, txtDetailAuthor, txtDetailGenre, txtDetailDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        imgDetailCover = findViewById(R.id.imgDetailCover);
        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailAuthor = findViewById(R.id.txtDetailAuthor);
        txtDetailGenre = findViewById(R.id.txtDetailGenre);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);

        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String genre = getIntent().getStringExtra("genre");
        String description = getIntent().getStringExtra("description");
        String coverUrl = getIntent().getStringExtra("cover_url");

        txtDetailTitle.setText(title);
        txtDetailAuthor.setText(author);
        txtDetailGenre.setText(genre);
        txtDetailDescription.setText(description);

        Glide.with(this)
                .load(coverUrl)
                .into(imgDetailCover);
    }
}