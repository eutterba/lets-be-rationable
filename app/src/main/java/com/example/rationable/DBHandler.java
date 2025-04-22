package com.example.rationable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rations_db";

    private static final String TABLE_NAME = "myrations";

    private static final String _ID_COL = "_id";

    private static final String NAME_COL = "name";

    private static final String CALORIES_COL = "calories";

    private static final String BARCODE_COL = "barcode";

    private static final String SERVINGS_COL = "servings";

    private static final String TOTAL_COL = "total";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        /*
        i made calories a real type because if i get the calories from webscraping it will be a
        decimal. i also made the servings a real type because you might only eat half of something
         */
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                +_ID_COL+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NAME_COL+" TEXT, "+CALORIES_COL+" REAL, "
                +BARCODE_COL+" Text, "+SERVINGS_COL+" REAL, "
                +TOTAL_COL+" REAL)";
        db.execSQL(CREATE_TABLE);

    }

    public void AddNewFood(String name, double calories, String barcode, double servings, double total){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(CALORIES_COL, calories);
        values.put(BARCODE_COL, barcode);
        values.put(SERVINGS_COL, servings);
        values.put(TOTAL_COL, total);

        db.insert(TABLE_NAME, null, values);
        db.close();

        //debugging statements
        Log.d("DBHandler", "Inserted new food: " + name);
    }

    public Cursor getRations(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + _ID_COL + ", " + NAME_COL + ", "
                + CALORIES_COL + ", " + BARCODE_COL + ", " + SERVINGS_COL
                + ", " + TOTAL_COL + " FROM " + TABLE_NAME + " WHERE "
                + SERVINGS_COL + " > 0";
        return db.rawQuery(query, null);

    }

    public Cursor getRationByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + _ID_COL + ", " + NAME_COL + ", "
                + CALORIES_COL + ", " + BARCODE_COL + ", " + SERVINGS_COL
                + ", " + TOTAL_COL + " FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id;
        return db.rawQuery(query, null);
    }

    public void getRationByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + _ID_COL + ", " + NAME_COL + ", "
                + CALORIES_COL + ", " + BARCODE_COL + ", " + SERVINGS_COL
                + ", " + TOTAL_COL + " FROM " + TABLE_NAME + " WHERE "
                + NAME_COL + " = " + name;
        db.rawQuery(query, null);
    }

    public Cursor getOpenRations(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + _ID_COL + ", " + NAME_COL + ", "
                + CALORIES_COL + ", " + BARCODE_COL + ", " + SERVINGS_COL
                + ", " + TOTAL_COL + " FROM " + TABLE_NAME + " WHERE "
                + SERVINGS_COL + "<" + TOTAL_COL ;
        return db.rawQuery(query, null);

    }

    public float getRationCalories(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+CALORIES_COL+ " FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor calories = db.rawQuery(query, null);
        float caloriesFloat = 0;
        if (calories.moveToFirst()) {
            caloriesFloat = calories.getFloat(0);
        }
        calories.close();
        query = "SELECT "+SERVINGS_COL+ " FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor servings = db.rawQuery(query, null);
        float servingsFloat = 0;
        if (servings.moveToFirst()) {
            servingsFloat = servings.getFloat(0);
        }
        servings.close();
        query = "SELECT "+TOTAL_COL+ " FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor total = db.rawQuery(query, null);
        float totalFloat = 0;
        if (total.moveToFirst()) {
            totalFloat = total.getFloat(0);
        }
        total.close();
        return caloriesFloat * servingsFloat * totalFloat;
    }

    public float getOpenRationCalories(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+CALORIES_COL+" FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor calories = db.rawQuery(query, null);
        float caloriesFloat = 0;
        if (calories.moveToFirst()) {
            caloriesFloat = calories.getFloat(0);
        }
        calories.close();
        query = "SELECT "+SERVINGS_COL+" FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor servings = db.rawQuery(query, null);
        float servingsFloat = 0;
        if (servings.moveToFirst()) {
            servingsFloat = servings.getFloat(0);
        }
        servings.close();
        query = "SELECT "+TOTAL_COL+" FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor total = db.rawQuery(query, null);
        float totalFloat = 0;
        if (total.moveToFirst()) {
            totalFloat = total.getFloat(0);
        }
        total.close();
        float open_amount=totalFloat%servingsFloat;
        return caloriesFloat * servingsFloat * open_amount;
    }


    public float getRationAmount(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+SERVINGS_COL+" FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor servings = db.rawQuery(query, null);
        float servingsFloat = 0;
        if (servings.moveToFirst()) {
            servingsFloat = servings.getFloat(0);
        }
        servings.close();
        query = "SELECT "+TOTAL_COL+" FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor total = db.rawQuery(query, null);
        float totalFloat = 0;
        if (total.moveToFirst()) {
            totalFloat = total.getFloat(0);
        }
        total.close();
        return servingsFloat % totalFloat;
    }

    public float getOpenRationAmount(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+SERVINGS_COL+" FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor servings = db.rawQuery(query, null);
        float servingsFloat = 0;
        if (servings.moveToFirst()) {
            servingsFloat = servings.getFloat(0);
        }
        servings.close();
        query = "SELECT "+TOTAL_COL +" FROM " + TABLE_NAME + " WHERE "
                + _ID_COL + " = " + id ;
        Cursor total = db.rawQuery(query, null);
        float totalFloat = 0;
        if (total.moveToFirst()) {
            totalFloat = total.getFloat(0);
        }
        total.close();
        float total_amount=totalFloat-(int)totalFloat;
        return servingsFloat % total_amount;
    }

    public ArrayList<String[]> getSchedule(int calories_eaten, int CALORIE_REQUiREMENTS) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor openRations = getOpenRations();
        float calories_needed = CALORIE_REQUiREMENTS - calories_eaten;
        Log.d("DBHandler", "Calories needed: " + calories_needed);
        boolean has_open_ration = true;
        ArrayList<String[]> schedule = new ArrayList();
        float curr_ration_calories;
        int curr_ration_id;
        float curr_ration_amount;
        //check for open rations
        has_open_ration = openRations.moveToFirst();
        //loops through the database, starting with the open rations till it meets requirements
        while(calories_needed>=0){
            //if there are open rations
            if(has_open_ration){
//                openRations.moveToNext();
                //grabs ration values
                curr_ration_id = openRations.getInt(0);
                curr_ration_calories = getOpenRationCalories(curr_ration_id);
                curr_ration_amount = getOpenRationAmount(curr_ration_id);
                String curr_ration_name = openRations.getString(1);
                //if the open ration has enough calories to eat
                if(curr_ration_calories > calories_needed) {
                    float eaten_amount = calories_needed / curr_ration_calories;
                    schedule.add(new String[]{curr_ration_name, String.valueOf(eaten_amount), String.valueOf(curr_ration_calories)});
                    calories_needed -= eaten_amount;
                    Log.d("DBHandler", "Calories needed: " + calories_needed);

                    break;
                }
                //if the open ration does not have enough calories
                schedule.add(new String[]{curr_ration_name, String.valueOf(curr_ration_amount), String.valueOf(curr_ration_calories)});
                calories_needed -= curr_ration_calories;
                Log.d("DBHandler", "Calories needed: " + calories_needed);
                //updates whether theres more open rations
                has_open_ration = openRations.moveToNext();
            }else {
                //there are no open rations
                Cursor allRations = getRations();
                if (allRations.moveToFirst()) {
                    curr_ration_id = allRations.getInt(0);
                    String curr_ration_name = allRations.getString(1);
                    curr_ration_calories = getRationCalories(curr_ration_id);
                    curr_ration_amount = getRationAmount(curr_ration_id);
                    int count = 0;
                    boolean food_found = false;
                    //checks if the food is in the schedule already
                    while (count < schedule.size()) {
                        if (schedule.get(count)[0].equals(curr_ration_name)) {
                            food_found = true;
                            break;
                        }
                        count++;
                    }
                    //if food is in the schedule already
                    if (food_found) {
                        float open_amount = Float.parseFloat(schedule.get(count)[1]);
                        float eaten_amount = calories_needed / curr_ration_calories;
                        float new_amount = open_amount + eaten_amount;
                        String new_amount_string = String.valueOf(new_amount);
                        String curr_ration_calories_string = String.valueOf(curr_ration_calories);
                        schedule.set(count, new String[]{curr_ration_name, new_amount_string, curr_ration_calories_string});
                        calories_needed -= eaten_amount;
                        Log.d("DBHandler", "Calories needed: " + calories_needed);
                        break;
                    }
                    float eaten_amount = calories_needed / curr_ration_calories;
                    float eaten_calories = eaten_amount * curr_ration_calories;
                    calories_needed -= eaten_calories;
                    Log.d("DBHandler", "Calories needed: " + calories_needed);
                    schedule.add(new String[]{curr_ration_name, String.valueOf(eaten_amount), String.valueOf(curr_ration_calories)});
                    allRations.moveToNext();
                }
            }

        }
        return schedule;
    }



