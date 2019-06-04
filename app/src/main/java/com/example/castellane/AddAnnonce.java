package com.example.castellane;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class AddAnnonce extends AppCompatActivity implements View.OnClickListener {

    private EditText editAddTitle, editAddContent;
    private Button btnAddAnnonce, btnBackAddAnnonce;
    private Users loggedInUser = null;
    private String email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_annonces_add);

        this.btnAddAnnonce = findViewById(R.id.btnAddAnnonce);
        this.btnBackAddAnnonce = findViewById(R.id.btnBackAddAnnonce);

        this.btnAddAnnonce.setOnClickListener(this);
        this.btnBackAddAnnonce.setOnClickListener(this);

        this.editAddTitle = findViewById(R.id.editAddTitle);
        this.editAddContent = findViewById(R.id.editAddContent);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackAddAnnonce) {
            Intent intent = new Intent(this, ViewAdminAnnonces.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnAddAnnonce) {
            String title, content;
            title = this.editAddTitle.getText().toString();
            content = this.editAddContent.getText().toString();
            if (title.length() == 0) {
                Toast.makeText(this, "Le titre est obligatoire", Toast.LENGTH_LONG).show();
            } else if (content.length() == 0) {
                Toast.makeText(this, "Le contenu est obligatoire", Toast.LENGTH_LONG).show();
            } else {
                Annonces annonce = new Annonces(title, content);
                new NewAnnonce(this).execute(annonce);
                Intent intent = new Intent(this, ViewAdminAnnonces.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
            }
        }
    }

    class NewAnnonce extends AsyncTask<Annonces, Void, Void> {
        private WeakReference<AddAnnonce> activityReference;

        NewAnnonce(AddAnnonce context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Annonces... annonces) {
            Annonces annonceToAdd = annonces[0];
            String apiURL = Constant.SERVER + "add_annonces.php";
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
                String parameters = "title=" + annonceToAdd.getTitle() + "&content=" + annonceToAdd.getContent() + "&userid=" + 1;
                Log.e("PARAMS_DBG", parameters);
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
            AddAnnonce addAnnonce = activityReference.get();
            if (addAnnonce == null || addAnnonce.isFinishing()) return ;
            Toast.makeText(addAnnonce, "Votre annonce a été ajoutée", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(addAnnonce, ViewAdminAnnonces.class);
            addAnnonce.startActivity(intent);
        }
    }
}
