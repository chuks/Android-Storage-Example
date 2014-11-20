package com.kekwanu.androidstorageexample;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public enum Serializer {
    INSTANCE;

	private static final String TAG = "com.kekwanu.androidstorageexample.Serializer";

	private Serializer(){
		Log.i(TAG,"constructor");
	}
	
	public String objectToString(Object object){
		Log.i(TAG,"objectToString");

	    try {
	    	Log.i(TAG,"objectToString - try block");

	    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    	ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	    	objectOutputStream.writeObject(object);
	    	objectOutputStream.close();
	    	
	    	return new String(Base64.encode(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));
	    	    
	    } 
	    catch (Exception e) {
	    	Log.i(TAG,"objectToString - exception block");
	    	e.printStackTrace();
	    	
	    	return "";
	    }
	}

    public Object stringToObject(String string){
        Log.i(TAG,"stringToObject");

        byte[] bytes = Base64.decode(string.getBytes(), Base64.DEFAULT);

        try {
            Log.i(TAG, "stringToObject - try block");

            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));

            return objectInputStream.readObject();
        }

        catch (IOException e) {
            Log.i(TAG,"stringToObject - exception block - err: "+e.getMessage());

            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
