package com.example.permissioncamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 144;
    private static final int REQUEST_ASK_PERMISSION =123 ;
    Button b1;
    private static final int CAMERA_REQUEST= 123;
    ImageView i;
    String PERMISSIONS[]={Manifest.permission.CAMERA};
    final private int REQUEST_CODE_ASK_PERMISSION =144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.btn);
        i=findViewById(R.id.imageView);
        if (!hasPermission(MainActivity.this,PERMISSIONS)){
            showCameraPermissionDialog();
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(i,CAMERA_REQUEST);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            i.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ) {
                //startActivty();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.CAMERA)) {
                    Toast.makeText(getApplicationContext() , "Permissions are required for this app" , Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showCameraPermissionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Cmaera Access")
                .setCancelable(true)
                .setMessage("Need Camera Access for Photo");
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,REQUEST_ASK_PERMISSION);
            }
        });
        builder.setCancelable(true);
        builder.show();
    }


    public static boolean hasPermission(Context context,String...permissions){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&& context !=null&&permissions!=null){
            for (String permission: permissions){
                if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Failed to resolve permission"+permission, Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        return true;
    }
}