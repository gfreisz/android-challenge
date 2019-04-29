package es.npatarino.android.gotchallenge.services;

import com.google.gson.annotations.SerializedName;

public class JsonObject
{
    @SerializedName("name")
    public String CharacterName;

    @SerializedName("imageUrl")
    public String UrlCharacterImage;

    @SerializedName("description")
    public String CharacterDescription;

    @SerializedName("houseImageUrl")
    public String UrlHouseImage;

    @SerializedName("houseName")
    public String HouseName;

    @SerializedName("houseId")
    public String HouseId;
}
