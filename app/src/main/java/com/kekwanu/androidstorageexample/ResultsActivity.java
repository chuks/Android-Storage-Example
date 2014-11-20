package com.kekwanu.androidstorageexample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class ResultsActivity extends ActionBarActivity {
    private String make;
    private String model;
    private String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //this example uses the new toolbar taht cmes in Android 5.0 and appcompat
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        //enable the Android home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent data = getIntent();
        String type = data.getStringExtra("type");

        processType(type);
    }

    private void processType(String type){

        if (type.equals("shared_prefs")){

            String carStr = retrieveFromPrefs();
            Car car = (Car) Serializer.INSTANCE.stringToObject(carStr);

            make = car.getMake();
            model = car.getModel();
            year = car.getYear();
        }
        else if (type.equals("sqlite")) {

            List<Car> savedCars = retrieveFromSqlite();

            //savedCars is a List of Cars. Retrieved the last one saved.
            Car car = retrieveFromSqlite().get(savedCars.size() - 1);

            make = car.getMake();
            model = car.getModel();
            year = car.getYear();

        }

        TextView makeTextView = (TextView)findViewById(R.id.make);
        TextView modelTextView = (TextView)findViewById(R.id.model);
        TextView yearTextView = (TextView)findViewById(R.id.year);

        makeTextView.setText(make);
        modelTextView.setText(model);
        yearTextView.setText(year);
    }

    private List<Car> retrieveFromSqlite(){
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext(), 4);

        return db.getCars();
    }

    private String retrieveFromPrefs(){
        String CAR = "com.kekwanu.sharedpreferencesexample.car";
        String APP_SHARED_PREFS = "com.kekwanu.sharedpreferencesexample";

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);

        return sharedPrefs.getString(CAR,"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home){
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){

        super.onBackPressed(); //default, calls finish()
        finish();
    }
}
