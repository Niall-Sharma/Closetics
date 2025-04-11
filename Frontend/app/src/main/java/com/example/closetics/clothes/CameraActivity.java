package com.example.closetics.clothes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.closetics.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    private int correctFacingCamera = CameraCharacteristics.AUTOMOTIVE_LENS_FACING_EXTERIOR_FRONT;

    //private int correctFacingCamera = CameraCharacteristics.LENS_FACING_FRONT;

    private int cameraDevicesNumber;

    private ImageReader imageReader;

    private TextureView textureView;

    private String[] cameraIds;


    private Button snapPicture;

    private Uri imageUri;
    private ImageView imageView;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    imageView.setImageURI(imageUri);
                } else {
                    // Cleanup if the photo wasn't actually taken
                    if (imageUri != null) {
                        getContentResolver().delete(imageUri, null, null);
                        imageUri = null;
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.capturedImage);
        Button cameraButton = findViewById(R.id.cameraButton);

        cameraButton.setOnClickListener(v -> openCamera());
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraLauncher.launch(intent);
    }



}


        /*
        setContentView(R.layout.activity_camera);


        textureView = findViewById(R.id.textureView);
        snapPicture = findViewById(R.id.snap);

        CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraIds= cameraManager.getCameraIdList();
            for (int i =0; i < cameraIds.length;i++){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIds[i]);
                //Grab the lens facing attribute of the camera device
                int lensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                //Open the camera when reaching the front facing one, do not continue loop once found
                if (lensFacing == correctFacingCamera) {
                    cameraDevicesNumber = i;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            Log.e("Camera Exception", e.toString());
        }


        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                openCamera(cameraIds[cameraDevicesNumber], cameraManager);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }
        });
        snapPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                cameraDevice.close();

            }
        });

         */



/*
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        try {
                            cameraDevice = camera;
                            createCaptureSession(camera);
                        } catch (CameraAccessException e) {
                            Log.e("On camera opened", e.toString());
                        }
                    }


                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    camera.close();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    camera.close();
                    camera=null;

                }
            }, null);
        } catch (CameraAccessException e) {
            Log.e("Camera Access Exception", e.toString());
        }
    }

 */


/*



    @RequiresApi(api = Build.VERSION_CODES.P)
    private void createCaptureSession(CameraDevice cameraDevice) throws CameraAccessException {

        Note:
        Surfaces are Output configurations for the camera
        Preview Surface used for rendering to the screen
        Image Reader used for saving images

        //Note add a preview configuration with a texture view to display the picture in the UI


        imageReader= ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 3);

        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        Surface previewSurface = new Surface(surfaceTexture);

        CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest((CameraDevice.TEMPLATE_PREVIEW));
        captureRequestBuilder.addTarget(previewSurface);
        captureRequestBuilder.addTarget(imageReader.getSurface());


        OutputConfiguration imageConfiguration = new OutputConfiguration(imageReader.getSurface());
        OutputConfiguration textureViewConfiguration = new OutputConfiguration(previewSurface);
        ArrayList<OutputConfiguration> surfaces = new ArrayList<OutputConfiguration>();
        surfaces.add(imageConfiguration);
        surfaces.add(textureViewConfiguration);

        //Creates a new thread for the camera
        Executor executor = Executors.newSingleThreadExecutor();

        CameraCaptureSession.StateCallback sessionCallback = new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                Log.d("Camera Configuration", "Success");
                try {
                    cameraCaptureSession = session;
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                    CaptureRequest captureRequest = captureRequestBuilder.build();
                    session.setRepeatingRequest(captureRequest, null, null);

                }catch (CameraAccessException e) {
                    Log.e("Error", e.toString());
                }



            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                Log.e("Camera Configuration", "Fail");

            }
        };



            SessionConfiguration sessionConfiguration = new SessionConfiguration(SessionConfiguration.SESSION_REGULAR,surfaces, executor, sessionCallback);
            cameraDevice.createCaptureSession(sessionConfiguration);
    }

    private void takePicture() {
        try {
            // Create CaptureRequest for still image capture
            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(imageReader.getSurface());  // Target the ImageReader surface

            // capture settings
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);

            // Capture the image
            cameraCaptureSession.capture(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    // Image has been captured, process or save it
                    Image image = imageReader.acquireNextImage();
                    // Process the image (e.g., save it to a file)
                    saveImage(image);
                    checkFile();
                    image.close();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void saveImage(Image image) {
        if (image == null) {
            return; // No image is available
        }

        // Get the JPEG image data directly from the ImageReader
        ByteBuffer buffer = image.getPlanes()[0].getBuffer(); // Get the buffer of the first plane (JPEG)
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes); // Read the bytes from the buffer

        // Save the image using MediaStore on Android 10 and above (Scoped Storage)
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "test2.jpg");  // Image file name
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Closetics");  // Save to Pictures/Closetics

        // Insert the image into MediaStore
        Uri imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        try {
            // Get an output stream to write the image
            OutputStream outputStream = this.getContentResolver().openOutputStream(imageUri);
            // Directly write the image bytes to the output stream (no Bitmap decoding)
            if (outputStream != null) {
                outputStream.write(bytes);
                outputStream.close();
            }
        } catch (IOException e) {
            Log.e("Exception", e.toString());
        } finally {
            image.close(); // Don't forget to close the image
        }
    }


    private void checkFile(){

        File imageFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "test.jpg");

        if (imageFile.exists()) {
            // Image saved successfully
            Log.d("Image Save", "Image exists and is saved properly.");
        } else {
            // Image saving failed
            Log.d("Image Save", "Image not found. There was an issue saving the image.");
        }

    }

 */

