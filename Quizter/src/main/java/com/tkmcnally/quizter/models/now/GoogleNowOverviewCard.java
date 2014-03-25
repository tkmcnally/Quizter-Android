package com.tkmcnally.quizter.models.now;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.activities.NavDrawerActivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple card as Google Play
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowOverviewCard extends Card {

    public boolean profileCard;

    protected TextView mLabel;
    protected ImageView mPhoto;


    protected String mark, nameString, scoreString;


    public Bitmap bitmap;
    public int resourceIDImage;

    private String photoUrl;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public GoogleNowOverviewCard(Context context) {
        this(context, R.layout.cards_list_overview_label);
    }

    public GoogleNowOverviewCard(Context context, int innerLayout) {
        super(context, innerLayout);

    }

    public void init() {

    }



    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView name = (TextView) view.findViewById(R.id.quiz_overview_name);
        name.setText(nameString);

        TextView score = (TextView) view.findViewById(R.id.quiz_overview_score);
        score.setText(scoreString);

        TextView markView = (TextView) view.findViewById(R.id.quiz_over_mark);
        markView.setText(mark);

        ImageView thumbnail_image = (ImageView) view.findViewById(resourceIDImage);
        float density = getContext().getResources().getDisplayMetrics().density;
        double size = 150 * density;
        if(size > 300) {
            size = 300;
        }

        thumbnail_image.getLayoutParams().width = (int) size;
        thumbnail_image.getLayoutParams().height = (int) size;

        imageLoader.displayImage(photoUrl, thumbnail_image ,((NavDrawerActivity)getContext()).getOptions(), animateFirstListener);

    }

    public int getResourceIDImage() {
        return resourceIDImage;
    }

    public void setResourceIDImage(int resourceIDImage) {
        this.resourceIDImage = resourceIDImage;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public String getScoreString() {
        return scoreString;
    }

    public void setScoreString(String scoreString) {
        this.scoreString = scoreString;
    }

    public void setIsProfileCard(boolean profileCard) {
        this.profileCard = profileCard;
    }

    public boolean isProfileCard() {
        return profileCard;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(getRoundedBitmap(loadedImage, view));

                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }

        public static Bitmap getRoundedBitmap(Bitmap bitmap, View view) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);

            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);


            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            bitmap.recycle();

            return output;
        }
    }

}