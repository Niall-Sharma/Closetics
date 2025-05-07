package com.example.closetics.dashboard;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.clothes.ClothesByTypeAdapter;
import com.example.closetics.clothes.ClothesCreationBaseFragment;
import com.example.closetics.clothes.ClothesManager;
import com.example.closetics.clothes.TypeGridRecyclerViewAdapter;
import com.example.closetics.outfits.OutfitManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.MyViewHolder>{
    /*
    Baseline clothes stats adapter
     */
    ArrayList<ClothingStatItem> objects;
    int which;
    Context context;


    public StatisticsRecyclerViewAdapter(ArrayList<ClothingStatItem> objects, int type, Context context){
        this.objects = objects;
         which= type;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_recycler_view_item, parent, false);
        StatisticsRecyclerViewAdapter.MyViewHolder myViewHolder = new StatisticsRecyclerViewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int realPosition = holder.getBindingAdapterPosition();

        ClothingStatItem item = objects.get(realPosition);


        if (which == 0){
            holder.image.setVisibility(View.GONE);
            getClothingItems(context, item.getOutfitId(), holder);

            try {
                holder.timesWorn.setText(item.getTimesWorn());
                holder.highTemp.setText(item.getAvgHighTemp());
                holder.lowTemp.setText(item.getAvgLowTemp());
                holder.name.setText(item.getName());
            } catch (JSONException e) {
                Log.e("exception", e.toString());
            }

        }
        else if (which ==1){
            try {
                //Set the text view in the recycler view
                holder.image.setVisibility(View.VISIBLE);
                if (item.getImage() != null){
                    Bitmap bitmap = ClothesByTypeAdapter.resizeWithAspectRatio(item.getImage(), 157, 147);
                    holder.image.setImageBitmap(bitmap);
                }
                else{

                }

                String s = "Number Of Outfits In: ";
                holder.timesWorn.setText(item.getTimesWorn());
                holder.outfitsIn.setText(item.getNumberOfOutfitsIn());
                holder.highTemp.setText(item.getAvgHighTemp());
                holder.lowTemp.setText(item.getAvgLowTemp());
                holder.prompt.setText(s);
                holder.name.setText(item.getName());
            } catch (JSONException e) {
                Log.e("exception", e.toString());
            }

        }
        else{
            try {
                holder.image.setVisibility(View.GONE);
                String s = "Number Of Outfits In: ";
                holder.timesWorn.setText(item.getTimesWorn());
                holder.outfitsIn.setText(item.getNumberOfOutfitsIn());
                holder.highTemp.setText(item.getAvgHighTemp());
                holder.lowTemp.setText(item.getAvgLowTemp());
                holder.prompt.setText(s);
                holder.name.setText(item.getName());
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }

        }


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private void getClothingItems(Context context, Long outfitId, MyViewHolder holder){
        OutfitManager.getAllOutfitItemsRequest(context, outfitId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                holder.images = new ArrayList<>();
                for (int i =0; i < response.length(); i++){
                    try {
                        JSONObject clothingItem = response.getJSONObject(i);
                        Long clothesId = clothingItem.getLong("clothesId");
                        getImage(context, clothesId, response.length(), holder.images, holder);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Image Error", error.toString());
            }
        });
    }

    private void getImage(Context context, Long clothesId, int size, ArrayList<byte[]> images, MyViewHolder holder){
        ClothesManager.getImageByClothing(context, clothesId, MainActivity.SERVER_URL, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                images.add(response);
                if (images.size() == size){
                    ImagePagerAdapter adapter = new ImagePagerAdapter(context, images);
                    holder.viewPager.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Assume there is no image, add null
                images.add(null);


            }
        });
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView timesWorn;
        private TextView lowTemp;
        private TextView highTemp;
        private TextView outfitsIn;
        private TextView name;
        private TextView prompt;
        private ImageView image;
        private ViewPager2 viewPager;
        private ArrayList<byte[]> images;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timesWorn = itemView.findViewById(R.id.specialType);
            lowTemp = itemView.findViewById(R.id.low_temp);
            highTemp = itemView.findViewById(R.id.high_temp);
            outfitsIn = itemView.findViewById(R.id.outfits_in);
            name = itemView.findViewById(R.id.name);
            prompt = itemView.findViewById(R.id.numberOutfitsPrompt);
            image = itemView.findViewById(R.id.imageView);
            viewPager = itemView.findViewById(R.id.viewPager);

        }
    }
}
