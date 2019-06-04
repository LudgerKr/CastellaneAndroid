package com.example.castellane;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifySeances extends AppCompatActivity implements View.OnClickListener {

    private EditText editUpdateDate, editUpdateTime;
    private Button btnUpdateSeance, btnBackUpdateSeance;
    private Seances seance;
    private String idseance;
    private String email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_seances_update);

        this.btnUpdateSeance = findViewById(R.id.btnUpdateSeance);
        this.btnBackUpdateSeance = findViewById(R.id.btnBackUpdateSeance);

        this.btnUpdateSeance.setOnClickListener(this);
        this.btnBackUpdateSeance.setOnClickListener(this);

        this.editUpdateDate = findViewById(R.id.editUpdateDate);
        this.editUpdateTime = findViewById(R.id.editUpdateTime);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");
        this.idseance = this.getIntent().getStringExtra("idseance");
        new GetSeance(this).execute(this.idseance);
    }

    private TextWatcher mDateEntryWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length()==2 && before ==0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working)>12) {
                    isValid = false;
                } else {
                    working+="-";
                    editUpdateDate.setText(working);
                    editUpdateDate.setSelection(working.length());
                }
            } else if (working.length()!=10) {
                isValid = false;
            }

            if (!isValid) {
                editUpdateDate.setError("Entrez une date valide : YYYY-MM-DD");
            } else {
                editUpdateDate.setError(null);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };

    private TextWatcher mTimeEntryWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length()==2 && before ==0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working)>12) {
                    isValid = false;
                } else {
                    working+=":";
                    editUpdateTime.setText(working);
                    editUpdateTime.setSelection(working.length());
                }
            } else if (working.length()!=8) {
                isValid = false;
            }

            if (!isValid) {
                editUpdateTime.setError("Entrez une horaire valide : HH:MM:SS");
            } else {
                editUpdateTime.setError(null);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackUpdateSeance) {
            Intent intent = new Intent(this, ViewAdminSeances.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnUpdateSeance) {
            String date, time;
            date = this.editUpdateDate.getText().toString();
            time = this.editUpdateTime.getText().toString();
            SimpleDateFormat unedate = new SimpleDateFormat("YYYY-MM-DD");
            unedate.setLenient(true);
            Date d = new Date();

            SimpleDateFormat unehoraire = new SimpleDateFormat("HH:MM:SS");
            unehoraire.setLenient(true);
            Date t = new Date();

            if (date.length() == 0) {
                Toast.makeText(this, "La date est obligatoire", Toast.LENGTH_LONG).show();
            } else if (time.length() == 0) {
                Toast.makeText(this, "L'horaire est obligatoire", Toast.LENGTH_LONG).show();
            } else {
                this.seance.setDate(date);
                this.seance.setTime(time);
                new UpdateSeance(this).execute(this.seance);
                Intent intent = new Intent(this, ViewAdminSeances.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
            }
        }
    }


    class GetSeance extends AsyncTask<String, Void, Seances> {
        private WeakReference<ModifySeances> activityReference;

        GetSeance(ModifySeances context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Seances doInBackground(String... idseances) {
            String idseance = idseances[0];
            String apiURL = Constant.SERVER + "my_seance.php?idseance=" +idseance;
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
                seance = new Seances(
                        object.getString("idseance"),
                        object.getString("date"),
                        object.getString("time")
                );
            } catch (JSONException e) {
                Log.e("Erreur de json", e.toString());
                System.out.println("Erreur de json"+e.toString());
            }
            System.out.println(apiURL + " URL");
            return seance;
        }

        @Override
        protected void onPostExecute(Seances Seance) {
            ModifySeances SeanceActivity = activityReference.get();
            if (Seance != null) {
                SeanceActivity.seance = seance;
                SeanceActivity.editUpdateDate.setText(Seance.getDate());
                SeanceActivity.editUpdateTime.setText(Seance.getTime());
            } else {
                System.out.println("Seance vide");
            }
        }
    }

    class UpdateSeance extends AsyncTask<Seances, Void, Void> {
        private WeakReference<ModifySeances> activityReference;

        UpdateSeance(ModifySeances context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Seances... Seances) {
            Seances SeanceToUpdate = Seances[0];
            String apiURL = Constant.SERVER + "update_seance.php";
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
                String parameters = "date=" + SeanceToUpdate.getDate() + "&time=" + SeanceToUpdate.getTime() + "&idseance=" + SeanceToUpdate.getIdseance();
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
            ModifySeances modifySeance = activityReference.get();
            if (modifySeance == null || modifySeance.isFinishing()) return ;
            Toast.makeText(modifySeance, "Seance mis à jour", Toast.LENGTH_LONG).show();
        }
    }
}
