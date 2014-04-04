package com.tkmcnally.quizter.view.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.drawable.CircleDrawable;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * This class provides a simple card as Google Now Birthday
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowProfileCard extends Card {

    public int USE_VIGNETTE = 0;
    public String PHOTO_URL;
    public GoogleNowProfileHeader header;
    String name;
    String dateCreated;
    String score;
    String prefix_dateCreated = "Member Since: ";
    String prefix_Score = "Score: ";

    public GoogleNowProfileCard(Context context) {
        super(context, R.layout.profile_card_inner_main);
        header = new GoogleNowProfileHeader(getContext(), R.layout.profile_card_inner_header);
    }

    public GoogleNowProfileCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    public void init() {

        //Add Header
        header.setButtonExpandVisible(true);
        addCardHeader(header);

        //Add Expand Area
        GoogleNowExpandCard expand = new GoogleNowExpandCard(getContext());
        expand.setDateCreated(prefix_dateCreated + dateCreated);
        addCardExpand(expand);


        //Add Thumbnail
        GoogleNowProfilePicture thumbnail = new GoogleNowProfilePicture(getContext());
        float density = getContext().getResources().getDisplayMetrics().density;
        int size = (int) (125 * density);
        thumbnail.setUrlResource(PHOTO_URL);
        thumbnail.setErrorResource(R.drawable.ic_error);
        addCardThumbnail(thumbnail);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView title = (TextView) view.findViewById(R.id.card_main_inner_simple_title);
        title.setText("Welcome to Quizter!");
        title.setTextColor(mContext.getResources().getColor(R.color.quizter_theme_color));
        title.setGravity(Gravity.CENTER_VERTICAL);

        ImageView thumbnail_image = (ImageView) getCardView().findViewById(R.id.card_thumbnail_image);
        float density = getContext().getResources().getDisplayMetrics().density;
        double size = 150 * density;
        if (size > 300) {
            size = 300;
        }

        thumbnail_image.getLayoutParams().width = (int) size;
        thumbnail_image.getLayoutParams().height = (int) size;

    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String splitName(String name) {
        return name.replaceFirst(" ", "\n");
    }

    public class GoogleNowProfilePicture extends CardThumbnail {

        public Bitmap bitmap;

        public GoogleNowProfilePicture(Context context) {
            super(context);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {
            /*
            viewImage.getLayoutParams().width = 250;
            viewImage.getLayoutParams().height = 250;
            */
        }

        @Override
        public boolean applyBitmap(View imageView, Bitmap bitmap) {
            this.bitmap = bitmap;


            switch (USE_VIGNETTE) {
                case 0:
                    return false;
                case 1:
                    CircleDrawable circle = new CircleDrawable(bitmap);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        imageView.setBackground(circle);
                    else
                        imageView.setBackgroundDrawable(circle);
                    return true;
                default:
                    return false;
            }
        }
    }

    class GoogleNowProfileHeader extends CardHeader {


        public GoogleNowProfileHeader(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            TextView txName = (TextView) view.findViewById(R.id.profile_card_name);
            //  TextView txSubName = (TextView) view.findViewById(R.id.profile_card_date_created);
            TextView txScore = (TextView) view.findViewById(R.id.profile_card_score);

            txName.setText(splitName(name));
            //txSubName.setText(dateCreated);
            txScore.setText(prefix_Score + score);

            ImageView scoreStar = (ImageView) view.findViewById(R.id.profile_card_score_star);
            float density = getContext().getResources().getDisplayMetrics().density;
            double size = 64 * density;
            if (size > 128) {
                size = 128;
            }
            // scoreStar.getLayoutParams().width = (int) size;
            // scoreStar.getLayoutParams().height = (int) size;
            scoreStar.setImageResource(R.drawable.star);
        }
    }

}