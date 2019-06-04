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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddSeances extends AppCompatActivity implements View.OnClickListener {

    private EditText editAddDate, editAddTime;
    private Button btnAddSeance, btnBackAddSeance;
    private String email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_seances_add);

        this.btnAddSeance = findViewById(R.id.btnAddSeance);
        this.btnBackAddSeance = findViewById(R.id.btnBackAddSeance);

        this.btnAddSeance.setOnClickListener(this);
        this.btnBackAddSeance.setOnClickListener(this);

        this.editAddDate = findViewById(R.id.editAddDate);
        this.editAddDate.addTextChangedListener(mDateEntryWatcher);

        this.editAddTime = findViewById(R.id.editAddTime);
        this.editAddTime.addTextChangedListener(mTimeEntryWatcher);


        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");
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
                    editAddDate.setText(working);
                    editAddDate.setSelection(working.length());
                }
            } else if (working.length()!=10) {
                isValid = false;
            }

            if (!isValid) {
                editAddDate.setError("Entrez une date valide : YYYY-MM-DD");
            } else {
                editAddDate.setError(null);
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
                    editAddTime.setText(working);
                    editAddTime.setSelection(working.length());
                }
            } else if (working.length()!=8) {
                isValid = false;
            }

            if (!isValid) {
                editAddTime.setError("Entrez une horaire valide : HH:MM:SS");
            } else {
                editAddTime.setError(null);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackAddSeance) {
            Intent intent = new Intent(this, ViewAdminSeances.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnAddSeance) {
            String date, time;
            date = this.editAddDate.getText().toString();
            time = this.editAddTime.getText().toString();
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
                Seances seance = new Seances(date, time);
                new NewSeance(this).execute(seance);
                Intent intent = new Intent(this, ViewAdminSeances.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
            }
        }
    }

    class NewSeance extends AsyncTask<Seances, Void, Void> {
        private WeakReference<AddSeances> activityReference;

        NewSeance(AddSeances context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Seances... seances) {
            Seances seanceToAdd = seances[0];
            String apiURL = Constant.SERVER + "add_seance.php";
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
                String parameters = "date=" + seanceToAdd.getDate() + "&time=" + seanceToAdd.getTime();
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
            AddSeances addSeance = activityReference.get();
            if (addSeance == null || addSeance.isFinishing()) return ;
            Toast.makeText(addSeance, "Votre seance a été ajoutée", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(addSeance, ViewAdminSeances.class);
            addSeance.startActivity(intent);
        }
    }
}
