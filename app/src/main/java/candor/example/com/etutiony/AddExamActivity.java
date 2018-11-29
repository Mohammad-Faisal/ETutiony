package candor.example.com.etutiony;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddExamActivity extends AppCompatActivity {

    String mUserID;
    EditText name , number;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mUserID = getIntent().getStringExtra("user_id");
        name = findViewById(R.id.name);
        number = findViewById(R.id.number_of_questions);
    }

    public void upload(View view) {

        if(isDataAvailable()){
            ProgressDialog mProgress;
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Saving Data.......");
            mProgress.setMessage("please wait while we create your account");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            String nameString = name.getText().toString();
            String numberString = number.getText().toString();

            int num = Integer.valueOf(numberString);


            Map< String, Object> tutionyMap = new HashMap<>();
            long timestamp = 1* new Date().getTime();

            tutionyMap.put("name" , nameString);
            tutionyMap.put("number_of_question" , num);
            tutionyMap.put("correct_score" , 0);
            tutionyMap.put("incorrect_score" , timestamp);

            firebaseFirestore.collection("exams").document(mUserID).collection("exams").add(tutionyMap).addOnSuccessListener(documentReference -> {
                mProgress.dismiss();
                finish();
            }).addOnFailureListener(e -> {
                mProgress.dismiss();
                Toast.makeText(AddExamActivity.this, "Some error occured ... please try again ", Toast.LENGTH_SHORT).show();
            });


        }else{
            Toast.makeText(this, "please enable your data !", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isDataAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
