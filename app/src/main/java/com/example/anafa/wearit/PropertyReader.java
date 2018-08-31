package com.example.anafa.wearit;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private Context context;
    private Properties properties;

    public PropertyReader(Context context){
        this.context= context;
        this.properties = getMyProperties(PropertyReader.getPropertyFileName());
    }

    public Properties getMyProperties(String file){
        try{
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(file);
            if (properties == null) {
                properties = new Properties();
            }
            properties.load(inputStream);

        }catch (Exception e){
            System.out.print(e.getMessage());
        }

        return properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public static String getPropertyFileName() {
        return "config.properties";
    }
}