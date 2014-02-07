package com.tkmcnally.quizter.models.now.profile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 1/10/14.
 */
public class Profile implements Parcelable {

    private String name;
    private String dateCreated;
    private String id;
    private String score;
    private String photo_url;

    public Profile(){

    }
    // Parcelling part
    public Profile(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.name = data[0];
        this.dateCreated = data[1];
        this.id = data[2];
        this.score = data[3];
        this.photo_url = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.name, this.dateCreated, this.id, this.score, this.photo_url});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };


    public String getName() {
        return name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

}
