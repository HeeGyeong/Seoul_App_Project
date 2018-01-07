package com.example.heegyeong.accessibilityservice;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Heegyeong on 2017-03-13.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<MyData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView;
        public TextView mPathView;
        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image);

            mTextView = (TextView)view.findViewById(R.id.textview);

            mPathView = (TextView)view.findViewById(R.id.textView1);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(ArrayList<MyData> myDataset) {
        mDataset = myDataset;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_custom, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).titleStr);
        holder.mImageView.setImageResource(mDataset.get(position).iconDrawable);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

class MyData{
    public String titleStr;
    public String decsStr;
    public int iconDrawable;

    public MyData(String titleStr,String decsStr, int iconDrawable){
        this.titleStr = titleStr;
        this.decsStr = decsStr;
        this.iconDrawable = iconDrawable;
    }

    public void setIcon(int icon) {
        iconDrawable = icon ;
    }
    public int getIcon() {
        return this.iconDrawable ;
    }

    public void setTitle(String title) {
        titleStr = title ;
    }
    public String getTitle() {
        return this.titleStr ;
    }

    public void setPack(String decs) {
        decsStr = decs ;
    }
    public String getPack() {
        return this.decsStr ;
    }
}
