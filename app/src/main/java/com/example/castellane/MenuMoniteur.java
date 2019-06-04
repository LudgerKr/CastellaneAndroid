package com.example.castellane;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuMoniteur extends AppCompatActivity implements View.OnClickListener {

    private Button btnMonitorVehicles, btnMyMonitorSessions, btnProfile, btnSignout;
    private String email, userId;
    private Users loggedInUser = null;

    public void fillInputs() {
        this.userId = loggedInUser.getIduser();
    }

    public void setLoggedInUser(Users loggedInUser) {
        this.loggedInUser = loggedInUser;
        fillInputs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_monitor);

        this.btnMonitorVehicles = findViewById(R.id.btnMonitorVehicles);
        this.btnMyMonitorSessions = findViewById(R.id.btnMyMonitorSessions);
        this.btnProfile = findViewById(R.id.btnProfile);
        this.btnSignout = findViewById(R.id.btnSignout);

        this.btnMonitorVehicles.setOnClickListener(this);
        this.btnMyMonitorSessions.setOnClickListener(this);
        this.btnProfile.setOnClickListener(this);
        this.btnSignout.setOnClickListener(this);

        this.email = this.getIntent().getStringExtra("email");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GetProfile getProfile = new GetProfile();
                getProfile.execute(email);
            }
        });
        thread.start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnMonitorVehicles:
                intent = new Intent(this, ViewMonitorVehicles.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnMyMonitorSessions:
                intent = new Intent(this, ViewMonitorSeances.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnProfile:
                intent = new Intent(this, Profile.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnSignout:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;
        }
    }

    public class GetProfile extends AsyncTask<String, Void, Users> {

        @Override
        protected void onPostExecute(Users user) {
            if (user == null) {
                System.out.println("onPostExecute PROFILE => null");
            } else {
                System.out.println("onPostExecute PROFILE => ok");
            }
            setLoggedInUser(user);
        }

        @Override
        protected Users doInBackground(String... strings) {
            Users user = null;
            String email = strings[0];
            String uri = Constant.SERVER + "my_profile.php?email=" + email;
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
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject object = jsonArray.getJSONObject(0);
                user = new Users(
                        object.getString("iduser"),
                        object.getString("lastname"),
                        object.getString("firstname"),
                        object.getString("age"),
                        object.getString("phone"),
                        object.getString("email"),
                        object.getString("password")
                );
            } catch (JSONException e) {
                Log.e("Erreur de json", e.toString());
                System.out.println("Erreur de json"+e.toString());
            }

            return user;
        }
    }
}
