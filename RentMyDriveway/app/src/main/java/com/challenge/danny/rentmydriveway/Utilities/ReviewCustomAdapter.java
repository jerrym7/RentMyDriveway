package com.challenge.danny.rentmydriveway.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.challenge.danny.rentmydriveway.R;
import com.challenge.danny.rentmydriveway.UserInformation.UserRatingDataModel;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewCustomAdapter extends ArrayAdapter implements View.OnClickListener {
    private ArrayList<UserRatingDataModel> dataSet;
    Context mContext;
    private static class ViewHolder{
        TextView userNameTextView; //user full name
        TextView commentTextView; //comments from user
        RatingBar userRatingBar; //rating from user
        CircleImageView userProfilePicture; //user's profile picture
    }
    public ReviewCustomAdapter(ArrayList<UserRatingDataModel> data, Context context){
        super(context,R.layout.layout_review_row,data);
        this.dataSet = data;
        this.mContext = context;
    }

    //if current user clicks the user's comments or their stuff
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        UserRatingDataModel dataModel = (UserRatingDataModel) object;
        switch(v.getId()){
        }
    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //get the data item for this position
        UserRatingDataModel dataModel = (UserRatingDataModel)getItem(position);
        //check if an existing view is being reused
        ViewHolder viewHolder;
        final View result;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_review_row,parent
                    ,false);
            viewHolder.userNameTextView = convertView.findViewById(R.id.username_textView);
            viewHolder.commentTextView = convertView.findViewById(R.id.comment_textView);
            viewHolder.userRatingBar = convertView.findViewById(R.id.user_ratingBar);
            viewHolder.userProfilePicture = convertView.findViewById(R.id.profile_imageView);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext,(position>lastPosition)? R.anim.up_from_bottom: R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.userNameTextView.setText(dataModel.getUsername());
        viewHolder.commentTextView.setText(dataModel.getComment());
        viewHolder.userRatingBar.setNumStars((int)dataModel.getRating());
        viewHolder.userRatingBar.setMax(5);
        return convertView;
    }
}
