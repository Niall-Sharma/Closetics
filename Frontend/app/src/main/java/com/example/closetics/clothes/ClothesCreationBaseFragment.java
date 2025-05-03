package com.example.closetics.clothes;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.closetics.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClothesCreationBaseFragment extends Fragment{
    private Button submit;
    private TextView clothesTextView;
    private EditText inputField;
    private ClothesDataViewModel clothesDataViewModel;
    private Spinner spinner;
    private Button takeImage;
    private ViewPager2 viewPager;
    private int position;
    private ImageView imageView;
    private String internalStoragePath;
    //Camera
    private int correctFacingCamera = CameraCharacteristics.AUTOMOTIVE_LENS_FACING_EXTERIOR_FRONT;
    private Uri imageUri;
    public static byte[] byteArray;

    //Launches the default camera app
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    byteArray = getImageByteArrayFromUri(imageUri);
                    clothesDataViewModel.setFragment(position, "s");
                    try {
                        imageView.setImageBitmap(resizeImage(imageUri, 150, 150));
                    } catch (IOException e) {
                        Log.d("Exception" ,e.toString());
                    }
                    imageView.setVisibility(View.VISIBLE);


                } else {
                    // Cleanup if the photo wasn't actually taken
                    if (imageUri != null) {
                        requireActivity().getContentResolver().delete(imageUri, null, null);
                        imageUri = null;

                    }
                }
            });



    public ClothesCreationBaseFragment(ClothesDataViewModel clothesDataViewModel){
        this.clothesDataViewModel = clothesDataViewModel;

    }
    /*
    Second constructor for editing viewpager
     */
    public ClothesCreationBaseFragment(ClothesDataViewModel clothesDataViewModel, String answeredQuestions ){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_clothes, container, false);
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("count");
        int index = position;
        Log.d("index", String.valueOf(index));



        submit = view.findViewById(R.id.add_button);
        clothesTextView = view.findViewById(R.id.question_text);
        inputField = view.findViewById(R.id.input_edit);
        spinner = view.findViewById(R.id.spinner2);
        takeImage = view.findViewById(R.id.imageCapture);
        clothesTextView.setText(ClothingItem.createClothesQuestions[index]);
        imageView = view.findViewById(R.id.imageView3);


        //Makes sure to set the image view if an image has already been captured!
        String imageUriString = clothesDataViewModel.getFragment(position);
        if (imageUriString != null && !imageUriString.isEmpty()) {
            imageUri = Uri.parse(imageUriString);
            try {
                imageView.setImageBitmap(resizeImage(imageUri, 150, 150));
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Log.d("Exception", e.toString());
            }
        }

        //Grab the viewpager
        viewPager = requireActivity().findViewById(R.id.edit_pager);

        //Image capture fragment
        if (index==0){
            setImageVisibility();
            takeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Request permissions
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
                    }
                    openCamera();


                }
            });
        }
        else if (index>1 && index <9){
            setEditTextVisibility();
        }
        else{
            setSpinnerVisibility();
        }

        inputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Update the arrayList when text is changed

                //If there is a change set it in the arrayList
                if (!(s.toString().equals(""))) {
                    clothesDataViewModel.setFragment(index, inputField.getText().toString().trim());
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            //Swipe on next button
            @Override
            public void onClick(View v) {
                if (viewPager!=null){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
                }
            }
        });


        return view;
    }

    public EditText getInputField() {
        return inputField;
    }
    public int getPosition(){
        return position;
    }
    private void setImageVisibility(){
        inputField.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        takeImage.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }
    private void setEditTextVisibility(){
        inputField.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        takeImage.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

    }
    private void setSpinnerVisibility(){
        inputField.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        takeImage.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

    }

    public static ClothesCreationBaseFragment newInstance(int fragmentCount, ClothesDataViewModel clothesDataViewModel) {
        //Create a new forgot password fragment
        ClothesCreationBaseFragment fragment = new ClothesCreationBaseFragment(clothesDataViewModel);
        Bundle args = new Bundle();
        args.putInt("count", fragmentCount);
        fragment.setArguments(args);
        return fragment;
    }

    private Bitmap resizeImage(Uri imageUri, int maxWidth, int maxHeight) throws IOException {
        InputStream input = requireContext().getContentResolver().openInputStream(imageUri);

        // Decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        input.close();

        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        int scaleFactor = Math.min(originalWidth / maxWidth, originalHeight / maxHeight);

        // Decode actual bitmap with scaling
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        input = requireContext().getContentResolver().openInputStream(imageUri);
        Bitmap resizedBitmap = BitmapFactory.decodeStream(input, null, options);
        input.close();
        return resizedBitmap;
    }



    private void openCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Closetics");

        imageUri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraLauncher.launch(intent);
    }


    private byte[] getImageByteArrayFromUri(Uri sourceUri) {
        try {
            // Decode bitmap from the URI
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), sourceUri);

            // Compress the bitmap to a PNG or JPEG byte array
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream); // or Bitmap.CompressFormat.PNG

            return byteStream.toByteArray();

        } catch (IOException e) {
            Log.e("ImageConversionError", e.toString());
            return null;
        }

    }





}



