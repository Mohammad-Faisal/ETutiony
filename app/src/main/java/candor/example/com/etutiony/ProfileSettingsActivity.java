package candor.example.com.etutiony;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {


    private static final String TAG = "ProfileSettingsActivity";

    private StorageReference mStorage;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference imageFilePath;
    private StorageReference thumbFilePath;
    private byte[] thumb_byte;
    private ProgressDialog mProgress;


    String type = "male";
    String name , instution;
    Uri imageUri = null;
    private String mainImageUrl = "";
    private String  thumbImageUrl = "";
    private String mUserID;
    CircleImageView regImage;
    RadioGroup roadDirectionGroup;
    RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        regImage= findViewById(R.id.image);
        mUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        if(getSupportActionBar()!=null)getSupportActionBar().setTitle("Profile Settings");
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseFirestore =FirebaseFirestore.getInstance();
        roadDirectionGroup = findViewById(R.id.radio_group);
    }



    public void upload(View view) {

        EditText nameEdit , instEdit;
        nameEdit = findViewById(R.id.name);
        instEdit = findViewById(R.id.inst);

        name =  nameEdit.getText().toString();
        instution = instEdit.getText().toString();

        if(name.equals("")){
            Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
        }else{
            mProgress = new ProgressDialog(ProfileSettingsActivity.this);
            mProgress.setTitle("Saving Data.......");
            mProgress.setMessage("please wait while we create your account");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            if(imageUri==null){
                uploadDataWithOutImage();
            }else{
                uploadDataWIthImage();
            }
        }

    }

    private void uploadDataWIthImage(){
        imageFilePath.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri downloadUrlImage = taskSnapshot.getDownloadUrl();
                    mainImageUrl =  downloadUrlImage.toString();
                    UploadTask uploadThumbTask = thumbFilePath.putBytes(thumb_byte);
                    uploadThumbTask.addOnFailureListener(exception -> {
                        Toast.makeText(ProfileSettingsActivity.this, "Some error occured. check your internet connection", Toast.LENGTH_SHORT).show();
                        Log.w("Thumb  Photo Upload:  " , exception);
                    }).addOnSuccessListener(taskSnapshot1 -> {
                        Uri downloadUrlThumb = taskSnapshot1.getDownloadUrl();
                        thumbImageUrl  = downloadUrlThumb.toString();
                        uploadDataWithOutImage();
                    });
                })
                .addOnFailureListener(exception -> {
                    mProgress.dismiss();
                    Toast.makeText(ProfileSettingsActivity.this, "Some error occured. check your internet connection", Toast.LENGTH_SHORT).show();
                    Log.w("Main Photo Upload   :  " , exception);
                });
    }

    public void uploadDataWithOutImage(){
        EditText nameEdit , instEdit;
        nameEdit = findViewById(R.id.name);
        instEdit = findViewById(R.id.inst);

        //now saving the data to firestore
        String name =  nameEdit.getText().toString();
        String instution = instEdit.getText().toString();

        String deviceTokenID = FirebaseInstanceId.getInstance().getToken();
        Map< String, Object> userMap = new HashMap<>();

        userMap.put("name" , name);
        userMap.put("instution" , instution);
        userMap.put("type" , type);
        userMap.put("image" , mainImageUrl);
        userMap.put("thumb_image" , thumbImageUrl);
        userMap.put("device_id" , deviceTokenID);

        firebaseFirestore.collection("users").document(mUserID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            public static final String TAG ="registration process " ;

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Intent mainIntent = new Intent(ProfileSettingsActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    mProgress.dismiss();
                    finish();

                }else{
                    mProgress.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProfileSettingsActivity.this, "Some error occured. check your internet connection", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: "+ error);
                }
            }
        });
    }

    public void pickImage(View view) {
        Utils.BringImagePicker(ProfileSettingsActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                regImage.setImageURI(imageUri);
                Utils functions = new Utils();
                thumb_byte = functions.CompressImage(imageUri , this);

                imageFilePath = mStorage.child("users").child(mUserID).child("Profile").child("profile_images").child(mUserID+".jpg");
                thumbFilePath = mStorage.child("users").child(mUserID).child("Profile").child("thumb_images").child(mUserID+".jpg");

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.w("Registration  :  " , error);
            }
        }
    }


    public void radioCLick(View view) {

        int radioId = roadDirectionGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();
    }


}
