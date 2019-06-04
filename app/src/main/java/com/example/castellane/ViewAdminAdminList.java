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

public class ViewAdminAdminList extends AppCompatActivity implements View.OnClickListener {

    private Button btnBackViewRightAdmin, btnModifyRightAdmin;
    private ListView listAdminList;
    private UsersList usersList = null;
    private String email, userId;

    private ArrayList<UsersList> usersLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rightadmin_view);

        this.btnBackViewRightAdmin = findViewById(R.id.btnBackViewRightAdmin);
        this.btnModifyRightAdmin = findViewById(R.id.btnModifyRightAdmin);

        this.btnBackViewRightAdmin.setOnClickListener(this);
        this.btnModifyRightAdmin.setOnClickListener(this);

        this.listAdminList = findViewById(R.id.listAdmin);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");


        final ViewAdminAdminList sp = this;

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
        if (v.getId() == R.id.btnModifyRightAdmin) {
            Intent intent = new Intent(this, ViewAdminUsersMenu.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnBackViewRightAdmin) {
            Intent intent = new Intent(
                    this, ViewAdminUsersMenu.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        }
    }

    public ArrayList<UsersList> getusersLists() {
        return usersLists;
    }

    public void remplirLV() {
        if (usersLists != null) {
            System.out.println("if");
            ArrayList<String> usersListsString = new ArrayList<>();
            for (UsersList usersList : usersLists) {
                usersListsString.add(usersList.toString());
                System.out.println("usersList => "+usersList.toString());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usersListsString);
            listAdminList.setAdapter(adapter);
        } else {
            System.out.println("no if");
        }
    }

    public void setusersLists(ArrayList<UsersList> usersLists) {
        this.usersLists = usersLists;
        remplirLV();
    }

    class DBusersLists extends AsyncTask<Void, Void, ArrayList<UsersList>> {

        @Override
        protected void onPostExecute(ArrayList<UsersList> usersLists) {
            setusersLists(usersLists);
        }

        @Override
        protected ArrayList<UsersList> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_allAdmin.php";
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
                    UsersList usersList = new UsersList(unObjet.getInt("iduser"),
                            unObjet.getString("lastname"),
                            unObjet.getString("firstname"),
                            unObjet.getString("age"),
                            unObjet.getString("phone"),
                            unObjet.getString("email"),
                            unObjet.getString("right"));
                    usersLists.add(usersList);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return usersLists;
        }
    }
}