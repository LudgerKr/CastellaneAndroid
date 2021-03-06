package com.example.castellane;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewAdminSeances extends AppCompatActivity implements View.OnClickListener {

    private Button btnBackViewseances, btnAddSeance, btnModifySeance, btnDeleteSeance;
    private Spinner spSeanceID;
    private ListView listseances;
    private String email, userId;

    private ArrayList<Seances> seances = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_seances_view);

        this.spSeanceID = findViewById(R.id.spSeanceID);

        this.btnBackViewseances = findViewById(R.id.btnBackViewSeance);
        this.btnAddSeance = findViewById(R.id.btnAddSeance);
        this.btnModifySeance = findViewById(R.id.btnModifySeance);
        this.btnDeleteSeance = findViewById(R.id.btnDeleteSeance);

        this.btnBackViewseances.setOnClickListener(this);
        this.btnAddSeance.setOnClickListener(this);
        this.btnModifySeance.setOnClickListener(this);
        this.btnDeleteSeance.setOnClickListener(this);

        this.listseances = findViewById(R.id.listSeance);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

        final ViewAdminSeances sp = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBseances DBseances = new DBseances();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBseances.execute();
                    }
                });
            }
        });
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBseancesLists DBseancesLists = new DBseancesLists();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBseancesLists.execute();
                    }
                });
            }
        });
        thread2.start();

    }

    @Override
    public void onClick(View v) {
        String uneseance = this.spSeanceID.getSelectedItem().toString();

        if (v.getId() == R.id.btnBackViewSeance) {
            Intent intent = new Intent(this, MenuAdministrateur.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnAddSeance) {
            Intent intent = new Intent(this, AddSeances.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnModifySeance) {
            Intent intent = new Intent(this, ModifySeances.class);
            intent.putExtra("idseance", uneseance);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnDeleteSeance) {
            Intent intent  = new Intent(this, ViewAdminSeances.class);
            intent.putExtra("seance", uneseance);
            new SeanceDelete(this).execute(uneseance);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        }
    }

    public ArrayList<Seances> getseances() {
        return seances;
    }

    public void remplirLV() {
        if (seances != null) {
            ArrayList<String> seancesString = new ArrayList<>();
            for (Seances seance : seances) {
                seancesString.add(seance.toString());
                System.out.println("seance => "+seance.toString());
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, seancesString);
                listseances.setAdapter(adapter);
            }

        }
    }

    public void setseances(ArrayList<Seances> seances) {
        this.seances = seances;
        remplirLV();
    }

    class DBseances extends AsyncTask<Void, Void, ArrayList<Seances>> {

        @Override
        protected void onPostExecute(ArrayList<Seances> seances) {
            setseances(seances);
        }

        @Override
        protected ArrayList<Seances> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_seance.php";
            ArrayList<Seances> seances = new ArrayList<Seances>();
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
                    Seances seance = new Seances(unObjet.getString("idseance"),
                            unObjet.getString("date"),
                            unObjet.getString("time"));
                    seances.add(seance);
                }
                return seances;
            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return null;
        }
    }

    public void remplirSP() {
        if (seances != null) {
            System.out.println("if");
            ArrayList<String> seancesString = new ArrayList<>();
            for (Seances seance : seances) {
                seancesString.add(seance.getIdseance());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, seancesString);
            spSeanceID.setAdapter(adapter);
        } else {
            System.out.println("no if");
        }
    }

    public void setseanceLists(ArrayList<Seances> seances) {
        this.seances = seances;
        remplirSP();
    }

    class DBseancesLists extends AsyncTask<Void, Void, ArrayList<Seances>> {

        @Override
        protected void onPostExecute(ArrayList<Seances> seances) {
            setseanceLists(seances);
        }

        @Override
        protected ArrayList<Seances> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_Uneseance.php";
            ArrayList<Seances> seances = new ArrayList<Seances>();
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
                    Seances seance = new Seances(unObjet.getString("idseance"));
                    seances.add(seance);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return seances;
        }
    }

    class SeanceDelete extends AsyncTask<String, Void, Void> {
        private WeakReference<ViewAdminSeances> activityReference;

        SeanceDelete(ViewAdminSeances context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(String... seances) {
            String uneSeance = spSeanceID.getSelectedItem().toString();
            String uri = Constant.SERVER + "deleteSeance.php?idseance=" + uneSeance;
            Log.e("uri", uri);
            String result = "";
            try {
                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                is.close();
                br.close();
            } catch (Exception e) {
                Log.e("Erreur de connexion", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ViewAdminSeances seanceActivity = activityReference.get();
            if (seanceActivity == null || seanceActivity.isFinishing()) return ;
            Toast.makeText(seanceActivity, "Seance supprimée", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(seanceActivity, ViewAdminSeances.class);
            seanceActivity.startActivity(intent);
        }
    }
}