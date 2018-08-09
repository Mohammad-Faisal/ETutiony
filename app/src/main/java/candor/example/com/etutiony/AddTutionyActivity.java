package candor.example.com.etutiony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTutionyActivity extends AppCompatActivity {

    String mUserID;
    EditText desc , days;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tutiony);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mUserID = getIntent().getStringExtra("user_id");
        desc = findViewById(R.id.desc);
        days = findViewById(R.id.days);
    }

    public void upload(View view) {

        if(isDataAvailable()){
            ProgressDialog mProgress;
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Saving Data.......");
            mProgress.setMessage("please wait while we create your account");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            String description = desc.getText().toString();
            String dayNumber = days.getText().toString();

            int num = Integer.valueOf(dayNumber);


            Map< String, Object> tutionyMap = new HashMap<>();
            long timestamp = 1* new Date().getTime();

            tutionyMap.put("description" , description);
            tutionyMap.put("cycle" , num);
            tutionyMap.put("current_days" , 0);
            tutionyMap.put("time_stamp" , timestamp);

            firebaseFirestore.collection("tutionys").document(mUserID).collection("tutionys").add(tutionyMap).addOnSuccessListener(documentReference -> {
                mProgress.dismiss();
                finish();
            }).addOnFailureListener(e -> {
                mProgress.dismiss();
                Toast.makeText(AddTutionyActivity.this, "Some error occured ... please try again ", Toast.LENGTH_SHORT).show();
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
