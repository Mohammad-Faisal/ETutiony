package candor.example.com.etutiony;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import candor.example.com.etutiony.database.AppDatabase;


public class AddExamActivity extends AppCompatActivity {

    String mUserID;
    EditText name , number;
    String correct_score  = "", incorrect_score = ""  , nameString = "" , numberString = "";
    private Spinner mCorrectSpinner , mIncorrectSpinner;
    FirebaseFirestore firebaseFirestore;
    public static AppDatabase appDatabase;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mUserID = getIntent().getStringExtra("user_id");
        name = findViewById(R.id.name);
        number = findViewById(R.id.number_of_questions);
        mCorrectSpinner = findViewById(R.id.correct_spinner);
        mIncorrectSpinner = findViewById(R.id.incorrect_spinner);
        mCorrectSpinner.setOnItemSelectedListener(new ItemSelectedListener());
        mIncorrectSpinner.setOnItemSelectedListener(new ItemSelectedListener());


        //Room database
        appDatabase = Room.databaseBuilder(this, AppDatabase.class , "ExamDao").build();


    }





    public void upload(View view) {
        if(isDataValid()){

            if(isDataAvailable()){
                ProgressDialog mProgress;
                mProgress = new ProgressDialog(this);
                mProgress.setTitle("Saving Data.......");
                mProgress.setMessage("please wait while we create your account");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();



                Map< String, Object> tutionyMap = new HashMap<>();
                long timestamp = 1* new Date().getTime();

                tutionyMap.put("timestamp" , timestamp);
                tutionyMap.put("name" , nameString);
                tutionyMap.put("number_of_question" , numberString);
                tutionyMap.put("correct_score" , correct_score);
                tutionyMap.put("incorrect_score" , incorrect_score);


                ExamItem item = new ExamItem.Builder().setName(nameString)
                        .setTimestamp(timestamp)
                        .setCorrectScore(correct_score)
                        .setInCorrectScore(incorrect_score)
                        .setNumberOfQuestion(numberString)
                        .create();


                saveTheExamOffline(item);


                //appDatabase.examDao().addExam(item);



                firebaseFirestore.collection("exams").document(mUserID).collection("exams").add(tutionyMap).addOnSuccessListener(documentReference -> {
                    mProgress.dismiss();
                    //finish();
                }).addOnFailureListener(e -> {
                    mProgress.dismiss();
                    Toast.makeText(AddExamActivity.this, "Some error occured ... please try again ", Toast.LENGTH_SHORT).show();
                });


            }else{
                Toast.makeText(this, "please enable your data !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveTheExamOffline(ExamItem item) {

        new Thread(new Runnable() {
            public void run() {
                appDatabase.examDao().addExam(item);
            }
        }).start();
    }


    private boolean isDataValid(){
        nameString = name.getText().toString();
        numberString = number.getText().toString();

        if(nameString.equals("")){
            Toast.makeText(this, "Please select a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(numberString.equals("")){
            Toast.makeText(this, "Please select number of questions", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (correct_score.equals("")) {
            Toast.makeText(this, "Please select correct score of questions", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (correct_score.equals("")) {
            Toast.makeText(this, "Please select incorrect score of questions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isDataAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    private class ItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getId() == R.id.correct_spinner){
                correct_score = String.valueOf(mCorrectSpinner.getSelectedItem());
            }else{
                incorrect_score = String.valueOf(mIncorrectSpinner.getSelectedItem());
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
