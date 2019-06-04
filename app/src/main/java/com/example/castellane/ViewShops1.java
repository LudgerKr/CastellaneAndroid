package com.example.castellane;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewShops1 extends AppCompatActivity implements View.OnClickListener {

    private Button btnBackViewShops;
    private ListView listShops;
    private Shops shop = null;
    private String idShop, email, userId;

    private ArrayList<Shops> shops = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_view1);

        this.btnBackViewShops = findViewById(R.id.btnBackViewShops);
        this.btnBackViewShops.setOnClickListener(this);
        this.listShops = findViewById(R.id.listShops);

        this.idShop = this.getIntent().getStringExtra("idshop");
        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");


        final ViewShops1 sp = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBShops DBShops = new DBShops();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBShops.execute();
                    }
                });
            }
        });
        thread.start();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackViewShops) {
            Intent intent = new Intent(this, ShopMenu.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        }
    }

    public ArrayList<Shops> getShops() {
        return shops;
    }

    public void remplirLV() {
        if (shops != null) {
            System.out.println("if");
            ArrayList<String> shopsString = new ArrayList<>();
            for (Shops shop : shops) {
                shopsString.add(shop.toString());
                System.out.println("shop => "+shop.toString());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shopsString);
            listShops.setAdapter(adapter);
        } else {
            System.out.println("no if");
        }
    }

    public void setShops(ArrayList<Shops> shops) {
        this.shops = shops;
        remplirLV();
    }

    class DBShops extends AsyncTask<Void, Void, ArrayList<Shops>> {

        @Override
        protected void onPostExecute(ArrayList<Shops> shops) {
            setShops(shops);
        }

        @Override
        protected ArrayList<Shops> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_shops.php?idshop=" +1;
            ArrayList<Shops> shops = new ArrayList<Shops>();
            String resultat = "";
            try {
                URL uneUrl = new URL(url);
                HttpURLConnection uneConnexion = (HttpURLConnection) uneUrl.openConnection();
                uneConnexion.setRequestMethod("GET");
                uneConnexion.setReadTimeout(15000);
                uneConnexion.setConnectTimeout(20000);
                uneConnexion.setDoInput(true);
                uneConnexion.connect();

                InputStream is = uneConnexion.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                resultat = sb.toString();
                Log.e("Chaine lue", resultat);
                br.close();
                is.close();
            } catch (IOException e) {
                Log.e("Erreur de connexion", url);
            }

            try {
                JSONArray tabJson = new JSONArray(resultat);
                for (int i = 0; i < tabJson.length(); i++) {
                    JSONObject unObjet = tabJson.getJSONObject(i);
                    Shops shop = new Shops(unObjet.getInt("idshop"),
                            unObjet.getString("title"),
                            unObjet.getInt("price"),
                            unObjet.getString("content"),
                            unObjet.getString("content2"),
                            unObjet.getString("content3"),
                            unObjet.getString("content4"),
                            unObjet.getString("content5"));
                    shops.add(shop);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return shops;
        }
    }
}