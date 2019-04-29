package es.npatarino.android.gotchallenge.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoTDataBase extends SQLiteOpenHelper {
    public static final String DatabaseName = "GoTDatabase.db";
    public static final int DatabaseVersion = 1;

    private final String TableNameCharacters = "GoTCharacters";
    private final String TableColCharactersID = "char_id";
    private final String TableColCharactersDescription = "char_description";
    private final String TableColCharactersHouseId = "char_houseId";
    private final String TableColCharactersImageUrl = "char_imageUrl";
    private final String TableColCharactersName = "char_name";

    private final String TableNameHouses = "GoTHouses";
    private final String TableColHousesId = "house_id";
    private final String TableColHousesName = "house_name";
    private final String TableColHousesImageUrl = "house_imageUrl";

    public static SQLiteDatabase db;

    public GoTDataBase(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);

        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_houses_sql =
                "CREATE TABLE IF NOT EXISTS " + TableNameHouses + "(" +
                        TableColHousesId + " TEXT PRIMARY KEY NOT NULL UNIQUE, " +
                        TableColHousesName + " TEXT, " +
                        TableColHousesImageUrl + " TEXT)";

        String create_table_characters_sql =
                "CREATE TABLE IF NOT EXISTS " + TableNameCharacters + " (" +
                        TableColCharactersID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                        TableColCharactersDescription + " TEXT, " +
                        TableColCharactersHouseId + " TEXT, " +
                        TableColCharactersImageUrl + " TEXT, " +
                        TableColCharactersName + " TEXT UNIQUE)";

        db.execSQL(create_table_houses_sql);
        db.execSQL(create_table_characters_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean insertHouse(String id, String name, String imageUrl){
        ContentValues values = new ContentValues();

        values.put(TableColHousesId, id);
        values.put(TableColHousesName, name);
        values.put(TableColHousesImageUrl, imageUrl);

        long ret = db.insertWithOnConflict(TableNameHouses, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return (ret > -1);
    }

    public boolean insertCharacter(String name, String description, String imageUrl, String houseId){
        ContentValues values = new ContentValues();

        values.put(TableColCharactersName, name);
        values.put(TableColCharactersDescription, description);
        values.put(TableColCharactersImageUrl, imageUrl);
        values.put(TableColCharactersHouseId, houseId);

        long ret = db.insertWithOnConflict(TableNameCharacters, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return (ret > -1);
    }

    public ArrayList<GoTHouse> getAllHouses(){
        ArrayList<GoTHouse> houses = new ArrayList<>();

        String sql = "SELECT * FROM " + TableNameHouses;
        Cursor cHouses = db.rawQuery(sql, null);
        while(cHouses.moveToNext()){
            String id = cHouses.getString(cHouses.getColumnIndex(TableColHousesId));
            String name = cHouses.getString(cHouses.getColumnIndex(TableColHousesName));
            String imageUrl = cHouses.getString(cHouses.getColumnIndex(TableColHousesImageUrl));

            GoTHouse house = new GoTHouse(id, name, imageUrl);
            houses.add(house);
        }

        return houses;
    }

    public HashMap<GoTHouse, ArrayList<GoTCharacter>> getAllCharacters(){
        HashMap<GoTHouse, ArrayList<GoTCharacter>> characters = new HashMap<>();

        String sql = "SELECT * FROM " + TableNameHouses + " AS h INNER JOIN "
                + TableNameCharacters + " AS c ON "
                + TableColHousesId + " = " + TableColCharactersHouseId;

        Cursor cHouses = db.rawQuery(sql, null);
        while(cHouses.moveToNext()){
            String house_id = cHouses.getString(cHouses.getColumnIndex(TableColHousesId));
            String house_name = cHouses.getString(cHouses.getColumnIndex(TableColHousesName));
            String house_imageUrl = cHouses.getString(cHouses.getColumnIndex(TableColHousesImageUrl));

            GoTHouse house = new GoTHouse(house_id, house_name, house_imageUrl);
            ArrayList<GoTCharacter> charactersList = null;
            if (!characters.containsKey(house)) {
                charactersList = new ArrayList<>();
                characters.put(house, charactersList);
            } else{
                charactersList = characters.get(house);
            }

            String char_id = cHouses.getString(cHouses.getColumnIndex(TableColCharactersID));
            String char_name = cHouses.getString(cHouses.getColumnIndex(TableColCharactersName));
            String char_description = cHouses.getString(cHouses.getColumnIndex(TableColCharactersDescription));
            String char_imageUrl = cHouses.getString(cHouses.getColumnIndex(TableColCharactersImageUrl));

            GoTCharacter character = new GoTCharacter(char_name, char_description, char_imageUrl);
            charactersList.add(character);
        }

        return characters;
    }

    public ArrayList<GoTCharacter> getCharactersBySearchValue(String value){
        ArrayList<GoTCharacter> characters = new ArrayList<>();

        String sql = "SELECT * FROM " + TableNameCharacters
                + " WHERE " + TableColCharactersName + " LIKE ?";

        Cursor cCharacter = db.rawQuery(sql, new String[]{"%" + value + "%"});
        while(cCharacter.moveToNext()){
            String char_name = cCharacter.getString(cCharacter.getColumnIndex(TableColCharactersName));
            String char_description = cCharacter.getString(cCharacter.getColumnIndex(TableColCharactersDescription));
            String char_imageUrl = cCharacter.getString(cCharacter.getColumnIndex(TableColCharactersImageUrl));

            GoTCharacter character = new GoTCharacter(char_name, char_description, char_imageUrl);
            characters.add(character);
        }

        return characters;
    }

    public ArrayList<GoTCharacter> getCharactersFromHouse(String houseId){
        ArrayList<GoTCharacter> characters = new ArrayList<>();
        String sql = "SELECT * FROM " + TableNameCharacters + " WHERE " + TableColCharactersHouseId + " = ? ";

        Cursor cCharacters = db.rawQuery(sql, new String[] { houseId });
        while(cCharacters.moveToNext()){
            String char_id = cCharacters.getString(cCharacters.getColumnIndex(TableColCharactersID));
            String char_name = cCharacters.getString(cCharacters.getColumnIndex(TableColCharactersName));
            String char_description = cCharacters.getString(cCharacters.getColumnIndex(TableColCharactersDescription));
            String char_imageUrl = cCharacters.getString(cCharacters.getColumnIndex(TableColCharactersImageUrl));

            GoTCharacter character = new GoTCharacter(char_name, char_description, char_imageUrl);
            characters.add(character);
        }

        return characters;
    }
}
