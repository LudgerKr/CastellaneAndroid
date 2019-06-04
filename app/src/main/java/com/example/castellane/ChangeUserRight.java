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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChangeUserRight extends AppCompatActivity implements View.OnClickListener {

    private Button btnChangeUserRight, btnBackChangeUserRight;
    private Spinner spChangeUserRight, spUserEmail;
    private String email;
    private UsersList usersList = null;
    private String userId;

    private ArrayList<UsersList> usersLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_right);


        this.btnChangeUserRight = findViewById(R.id.btnChangeUserRight);
        this.btnBackChangeUserRight = findViewById(R.id.btnBackChangeUserRight);

        this.btnChangeUserRight.setOnClickListener(this);
        this.btnBackChangeUserRight.setOnClickListener(this);

        this.spUserEmail = findViewById(R.id.spUserEmail);

        this.spChangeUserRight = findViewById(R.id.spChangeUserRight);

        this.email = this.getIntent().getStringExtra("email");

        final List<String> right = new ArrayList<String>();
        right.add("Utilisateur");
        right.add("Moniteur");
        right.add("Administrateur");

        ArrayAdapter<String> unAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, right);

        this.spChangeUserRight.setAdapter(unAdapter2);
        unAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spChangeUserRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ChangeUserRight.this, "Selected : "+ right.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBusersLists DBusersLists = new DBusersLists();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBusersLists.execute();
                    }
                });
            }
        });
        thread.start();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChangeUserRight) {
            final Users updatedUser;
            String Useremail = this.spUserEmail.getSelectedItem().toString();
            String right = this.spChangeUserRight.getSelectedItem().toString();
            updatedUser = new Users(Useremail, right);
            Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateProfile updateProfile = new UpdateProfile();
                        updateProfile.execute(updatedUser);
                    }
                });
                thread.start();
                Toast.makeText(ChangeUserRight.this, "L'utilisateur " +Useremail+ " est " + right,
                    Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ViewAdminUsersMenu.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this, ViewAdminUsersMenu.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        }
    }

    public class UpdateProfile extends AsyncTask<Users, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
        }

        @Override
        protected Void doInBackground(Users... users) {
            Users user = users[0];
            String uri = Constant.SERVER + "update_usersRight.php?email=" + user.getEmail() +
                    "&right=" + user.getRight();
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

    public void remplirSP() {
        if (usersLists != null) {
            System.out.println("if");
            ArrayList<String> usersListsString = new ArrayList<>();
            for (UsersList usersList : usersLists) {
                usersListsString.add(usersList.getEmail());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usersListsString);
            spUserEmail.setAdapter(adapter);
        } else {
            System.out.println("no if");
        }
    }

    public void setusersLists(ArrayList<UsersList> usersLists) {
        this.usersLists = usersLists;
        remplirSP();
    }

    class DBusersLists extends AsyncTask<Void, Void, ArrayList<UsersList>> {

        @Override
        protected void onPostExecute(ArrayList<UsersList> usersLists) {
            setusersLists(usersLists);
        }

        @Override
        protected ArrayList<UsersList> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_allUser.php";
            ArrayList<UsersList> usersLists = new ArrayList<UsersList>();
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
                    UsersList usersList = new UsersList(unObjet.getString("email"));
                    usersLists.add(usersList);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return usersLists;
        }
    }
}