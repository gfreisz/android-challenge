package es.npatarino.android.gotchallenge.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.npatarino.android.gotchallenge.interfaces.IManagerEvents;
import es.npatarino.android.gotchallenge.interfaces.IWebServicesEvents;
import es.npatarino.android.gotchallenge.services.JsonObject;
import es.npatarino.android.gotchallenge.services.WebServices;
import es.npatarino.android.gotchallenge.utils.Utils;

public class GoTManager {
    private Context mContext;

    private HashMap<GoTHouse, ArrayList<GoTCharacter>> mHouses;
    private GoTDataBase mDatabase;
    private boolean mIsComplete = false;

    private static GoTManager mInstance;
    private GoTManager(){}
    private GoTManager(Context context)
    {
        mContext = context;
        mDatabase = new GoTDataBase(context);
    }

    public static GoTManager getInstance(Context context){
        if (mInstance == null)
            mInstance = new GoTManager(context);

        return mInstance;
    }

    public void refreshData(final IManagerEvents callbacks){
        if (Utils.isNetworkAvailable(mContext)){
            WebServices.getInstace().getDataAsync(new IWebServicesEvents() {
                @Override
                public void onGetDataFinishCallback(List<JsonObject> list) {
                    mHouses = new HashMap<>();
                    for (JsonObject obj: list) {
                        GoTHouse house = new GoTHouse(obj.HouseId, obj.HouseName, obj.UrlHouseImage);
                        ArrayList<GoTCharacter> charactersList = null;

                        boolean existHouse= false;
                        for (Map.Entry<GoTHouse, ArrayList<GoTCharacter>> entry: mHouses.entrySet()) {
                            if(entry.getKey().getId().equals(house.getId())){
                                existHouse = true;
                                charactersList = entry.getValue();
                                break;
                            }
                        }

                        if (!existHouse) {
                            charactersList = new ArrayList<>();
                            mHouses.put(house, charactersList);
                        }

                        GoTCharacter character = new GoTCharacter(obj.CharacterName, obj.CharacterDescription, obj.UrlCharacterImage);
                        charactersList.add(character);

                        house.save(mDatabase);
                        character.save(mDatabase, house.getId());
                    }

                    mIsComplete = true;

                    callbacks.onRecoveryDataFinishCallback();
                }

                @Override
                public void onGetDataErrorCallback(String msg) {
                    callbacks.onRecoveryDataErrorCallback();
                }
            });
        } else{
            callbacks.onNetworkErrorCallback();
            callbacks.onRecoveryDataFinishCallback();
        }
    }

    public ArrayList<GoTHouse> getAllHouses(){
        ArrayList<GoTHouse> houses;
        if (mIsComplete) {
            houses = new ArrayList<>(mHouses.keySet());
        } else{
            houses = mDatabase.getAllHouses();
        }

        return houses;
    }

    public ArrayList<GoTCharacter> getAllCharacters(){
        if (!mIsComplete){
            mHouses = mDatabase.getAllCharacters();
        }

        ArrayList<GoTCharacter> characters = new ArrayList<>();

        for (Map.Entry<GoTHouse, ArrayList<GoTCharacter>> entry : mHouses.entrySet()) {
            characters.addAll(entry.getValue());
        }

        mIsComplete = true;

        return characters;
    }

    public ArrayList<GoTCharacter> getAllCharactersFromHouse(GoTHouse house){
        ArrayList<GoTCharacter> characters = null;
        if (mIsComplete) {
            for (Map.Entry<GoTHouse, ArrayList<GoTCharacter>> entry : mHouses.entrySet()) {
                if (entry.getKey().getId().equals(house.getId())) {
                    characters = entry.getValue();
                    break;
                }
            }
        } else {
            characters = mDatabase.getCharactersFromHouse(house.getId());
        }
        return characters;
    }

    public ArrayList<GoTCharacter> getCharactersBySearchValue(String search){
        return mDatabase.getCharactersBySearchValue(search);
    }
}
