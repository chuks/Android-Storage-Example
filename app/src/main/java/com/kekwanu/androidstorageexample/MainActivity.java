package com.kekwanu.androidstorageexample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements Button.OnClickListener{
    private static String TAG = MainActivity.class.getCanonicalName();

    private EditText make;
    private EditText model;
    private EditText year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        make = (EditText)findViewById(R.id.make_input);
        model = (EditText)findViewById(R.id.model_input);
        year = (EditText)findViewById(R.id.year_input);

        Button prefsBtn = (Button)findViewById(R.id.prefs_button);
        prefsBtn.setOnClickListener(this);

        Button sqliteBtn = (Button)findViewById(R.id.sqlite_button);
        sqliteBtn.setOnClickListener(this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        String makeStr;
        String modelStr;
        String yearStr;

        if (isEmpty(make.getText())) {

            Toast.makeText(this, getString(R.string.no_make), Toast.LENGTH_SHORT).show();
        }
        else if (isEmpty(model.getText())) {

            Toast.makeText(this, getString(R.string.no_model), Toast.LENGTH_SHORT).show();
        }
        else if (isEmpty(year.getText())) {

            Toast.makeText(this, getString(R.string.no_year), Toast.LENGTH_SHORT).show();
        }

        else {

            makeStr = make.getText().toString().trim();
            modelStr = model.getText().toString();
            yearStr = year.getText().toString();

            Car car = new Car(makeStr, modelStr, yearStr);

            Intent intent = new Intent(this, ResultsActivity.class);

            switch (v.getId()) {
                case R.id.prefs_button:

                    intent.putExtra("type","shared_prefs");
                    String carStr = Serializer.INSTANCE.objectToString(car);
                    saveToPrefs(carStr);

                    break;

                case R.id.sqlite_button:
                    intent.putExtra("type","sqlite");
                    saveToSql(car);
                    break;

            }

            startActivity(intent);
        }
    }

    public boolean isEmpty(Editable editable){

        if (editable.equals(null)){
            Log.i(TAG, "isEmpty - editable is null");

            return true;
        }
        else{
            return editable.toString().trim().length() == 0;
        }
    }

    private void saveToPrefs(String carStr){

        String CAR = "com.kekwanu.sharedpreferencesexample.car";
        String APP_SHARED_PREFS = "com.kekwanu.sharedpreferencesexample";

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor;

        prefsEditor = sharedPrefs.edit();
        prefsEditor.putString(CAR, carStr);
        prefsEditor.apply();
    }

    private void saveToSql(Car car){
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext(), 4);

        db.addCar(car);
    }

}
