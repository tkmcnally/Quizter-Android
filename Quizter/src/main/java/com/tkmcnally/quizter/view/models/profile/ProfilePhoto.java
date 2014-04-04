package com.tkmcnally.quizter.view.models.profile;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 1/10/14.
 */
public class ProfilePhoto implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ProfilePhoto createFromParcel(Parcel in) {
            return new ProfilePhoto(in);
        }

        public ProfilePhoto[] newArray(int size) {
            return new ProfilePhoto[size];
        }
    };
    private Bitmap photo;

    public ProfilePhoto() {

    }

    public ProfilePhoto(Parcel in) {
        this.photo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public Bitmap getBitmap() {
        return photo;
    }

    public void setBitmap(Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.photo);
    }
}
