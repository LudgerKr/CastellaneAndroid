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

public class ViewAdminAnnonces extends AppCompatActivity implements View.OnClickListener {

    private Button btnBackViewannonces, btnAddAnnonce, btnModifyAnnonce, btnDeleteAnnonce;
    private Spinner spAnnonceID;
    private ListView listannonces;
    private String email, userId;

    private ArrayList<Annonces> annonces = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_annonces_view);

        this.spAnnonceID = findViewById(R.id.spAnnonceID);

        this.btnBackViewannonces = findViewById(R.id.btnBackViewAnnonce);
        this.btnAddAnnonce = findViewById(R.id.btnAddAnnonce);
        this.btnModifyAnnonce = findViewById(R.id.btnModifyAnnonce);
        this.btnDeleteAnnonce = findViewById(R.id.btnDeleteAnnonce);

        this.btnBackViewannonces.setOnClickListener(this);
        this.btnAddAnnonce.setOnClickListener(this);
        this.btnModifyAnnonce.setOnClickListener(this);
        this.btnDeleteAnnonce.setOnClickListener(this);

        this.listannonces = findViewById(R.id.listAnnonce);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

        final ViewAdminAnnonces sp = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBannonces DBannonces = new DBannonces();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBannonces.execute();
                    }
                });
            }
        });
        thread.start();

        Thread threadSP = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBannoncesLists DBannoncesLists = new DBannoncesLists();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBannoncesLists.execute();
                    }
                });
            }
        });
        threadSP.start();

    }

    @Override
    public void onClick(View v) {
        String uneAnnonce = this.spAnnonceID.getSelectedItem().toString();
        if (v.getId() == R.id.btnBackViewAnnonce) {
            Intent intent = new Intent(this, MenuAdministrateur.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnAddAnnonce) {
            Intent intent = new Intent(this, AddAnnonce.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnModifyAnnonce) {
            Intent intent = new Intent(this, ModifyAnnonces.class);
            intent.putExtra("email", this.email);
            intent.putExtra("title", uneAnnonce);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnDeleteAnnonce) {
            Intent intent  = new Intent(this, ViewAdminAnnonces.class);
            new AnnonceDelete(this).execute(uneAnnonce);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        }
    }

    public ArrayList<Annonces> getannonces() {
        return annonces;
    }

    public void remplirLV() {
        if (annonces != null) {
            ArrayList<String> annoncesString = new ArrayList<>();
            for (Annonces annonce : annonces) {
                annoncesString.add(annonce.toString());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, annoncesString);
            listannonces.setAdapter(adapter);
        }
    }

    public void setannonces(ArrayList<Annonces> annonces) {
        this.annonces = annonces;
        remplirLV();
    }

    class DBannonces extends AsyncTask<Void, Void, ArrayList<Annonces>> {

        @Override
        protected void onPostExecute(ArrayList<Annonces> annonces) {
            setannonces(annonces);
        }

        @Override
        protected ArrayList<Annonces> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_annonces.php";
            ArrayList<Annonces> annonces = new ArrayList<Annonces>();
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
                    Annonces annonce = new Annonces(unObjet.getInt("idnew"),
                            unObjet.getInt("userid"),
                            unObjet.getString("title"),
                            unObjet.getString("content"));
                    annonces.add(annonce);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return annonces;
        }
    }

    public void remplirSP() {
        if (annonces != null) {
            System.out.println("ifSP");
            ArrayList<String> annoncesString = new ArrayList<>();
            for (Annonces annonce : annonces) {
                annoncesString.add(annonce.getTitle());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, annoncesString);
            spAnnonceID.setAdapter(adapter);
        } else {
            System.out.println("no if");
        }
    }

    public void setannonceLists(ArrayList<Annonces> annonces) {
        this.annonces = annonces;
        remplirSP();
    }

    class DBannoncesLists extends AsyncTask<Void, Void, ArrayList<Annonces>> {

        @Override
        protected void onPostExecute(ArrayList<Annonces> annonces) {
            setannonceLists(annonces);
        }

        @Override
        protected ArrayList<Annonces> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_Uneannonce.php";
            ArrayList<Annonces> annonces = new ArrayList<Annonces>();
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
                    Annonces annonce = new Annonces(unObjet.getString("title"));
                    annonces.add(annonce);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return annonces;
        }
    }

    class AnnonceDelete extends AsyncTask<String, Void, Void> {
        private WeakReference<ViewAdminAnnonces> activityReference;

        AnnonceDelete(ViewAdminAnnonces context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(String... annonces) {
            String uneAnnonce = spAnnonceID.getSelectedItem().toString();
                String uri = Constant.SERVER + "deleteAnnonce.php?title=" + uneAnnonce;
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
            ViewAdminAnnonces annonceActivity = activityReference.get();
            if (annonceActivity == null || annonceActivity.isFinishing()) return ;
            Toast.makeText(annonceActivity, "Annonce supprimée", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(annonceActivity, ViewAdminAnnonces.class);
            annonceActivity.startActivity(intent);
        }
    }
}