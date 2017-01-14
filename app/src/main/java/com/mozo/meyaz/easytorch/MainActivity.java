package com.mozo.meyaz.easytorch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private ImageButton dugme;
    private boolean acikmi;
    private MediaPlayer mediaPlayer;
 //   private Activity aktivite;

 //   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FlashLightActivity", "onCreate()");
        setContentView(R.layout.activity_main);
        dugme = (ImageButton) findViewById(R.id.dugme);

        acikmi = false;
/*We need to detect whether the device has a Flash Light or not. In case the device doesn’t have support for flashlight we have to alert the user through an alert message.*/
        Boolean flashVarmi = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!flashVarmi) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
              //      finish();
             //       System.exit(0);
                    Toast.makeText(getApplicationContext(),"Cihazında  kamera yok",Toast.LENGTH_LONG);
                }
            });
            alert.show();
            return;
        }
//Next, we add the add the code to the onCreate() method to get the CameraManager object. Then we set the OnClickListener() for the on/off button for our Led Flash Light Application.
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        dugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (acikmi) {
                        isigiKapa();
                        acikmi = false;
                    } else {
                        isigiAc();
                        acikmi = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //Next we add the turnOffFlashLight() and turnOnFlashLight() methods for turning the Flash Off and On respectively, We will also add a method playOnOffSound to give the sound effect of clicking a button.

    public void isigiAc() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
                playOnOffSound();
                dugme.setImageResource(R.drawable.on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void isigiKapa() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
                playOnOffSound();
                dugme.setImageResource(R.drawable.off);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playOnOffSound() {

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.ses);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mediaPlayer.start();
    }

    //At last override the Activity Lifecycle method’s by adding the following code. When the App is minimized by user, we will turnOff the Flash and as soon as the user returns to the App, the Flash Light will resume if it was on earlier.

    @Override
    protected void onStop() {
        super.onStop();
        if (acikmi) {
            isigiKapa();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (acikmi) {
            isigiKapa();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (acikmi) {
            isigiAc();
        }
    }
}


// http://www.androidtutorialpoint.com/basics/learn-by-doing/flash-light-application/ adresinden faydalandım.






