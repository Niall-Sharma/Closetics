package com.example.closetics.clothes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.closetics.MainActivity;

import java.io.Serializable;

public class EditClothesFragment extends ClothesCreationBaseFragment {

    private static ClothingItem clothingItem;



    public EditClothesFragment() {
        // Required empty public constructor
    }

    public static ClothesCreationBaseFragment newInstance(int position) {
        EditClothesFragment fragment = new EditClothesFragment();
        Bundle args = new Bundle();
        args.putInt("count", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        int pos = getPosition();
        String field = getClothingItemField(pos);

        if (pos > 1 && pos < 9) {
            if (!field.equals("null")) {
                getInputField().setText(field);
            }
        } else if (pos == 0) {
            if (clothingItem.getImage() != null) {
                setImageView();
            }
        } else if (pos == 1) {
            Spinner spinner = getSpinner();
            String bool = clothingItem.getFavorite();
            spinner.setSelection("false".equals(bool) ? 1 : 0);
        } else if (pos == 9) {
            Integer typeI = Math.toIntExact(clothingItem.getType());
            ArrayAdapter<String> spinnerAdapter = getSpinnerAdapter();
            Spinner spinner = getSpinner();
            String type = MainActivity.CLOTHING_TYPES.get(typeI);
            type = type.substring(0, 1).toUpperCase() + type.substring(1);
            int spinnerPosition = spinnerAdapter.getPosition(type);
            spinner.setSelection(spinnerPosition);
        } else {
            Integer specialTypeI = Math.toIntExact(clothingItem.getSpecialType());
            ArrayAdapter<String> spinnerAdapter = getSpinnerAdapter();
            Spinner spinner = getSpinner();
            String specialType = MainActivity.CLOTHING_SPECIAL_TYPES.get(specialTypeI);
            int spinnerPosition = spinnerAdapter.getPosition(specialType);

            spinner.setSelection(spinnerPosition);
        }
    }

    private void setImageView() {
        Bitmap bitmap = ClothesByTypeAdapter.resizeWithAspectRatio(clothingItem.getImage(), 150, 150);
        getImageView().setImageBitmap(bitmap);
    }

    public static void setClothingItem(ClothingItem clothingItem) {
        EditClothesFragment.clothingItem = clothingItem;
    }

    private String getClothingItemField(int position) {
        switch (position) {
            case 2: return clothingItem.getSize();
            case 3: return clothingItem.getColor();
            case 4: return clothingItem.getDateBought();
            case 5: return clothingItem.getPrice();
            case 6: return clothingItem.getItemName();
            case 7: return clothingItem.getBrand();
            case 8: return clothingItem.getMaterial();
            default: return null;
        }
    }
}