//    public void updateRation(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT NAME_COL FROM " + TABLE_NAME + " WHERE "
//                + _ID_COL + " = " + id ;
//        Cursor name = db.rawQuery(query, null);
//        String nameString = name.getString(0);
//        query = "SELECT CALORIES_COL FROM " + TABLE_NAME + " WHERE "
//                + _ID_COL + " = " + id ;
//        Cursor calories = db.rawQuery(query, null);
//        float caloriesFloat = calories.getFloat(0);
//        query = "SELECT BARCODE_COL FROM " + TABLE_NAME + " WHERE "
//                + _ID_COL + " = " + id ;
//        Cursor barcode = db.rawQuery(query, null);
//        String barcodeString = barcode.getString(0);
//        query = "SELECT SERVINGS_COL FROM " + TABLE_NAME + " WHERE "
//                + _ID_COL + " = " + id ;
//        Cursor servings = db.rawQuery(query, null);
//        float servingsFloat = servings.getFloat(0);
//        query = "SELECT TOTAL_COL FROM " + TABLE_NAME + " WHERE "
//                + _ID_COL + " = " + id ;
//        Cursor total = db.rawQuery(query, null);
//        float totalFloat = total.getFloat(0);
//        ContentValues previousValues = new ContentValues();
//
//
//    }



    public void updateRations(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    public void updateRations(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    public void updateRations(int id, String name, double calories){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    public void updateRations(int id, String name, double calories, int barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    public void updateRations(int id, String name, double calories, int barcode, double servings){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    public void updateRations(String name, double calories, int barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    public void updateRations(String name, double calories, int barcode, double servings){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }







    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



}
