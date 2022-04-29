package com.example.dolearn.test;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dolearn.R;
import com.example.dolearn.note.NoteActivity;
import com.example.dolearn.topic.Dictionary;
import com.example.dolearn.topic.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    TextView total_question_tv, question_tv;
    Button ans_a_btn, ans_b_btn, ans_c_btn, ans_d_btn, submit_btn;

    ArrayList<Item> noteList = (ArrayList<Item>) NoteActivity.listNote.clone();
    ArrayList<Boolean> addedChoice = new ArrayList<Boolean>(Arrays.asList(new Boolean[noteList.size()]));
    ArrayList<String> words = new ArrayList<String>();
    ArrayList<String> shuffledList = new ArrayList<String>();
    ArrayList<String> choices = new ArrayList<String>();

    int score = 0;
    int totalQuestion = noteList.size();
    int currentQuestionIndex = 0;
    boolean allowPress = true;
    String selectedAnswer = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);

        total_question_tv = findViewById(R.id.total_question_tv);
        question_tv = findViewById(R.id.question_tv);
        ans_a_btn = findViewById(R.id.ans_a_btn);
        ans_b_btn = findViewById(R.id.ans_b_btn);
        ans_c_btn = findViewById(R.id.ans_c_btn);
        ans_d_btn = findViewById(R.id.ans_d_btn);
//        submit_btn = findViewById(R.id.submit_btn);

        ans_a_btn.setOnClickListener(this);
        ans_b_btn.setOnClickListener(this);
        ans_c_btn.setOnClickListener(this);
        ans_d_btn.setOnClickListener(this);
//        submit_btn.setOnClickListener(this);

//        total_question_tv.setText("Total questions: " + totalQuestion);
        total_question_tv.setText("Tổng số câu hỏi: " + noteList.size());





//        System.out.println(noteList.size());
//        Random random = new Random();
//        System.out.println(random.nextInt(5));

        for(int i = 0; i < noteList.size(); i++){
            String[] tokens = noteList.get(i).getEngName().split(" ");
            words.add(tokens[0]);
//            shuffledList.add(noteList.get(i).getVieName());
        }


        loadNewQuestion();

        System.out.println(words);
        System.out.println(choices);

    }

    @Override
    public void onClick(View view) {
        if(allowPress) {

            Button clickedButton = (Button) view;

            //        if(clickedButton == submit_btn){
            //            if(selectedAnswer == noteList.get(currentQuestionIndex).getVieName()){
            //                score++;
            //                System.out.println("Correct");
            //            }
            //
            //            currentQuestionIndex++;
            //            loadNewQuestion();
            //        }else{
            //            // choices button pressed
            //            selectedAnswer = clickedButton.getText().toString();
            //            clickedButton.setBackgroundResource(R.color.teal_700);
            //        }
            String correctAnswer = noteList.get(currentQuestionIndex).getVieName();

            selectedAnswer = clickedButton.getText().toString();
            if (selectedAnswer.equals(correctAnswer)) {
                clickedButton.setBackgroundResource(R.color.right_answer_color);
                score++;
            } else {
                clickedButton.setBackgroundResource(R.color.wrong_andswer_color);
                if (ans_a_btn.getText().toString().equals(correctAnswer)) {
                    ans_a_btn.setBackgroundResource(R.color.right_answer_color);
                }
                if (ans_b_btn.getText().toString().equals(correctAnswer)) {
                    ans_b_btn.setBackgroundResource(R.color.right_answer_color);
                }
                if (ans_c_btn.getText().toString().equals(correctAnswer)) {
                    ans_c_btn.setBackgroundResource(R.color.right_answer_color);
                }
                if (ans_d_btn.getText().toString().equals(correctAnswer)) {
                    ans_d_btn.setBackgroundResource(R.color.right_answer_color);
                }
            }

            //        try {
            //            Thread.sleep(1000);
            //        } catch (InterruptedException e) {
            //            e.printStackTrace();
            //        }
            //        int counter = 0;
            //        TimerTask timerTask = new TimerTask() {
            //            @Override
            //            public void run() {
            //                Log.e("Timer Task", String.valueOf(counter));
            //                counter++;
            //            }
            //        }
            //        Timer timer = new Timer(timerTask, 0, 1000);

            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long l) {
                    allowPress = false;
                }

                @Override
                public void onFinish() {
                    allowPress = true;
                    currentQuestionIndex++;
                    loadNewQuestion();
                }
            }.start();
        }
    }

    void loadNewQuestion(){
        if(currentQuestionIndex == totalQuestion){
            finishQuiz();
            return;
        }

//        question_tv.setText(QuestionAnswer.questions[currentQuestionIndex]);
//
//        ans_a_btn.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
//        ans_b_btn.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
//        ans_c_btn.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
//        ans_d_btn.setText(QuestionAnswer.choices[currentQuestionIndex][3]);

        ans_a_btn.setBackgroundColor(Color.WHITE);
        ans_b_btn.setBackgroundColor(Color.WHITE);
        ans_c_btn.setBackgroundColor(Color.WHITE);
        ans_d_btn.setBackgroundColor(Color.WHITE);

        question_tv.setText(words.get(currentQuestionIndex) + " có nghĩa là gì?");

        randomChoice();
        ans_a_btn.setText(choices.get(0));
        ans_b_btn.setText(choices.get(1));
        ans_c_btn.setText(choices.get(2));
        ans_d_btn.setText(choices.get(3));


    }

    void finishQuiz(){
        String passStatus = "";

        if(score >= totalQuestion * 0.6){
//            passStatus = "Pass";
            passStatus = "Đạt";
        }else{
//            passStatus = "Failed";
            passStatus = "Không đạt";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
//                .setMessage("Score is " + score + " out of " + totalQuestion)
                .setMessage("Trả lời đúng " + score + " trong tổng số " + totalQuestion + " câu hỏi")
//                .setPositiveButton("Restart", ((dialogInterface, i) -> restartQuiz()))
                .setPositiveButton("Bắt đầu lại", ((dialogInterface, i) -> restartQuiz()))
                .setCancelable(false)
                .show();
    }

    void restartQuiz(){
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
    }

    void randomChoice(){
        choices.clear();
        choices.add(noteList.get(currentQuestionIndex).getVieName());

        Random random = new Random();
        int randomPos = random.nextInt(noteList.size());

        Collections.fill(addedChoice, Boolean.FALSE);
        System.out.println(addedChoice);
        for(int i = 0; i < 3; i++){
            while (randomPos == currentQuestionIndex || addedChoice.get(randomPos))
                randomPos = random.nextInt(noteList.size());
            choices.add(noteList.get(randomPos).getVieName());
            addedChoice.set(randomPos, true);
        }
        Collections.shuffle(choices);
    }
}
