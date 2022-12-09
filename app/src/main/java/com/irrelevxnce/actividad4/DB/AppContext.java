package com.irrelevxnce.actividad4.DB;

import android.app.Application;

import com.irrelevxnce.actividad4.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AppContext extends Application {
    public StringBuilder line = new StringBuilder();
    public String create;
    public String nif;


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            InputStream is = getResources().openRawResource(R.raw.clientes);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String l;
            while ((l = br.readLine()) != null) {
                line.append(l);
                }
            } catch (IOException e) {
                e.printStackTrace();
        }
        create = line.toString();
    }
}
