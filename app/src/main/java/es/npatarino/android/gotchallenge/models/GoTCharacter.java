package es.npatarino.android.gotchallenge.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolás Patarino on 21/02/16.
 */
public class GoTCharacter {
    private String mName;
    private String mUrlImage;
    private String mDescription;

    public GoTCharacter(){}
    public GoTCharacter(String name, String description, String url){
        this.mName = name;
        this.mDescription = description;
        this.mUrlImage = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String s) {
        this.mName = s;
    }

    public String getUrlImage() {
        return mUrlImage;
    }

    public void setUrlImage(final String s) {
        this.mUrlImage = s;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(final String s) {
        this.mDescription = s;
    }

    public void save(GoTDataBase db, String houseId){
        db.insertCharacter(this.mName, this.mDescription, this.mUrlImage, houseId);
    }
}
