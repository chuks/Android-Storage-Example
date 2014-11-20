package com.kekwanu.androidstorageexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;


public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = MySQLiteHelper.class.getCanonicalName();

    // Database Name
    private static final String DATABASE_NAME = "CarDB";

    // Chats table name
    private static final String TABLE_CARS = "cars";

    // Chats Table Columns names
    private static final String KEY_MAKE  = "make";
    private static final String KEY_MODEL = "model";
    private static final String KEY_YEAR  = "year";

    private static final String[] COLUMNS = {KEY_MAKE,KEY_MODEL,KEY_YEAR};
    private SQLiteDatabase db;
 
    public MySQLiteHelper(Context context, int dbVersion) {
        super(context, DATABASE_NAME, null, dbVersion);

        Log.i(TAG, "MySQLiteHelper");
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");

        // SQL statement to create chat table
        String CREATE_TABLE_CARS = "CREATE TABLE cars ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "make TEXT, "+
                "model TEXT, "+
                "year TEXT)";
 
        // create books table
        db.execSQL(CREATE_TABLE_CARS);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");

        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS chats");
 
        // create fresh chats table
        this.onCreate(db);
    }

    public SQLiteDatabase openDB() {
        Log.i(TAG, "openDB");

        db = this.getWritableDatabase();

        return db;
    }

    public void addCar(Car car){
        Log.i(TAG, "addCar");

        try {
            // 1. get reference to writable DB
            SQLiteDatabase db = openDB();

            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_MAKE,  car.getMake());
            values.put(KEY_MODEL, car.getModel());
            values.put(KEY_YEAR, car.getYear());

            // 3. insert
            db.insert(TABLE_CARS, null, values);

            // 4. close
            db.close();
        }
        catch (SQLException e){
            Log.i(TAG, "addChat - SQLException: " + e.getMessage());
        }
    }

    public List<Car> getCars() {
        Log.i(TAG, "getCars");

        List<Car> cars = new LinkedList<Car>();

        // 1. build the query
        String query = "SELECT * FROM " + TABLE_CARS;

        // 2. get reference to writable DB
        try {
            SQLiteDatabase db = openDB();
            Cursor cursor = db.rawQuery(query, null);

            // 3. go over each row, build book and add it to list
            Car car = null;
            if (cursor.moveToFirst()) {
                do {
                    car = new Car(
                            cursor.getString(1),   //make
                            cursor.getString(2),   //model
                            cursor.getString(3)    //year
                    );

                    // Add chat to chats
                    cars.add(car);
                } while (cursor.moveToNext());
            }

            // 3. close
            db.close();
        }
        catch (SQLException e){
            Log.i(TAG, "getCars - SQLException: " + e.getMessage());
        }
        catch (NullPointerException e){
            Log.i(TAG, "getCars - NullPointerException: " + e.getMessage());
        }

        Log.i(TAG, "getCars - number of cars: " + cars.size());

        // return cars
        return cars;
    }

    public void deleteAllCarsWithMake(String make) {
        Log.i(TAG, "deleteAllCarsWithMake");

        String whereClause = "make = ?";
        String[]whereArgs = new String[]{make};

        // 2. delete
        try {
            SQLiteDatabase db = openDB();
            db.delete(TABLE_CARS,whereClause,whereArgs);
        }
        catch (SQLException e) {
            Log.i(TAG, "deleteAllCarsWithMake - SQLException: " + e.getMessage());
        }
        catch (NullPointerException e){
            Log.i(TAG, "deleteAllCarsWithMake - NullPointerException: " + e.getMessage());
        }

        // 3. close
        db.close();
    }

    public void deleteAllCars(){
        Log.i(TAG, "deleteAllCars");

        try {
            SQLiteDatabase db = openDB();
            db.delete(TABLE_CARS, null, null);
        }
        catch (SQLException e) {
            Log.i(TAG, "deleteAllCars - SQLException: " + e.getMessage());
        }
        catch (NullPointerException e){
            Log.i(TAG, "deleteAllCars - NullPointerException: " + e.getMessage());
        }

        // 3. close
        db.close();
    }

}