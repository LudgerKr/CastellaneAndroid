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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private EditText editFirstname, editLastname, editEmail, editAge, editPhone, editPassword;
    private Button btnSave;
    private String email;
    private Users loggedInUser = null;
    private String idUser, userId;

    public void fillInputs() {
        this.editFirstname.setText(loggedInUser.getFirstname());
        this.editLastname.setText(loggedInUser.getLastname());
        this.editEmail.setText(loggedInUser.getEmail());
        this.idUser = loggedInUser.getIduser();
        this.editAge.setText(loggedInUser.getAge());
        this.editPhone.setText(loggedInUser.getPhone());
        this.editPassword.setText(loggedInUser.getPassword());
    }

    public void setLoggedInUser(Users loggedInUser) {
        this.loggedInUser = loggedInUser;
        fillInputs();
    }

    public void setSaveText() {
        Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_LONG).show();
        this.email = this.editEmail.getText().toString();
        this.editPassword.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.editFirstname = findViewById(R.id.editFirstName);
        this.editLastname = findViewById(R.id.editLastName);
        this.editEmail = findViewById(R.id.editUserEmail);
        this.editAge = findViewById(R.id.editAge);
        this.editPhone = findViewById(R.id.editPhone);
        this.editPassword = findViewById(R.id.editPassword);

        this.btnSave = findViewById(R.id.btnSave);

        this.btnSave.setOnClickListener(this);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

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
        if (v.getId() == R.id.btnSave) {
            final Users updatedUser;
            String firstname = this.editFirstname.getText().toString();
            String lastname = this.editLastname.getText().toString();
            String email = this.editEmail.getText().toString();
            String age = this.editAge.getText().toString();
            String phone = this.editPhone.getText().toString();
            String password = this.editPassword.getText().toString();

            if (firstname.length() == 0 || lastname.length() == 0 || email.length() == 0) {
                Toast.makeText(this, "Tous les champs sont obligatoires à l'exception du mot de passe", Toast.LENGTH_LONG).show();
            } else {
                if (password.length() == 0) {
                    updatedUser = new Users(this.idUser, lastname, firstname, age, phone, email);
                } else {
                    updatedUser = new Users(this.idUser, lastname, firstname, age, phone, email, password);
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateProfile updateProfile = new UpdateProfile();
                        updateProfile.execute(updatedUser);
                    }
                });
                thread.start();
            }

            Intent intent = new Intent(this, MenuUtilisateur.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
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

    public class UpdateProfile extends AsyncTask<Users, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            setSaveText();
        }

        @Override
        protected Void doInBackground(Users... users) {
            Users user = users[0];
            String uri = Constant.SERVER + "update_profile.php?iduser=" + user.getIduser() +
                    "&lastname=" + user.getLastname() + "&firstname=" + user.getFirstname() +
                    "&age=" + user.getAge() + "&phone=" + user.getPhone() + "&email=" + user.getEmail() +
                    "&password=" + user.getPassword();
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
    }
}