package be.ehb.elviraiskhakova.application1.challenges;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.ehb.elviraiskhakova.application1.MyDemoStoryLineDBHelper;
import be.ehb.elviraiskhakova.application1.R;
import cz.mendelu.busItWeek.library.ImageSelectPuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;

public class ImageSelectActivity extends AppCompatActivity {

    private TextView counter;
    private TextView question;
    private RecyclerView imageList;
    private StoryLine storyLine;
    private Task currentTask;
    private ImageSelectPuzzle puzzle;
    private List<Integer> answers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        counter = (TextView) findViewById(R.id.counter);
        question = (TextView) findViewById(R.id.question);
        imageList = (RecyclerView) findViewById(R.id.imageList);

        storyLine = StoryLine.open(this, MyDemoStoryLineDBHelper.class);

        currentTask = storyLine.currentTask();
        puzzle = (ImageSelectPuzzle) currentTask.getPuzzle();
        question.setText(puzzle.getQuestion());

//        Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();


        imageList = findViewById(R.id.imageList);
        ImageSelectListAdapter adapter = new ImageSelectListAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        imageList.setLayoutManager(layoutManager);
        imageList.setAdapter(adapter);
        imageList.setNestedScrollingEnabled(false);

        answers = new ArrayList<>();

        for (Map.Entry<Integer, Boolean> entry : puzzle.getImages().entrySet()) {
            answers.add(entry.getKey());
        }
        adapter.notifyDataSetChanged();

    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {

        public ImageView answer;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.image);
        }
    }

    private class ImageSelectListAdapter extends RecyclerView.Adapter<AnswerViewHolder> {


        @Override
        public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_select_list, parent, false);
            return new AnswerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AnswerViewHolder holder, final int position) {
            Integer answer = answers.get(position);
            Picasso.with(ImageSelectActivity.this)
                    .load(answer)
                    .into(holder.answer);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (puzzle.getAnswerForImage(holder.getAdapterPosition())) {
                        //correct answer
                        currentTask.finish(true);
                        Toast.makeText(ImageSelectActivity.this, "Correct answer"
                                + "", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // wrong answer
                        Toast.makeText(ImageSelectActivity.this, "Wrong answer"
                                + "", Toast.LENGTH_SHORT).show();
                        currentTask.finish(false);
                        finish();
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            if (answers == null) {
                answers = new ArrayList<>();
            }
            return answers.size();
        }
    }
}


