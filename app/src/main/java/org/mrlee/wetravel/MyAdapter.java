package org.mrlee.wetravel;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Lee on 2019-01-29.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<MyData> mDataset;
    private StorageReference storageRef;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image);
            mTextView = (TextView)view.findViewById(R.id.textview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            System.out.println(getPosition());
            Intent intent;
            if(mDataset.get(getPosition()).isEdit) intent = new Intent(v.getContext() , edit_travel.class);
            else intent = new Intent(v.getContext() , view_travel.class);

            intent.putExtra("title", mDataset.get(getPosition()).text);
            intent.putExtra("content", mDataset.get(getPosition()).content);
            intent.putExtra("image", mDataset.get(getPosition()).img);
            intent.putExtra("startday", mDataset.get(getPosition()).startday);
            intent.putExtra("endday", mDataset.get(getPosition()).endday);

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
        holder.mTextView.setText(mDataset.get(position).text);
        String filename = "board/"+ mDataset.get(position).img;
        storageRef = FirebaseStorage.getInstance().getReference(filename);
        Glide.with(holder.mImageView.getContext()).load(storageRef).transition(DrawableTransitionOptions.withCrossFade()).into(holder.mImageView);
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
    public MyData(String text, String img, String content, String startday, String endday, boolean isEdit){
        this.text = text;
        this.img = img;
        this.content = content;
        this.startday = startday;
        this.endday = endday;
        this.isEdit = isEdit;
    }
}