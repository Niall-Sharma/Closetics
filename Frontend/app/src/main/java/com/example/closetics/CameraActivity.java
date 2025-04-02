package com.example.closetics;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.media.ImageReader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private int cameraDevicesNumber;
    private String[] cameraIds;
    /*
    Typical default indexes for the front and rear cameras
     */






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds= cameraManager.getCameraIdList();
            cameraDevicesNumber = cameraIds.length;
            for (int i =0; i < cameraIds.length;i++){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIds[i]);
                //Grab the lens facing attribute of the camera device
                int lensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                //Open the camera when reaching the front facing one, do not continue loop once found
                if (lensFacing == CameraCharacteristics.AUTOMOTIVE_LENS_FACING_EXTERIOR_FRONT) {
                    openCamera(cameraIds[i], cameraManager);
                    break;
                }
            }


        } catch (CameraAccessException e) {
            Log.e("Camera Exception", e.toString());
        }
        //Create the capture session
    }

    private void openCamera(String cameraId, CameraManager cameraManager){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            //Request permission if permission hasn't been granted
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 100);
            return;
        }
        try {
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {

                    camera.createCaptureSession();



                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    camera.close();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {

                }
            }, null);
        } catch (CameraAccessException e) {
            Log.e("Camera Access Exception", e.toString());
        }
    }
    private void createCaptureSession(CameraDevice cameraDevice){
        /*
        Note:
        Surfaces are Output configurations for the camera
        Preview Surface used for rendering to the screen
        Image Reader used for saving images
         */
        //Note add a preview configuration with a texture view to display the picture in the UI

        ImageReader imageReader= ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 3);

        OutputConfiguration imageConfiguration = new OutputConfiguration(imageReader.getSurface());
        ArrayList<OutputConfiguration> surfaces = new ArrayList<OutputConfiguration>();
        surfaces.add(imageConfiguration);
        //Creates a new thread for the camera
        Executor executor = Executors.newSingleThreadExecutor();

        CameraCaptureSession.StateCallback sessionCallback = new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {

            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {

            }
        }



            //SessionConfiguration sessionConfiguration = new SessionConfiguration(SessionConfiguration.SESSION_REGULAR,
               // surfaces, executor, );

    }
}
