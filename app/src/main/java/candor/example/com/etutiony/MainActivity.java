package candor.example.com.etutiony;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String mUserID , mUserName , mUserImage ,  mUserThumbImage;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    ArrayList<TutionyItem> tutionyList = new ArrayList<>();
    TutionyListAdapter tutionyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recycler);
        firebaseFirestore = FirebaseFirestore.getInstance();



        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            final FirebaseUser mUser = firebaseAuth.getCurrentUser();
            if (mUser != null) {
                mUserID = mUser.getUid();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("users").document(mUserID);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (!document.exists()) {
                            Intent regIntent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
                            startActivity(regIntent);
                            finish();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
            }else {
                if(!isDataAvailable()){
                    Toast.makeText(this, "Please enable your data to continue", Toast.LENGTH_SHORT).show();
                }else{
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };


        //setting RecyclerView
        tutionyList = new ArrayList<>();
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this , 1));
        tutionyListAdapter = new TutionyListAdapter(tutionyList, this , this);
        recyclerView.setAdapter(tutionyListAdapter);

        loadTutionys();

    }


    public void loadTutionys(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query nextQuery = firebaseFirestore.collection("tutionys").document(mUserID).collection("tutionys").orderBy("time_stamp" , Query.Direction.DESCENDING).limit(100);
        nextQuery.get().addOnSuccessListener(documentSnapshots -> {
            if(documentSnapshots!=null){
                if(!documentSnapshots.isEmpty()){
                    for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            Log.d(TAG, "loadMorePost:    found some more data  !!!!!");
                            TutionyItem singleTutiony = doc.getDocument().toObject(TutionyItem.class);
                            tutionyList.add(singleTutiony);
                        }
                    }
                    tutionyListAdapter.notifyDataSetChanged();
                }
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                mUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DocumentReference docRef = db.collection("users").document(mUserID);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (!document.exists()) {
                            Intent regIntent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
                            startActivity(regIntent);
                            finish();
                        }else{
                            mUserImage = task.getResult().getString("image");
                            mUserName = task.getResult().getString("name");
                            mUserThumbImage = task.getResult().getString("thumb_image");
                            mUserImage = task.getResult().getString("image");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });


            }else{
                Toast.makeText(this, "Failed for some reason please check your internet connnection ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }


    private boolean isDataAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void openAddTutiony(View view) {
        Intent intent = new Intent(MainActivity.this , AddTutionyActivity.class);
        intent.putExtra("user_id" , mUserID);
        startActivity(intent);
    }
}
