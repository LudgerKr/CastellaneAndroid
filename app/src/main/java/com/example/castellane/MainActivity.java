package com.example.castellane;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editPassword;
    private Button buttonCancel, buttonSignin;
    private Spinner spRight;
    private static Users unUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editEmail = findViewById(R.id.editUserEmail);
        this.editPassword = findViewById(R.id.editPassword);

        this.spRight = findViewById(R.id.spRight);

        final List<String> right = new ArrayList<String>();
        right.add("Utilisateur");
        right.add("Moniteur");
        right.add("Administrateur");

        ArrayAdapter<String> unAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, right);
        this.spRight.setAdapter(unAdapter);
        unAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "Selected : "+ right.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        this.buttonCancel = findViewById(R.id.buttonCancel);
        this.buttonSignin = findViewById(R.id.buttonSignin);

        this.buttonCancel.setOnClickListener(this);
        this.buttonSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String email, password;
        final MainActivity ma = this;

        if (v.getId() == R.id.buttonCancel) {
            this.editEmail.setText("");
            this.editPassword.setText("");
        } else if (v.getId() == R.id.buttonSignin) {
            email = this.editEmail.getText().toString();
            password = this.editPassword.getText().toString();

            if (email.length() == 0) {
                Toast.makeText(this, "Veuillez saisir votre adresse mail",
                        Toast.LENGTH_LONG).show();
            } else if (password.length() == 0) {
                Toast.makeText(this, "Veuillez saisir mot de passe",
                        Toast.LENGTH_LONG).show();
            } else {
                final Users user = new Users(email, password);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Connexion connexion = new Connexion();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                connexion.execute(user);

                                if (user == null) {
                                    Toast.makeText(ma, "Veuillez saisir mot de passe",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ma, "Bienvenue " + user.getFirstname(),
                                            Toast.LENGTH_LONG).show();
                                    if (spRight.getSelectedItem().equals("Utilisateur")) {
                                        Intent intent = new Intent(ma, MenuUtilisateur.class);
                                        intent.putExtra("email", user.getEmail());
                                        intent.putExtra("userId", user.getIduser());
                                        System.out.println(user.getIduser() + " ZIZI ID");
                                        System.out.println(user.getEmail() + " ZIZI EMAIL");
                                        ma.startActivity(intent);
                                    } else if (spRight.getSelectedItem().equals("Moniteur")) {
                                        Intent intent = new Intent(ma, MenuMoniteur.class);
                                        intent.putExtra("email", user.getEmail());
                                        intent.putExtra("userId", user.getIduser());
                                        ma.startActivity(intent);
                                    } else if (spRight.getSelectedItem().equals("Administrateur")) {
                                        Intent intent = new Intent(ma, MenuAdministrateur.class);
                                        intent.putExtra("email", user.getEmail());
                                        intent.putExtra("userId", user.getIduser());
                                        ma.startActivity(intent);
                                    }
                                }
                            }
                        });

                    }
                });
                thread.start();
            }
        }
    }

    public static Users getUnUser() {
        return MainActivity.unUser;
    }

    public static void setUnUser(Users unUser) {
        MainActivity.unUser = unUser;
    }

}

class Connexion extends AsyncTask<Users, Void, Users> {

    @Override
    protected Users doInBackground(Users... users) {
        Users user = null;
        Users userconnect = users[0];
        String apiUrl = Constant.SERVER + "check_connexion.php";
        String result = "";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection httpConnexion = (HttpURLConnection)url.openConnection();
            httpConnexion.setRequestMethod("POST");
            httpConnexion.setReadTimeout(15000);
            httpConnexion.setConnectTimeout(20000);
            httpConnexion.setDoInput(true);
            httpConnexion.setDoOutput(true);
            httpConnexion.connect();
            OutputStream os = httpConnexion.getOutputStream();
            String parameters = "?email=" + userconnect.getEmail() + "&password=" + userconnect.getPassword();
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
            Log.e("Ligne lue", result);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Erreur de connexion", "Erreur de connexion");
        }

        try {
            if (!result.equals("")) {
                JSONArray tabJson = new JSONArray(result);
                JSONObject objectJson = tabJson.getJSONObject(0);
                int nb = objectJson.getInt("nb");
                if (nb != 0) {
                    String iduser = objectJson.getString("iduser");
                    String lastname = objectJson.getString("lastname");
                    String firstname = objectJson.getString("firstname");
                    user = new Users(userconnect.getEmail(), userconnect.getPassword(), lastname, firstname);
                }
            }
        } catch (JSONException e) {
            Log.e("Erreur", "Impossible de parser le JSON : " + result);

        }

        return user;
    }
}