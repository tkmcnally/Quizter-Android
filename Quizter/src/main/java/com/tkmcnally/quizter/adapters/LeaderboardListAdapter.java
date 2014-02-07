package com.tkmcnally.quizter.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tkmcnally.quizter.models.quizter.ScoreData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 1/12/14.
 */
public class LeaderboardListAdapter extends BaseAdapter {

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    Activity context;
    ArrayList<ScoreData> items;

    int layoutId;

    int rankId;
    int imageId;
    int nameId;
    int scoreId;

   // private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public LeaderboardListAdapter(Activity context, int layoutId, int rankId, int imageId, int nameId, int scoreId, ArrayList<ScoreData> items) {

        this.context = context;
        this.items = items;

        this.layoutId = layoutId;

        this.rankId = rankId;
        this.imageId = imageId;
        this.nameId = nameId;
        this.scoreId = scoreId;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        if(view == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder();

            holder.nameView = (TextView)view.findViewById(nameId);
            holder.rankView = (TextView)view.findViewById(rankId);
            holder.scoreVeiw = (TextView)view.findViewById(scoreId);
            holder.userPic = (ImageView) view.findViewById(imageId);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ScoreData scoreData = items.get(pos);
        holder.nameView.setText(scoreData.getName());
        holder.rankView.setText(scoreData.getRank());
        holder.scoreVeiw.setText(scoreData.getScore());

        imageLoader.displayImage(scoreData.getPhoto_url(), holder.userPic, options, animateFirstListener);

        return(view);
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setOptions(DisplayImageOptions options) {
        this.options = options;
    }

    class ViewHolder {
        TextView rankView ;
        TextView nameView;
        TextView scoreVeiw;
        ImageView userPic;
    }
}