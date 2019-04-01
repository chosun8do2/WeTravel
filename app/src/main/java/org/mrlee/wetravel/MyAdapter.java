package org.mrlee.wetravel;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Lee on 2019-01-29.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<MyData> mDataset;
    private StorageReference storageRef;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mDate;
        public TextView mTitle;
        public TextView mName;
        public TextView mCountry;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image);
            mDate = (TextView)view.findViewById(R.id.date);
            mTitle = (TextView)view.findViewById(R.id.title);
            mName = (TextView)view.findViewById(R.id.myprofile_car);
            mCountry = (TextView)view.findViewById(R.id.country);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            System.out.println(getPosition());
            mAuth = FirebaseAuth.getInstance();
            String currentUser = mAuth.getCurrentUser().getEmail();
            Intent intent;
            if(mDataset.get(getPosition()).user.equals(currentUser)) intent = new Intent(v.getContext() , edit_travel.class);
            else intent = new Intent(v.getContext() , view_travel.class);

            intent.putExtra("title", mDataset.get(getPosition()).text);
            intent.putExtra("content", mDataset.get(getPosition()).content);
            intent.putExtra("image", mDataset.get(getPosition()).img);
            intent.putExtra("startday", mDataset.get(getPosition()).startday);
            intent.putExtra("endday", mDataset.get(getPosition()).endday);
            intent.putExtra("country", mDataset.get(getPosition()).country);
            intent.putExtra("name", mDataset.get(getPosition()).name);
            intent.putExtra("key", mDataset.get(getPosition()).key);

            v.getContext().startActivity(intent);
        }

    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<MyData> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mDate.setText(mDataset.get(position).startday + " ~ " + mDataset.get(position).endday);
        holder.mTitle.setText(mDataset.get(position).text);
        holder.mName.setText(mDataset.get(position).name);
        holder.mCountry.setText(mDataset.get(position).country);
        String filename = "board/"+ mDataset.get(position).img;
        storageRef = FirebaseStorage.getInstance().getReference(filename);
        Glide.with(holder.mImageView.getContext()).load(storageRef).into(holder.mImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

class MyData{
    public boolean isEdit;
    public String text;
    public String img;
    public String content;
    public String startday;
    public String endday;
    public String country;
    public String name;
    public String user;
    public String key;
    public MyData() {

    }
    public MyData(String text, String img, String content, String startday, String endday, String country, String name, String user, String key){
        this.text = text;
        this.img = img;
        this.content = content;
        this.startday = startday;
        this.endday = endday;
        this.country = country;
        this.name = name;
        this.user = user;
        this.key = key;
    }
}