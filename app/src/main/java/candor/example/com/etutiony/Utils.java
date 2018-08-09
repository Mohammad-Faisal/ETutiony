package candor.example.com.etutiony;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ProgressBar;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class Utils {


    public  static void BringImagePicker(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(activity);
    }

    public  final byte[] CompressImage(Uri imagetUri , Activity context){
        Bitmap thumb_bitmap = null;
        File thumb_file = new File(imagetUri.getPath());
        try {
            thumb_bitmap = new Compressor(context)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(30)
                    .compressToBitmap(thumb_file);
        }catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assert thumb_bitmap != null;
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        return baos.toByteArray();
    }

    public void showProgressBar(Context context , ProgressDialog mProgress){
        mProgress = new ProgressDialog(context);
        mProgress.setTitle("Saving Data.......");
        mProgress.setMessage("please wait while we create your account");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
    }




}
