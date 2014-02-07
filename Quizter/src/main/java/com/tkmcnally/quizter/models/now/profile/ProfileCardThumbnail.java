package com.tkmcnally.quizter.models.now.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by Thomas on 1/20/14.
 */
public class ProfileCardThumbnail extends CardThumbnail {

    private Bitmap bitmap;

    public ProfileCardThumbnail(Context context) {
        super(context);
    }
    @Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {

        ImageView image = (ImageView)viewImage ;
        image.setImageBitmap(bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
