package es.npatarino.android.gotchallenge.models;

/**
 * Created by Nicolás Patarino on 21/02/16.
 */
public class GoTHouse{
    private String mUrlImage;
    private String mName;
    private String mId;

    public GoTHouse(String id, String name, String url){
        this.mId = id;
        this.mName = name;
        this.mUrlImage = url;
    }

    public String getUrlImage() {
        return mUrlImage;
    }

    public void setUrlImage(String houseImageUrl) {
        this.mUrlImage = houseImageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String houseName) {
        this.mName = houseName;
    }

    public String getId() {
        return mId;
    }

    @Override
    public boolean equals(Object house) {
        return ((GoTHouse)house).getId().equals(this.mId);
    }

    @Override
    public int hashCode() {
        return this.mId.hashCode();
    }

    public void save(GoTDataBase db){
        db.insertHouse(this.mId, this.mName, this.mUrlImage);
    }
}
