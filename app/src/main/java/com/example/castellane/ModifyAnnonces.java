package com.example.castellane;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModifyAnnonces extends AppCompatActivity implements View.OnClickListener {

    private EditText editUpdateTitle, editUpdateContent;
    private Button btnUpdateAnnonce, btnBackUpdateAnnonce;
    private Annonces annonce;
    private String title;
    private String email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_annonces_update);

        this.btnUpdateAnnonce = findViewById(R.id.btnUpdateAnnonce);
        this.btnBackUpdateAnnonce = findViewById(R.id.btnBackUpdateAnnonce);

        this.btnUpdateAnnonce.setOnClickListener(this);
        this.btnBackUpdateAnnonce.setOnClickListener(this);

        this.editUpdateTitle = findViewById(R.id.editUpdateTitle);
        this.editUpdateContent = findViewById(R.id.editUpdateContent);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

        this.title = this.getIntent().getStringExtra("title");
        new GetAnnonce(this).execute(this.title);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackUpdateAnnonce) {
            Intent intent = new Intent(this, ViewAdminAnnonces.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnUpdateAnnonce) {
            final String title, content;
            title = this.editUpdateTitle.getText().toString();
            content = this.editUpdateContent.getText().toString();
            if (title.length() == 0) {
                Toast.makeText(this, "Le titre est obligatoire", Toast.LENGTH_LONG).show();
            } else if (content.length() == 0) {
                Toast.makeText(this, "Le contenu est obligatoire", Toast.LENGTH_LONG).show();
            } else {
                this.annonce.setTitle(title);
                this.annonce.setContent(content);
                new UpdateAnnonce(this).execute(this.annonce);
                Intent intent = new Intent(this, ViewAdminAnnonces.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
            }
        }
    }

    class GetAnnonce extends AsyncTask<String, Void, Annonces> {
        private WeakReference<ModifyAnnonces> activityReference;

        GetAnnonce(ModifyAnnonces context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Annonces doInBackground(String... titles) {
            String title = titles[0];
            String apiURL = Constant.SERVER + "my_annonce.php?title=" +title;
            String result = "";
            try {
                URL url = new URL(apiURL);
                HttpURLConnection httpConnexion = (HttpURLConnection) url.openConnection();
                httpConnexion.setRequestMethod("POST");
                httpConnexion.setReadTimeout(15000);
                httpConnexion.setConnectTimeout(20000);
                httpConnexion.setDoInput(true);
                httpConnexion.setDoOutput(true);
                httpConnexion.connect();
                OutputStream os = httpConnexion.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.flush();
                bw.close();
                os.close();
                InputStream is = httpConnexion.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                br.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("API_ERROR", e.toString());
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject object = jsonArray.getJSONObject(0);
                annonce = new Annonces(
                        object.getInt("idnew"),
                        object.getString("title"),
                        object.getString("content")
                );
            } catch (JSONException e) {
                Log.e("Erreur de json", e.toString());
                System.out.println("Erreur de json"+e.toString());
            }
            System.out.println(apiURL + " URL");
            return annonce;
        }

        @Override
        protected void onPostExecute(Annonces annonce) {
            ModifyAnnonces annonceActivity = activityReference.get();
            if (annonce != null) {
                annonceActivity.annonce = annonce;
                annonceActivity.editUpdateTitle.setText(annonce.getTitle());
                annonceActivity.editUpdateContent.setText(annonce.getContent());
            } else {
                System.out.println("Annonce vide");
            }
        }
    }

    class UpdateAnnonce extends AsyncTask<Annonces, Void, Void> {
        private WeakReference<ModifyAnnonces> activityReference;

        UpdateAnnonce(ModifyAnnonces context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Annonces... annonces) {
            Annonces annonceToUpdate = annonces[0];
            String apiURL = Constant.SERVER + "update_annonces.php";
            String result = "";
            try {
                URL url = new URL(apiURL);
                HttpURLConnection httpConnexion = (HttpURLConnection) url.openConnection();
                httpConnexion.setRequestMethod("POST");
                httpConnexion.setReadTimeout(15000);
                httpConnexion.setConnectTimeout(20000);
                httpConnexion.setDoInput(true);
                httpConnexion.setDoOutput(true);
                httpConnexion.connect();
                OutputStream os = httpConnexion.getOutputStream();
                String parameters = "idnew=" + annonceToUpdate.getIdnew() + "&title=" + annonceToUpdate.getTitle() + "&content=" + annonceToUpdate.getContent() + "&userid=1";
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(parameters);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = httpConnexion.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                br.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("API_ERROR", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ModifyAnnonces modifyAnnonces = activityReference.get();
            if (modifyAnnonces == null || modifyAnnonces.isFinishing()) return ;
            Toast.makeText(modifyAnnonces, "Annonce mis à jour", Toast.LENGTH_LONG).show();
        }
    }
}
