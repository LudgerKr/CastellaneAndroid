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

public class ViewAdminVehicles extends AppCompatActivity implements View.OnClickListener {

    private Button btnBackViewvehicles, btnAddVehicles, btnModifyVehicles, btnDeleteVehicles;
    private Spinner spVehicleID;
    private ListView listvehicles;
    private Vehicles vehicle = null;
    private String email, userId;

    private ArrayList<Vehicles> vehicles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_vehicles_view);

        this.spVehicleID = findViewById(R.id.spVehicleID);

        this.btnBackViewvehicles = findViewById(R.id.btnBackViewVehicles);
        this.btnAddVehicles = findViewById(R.id.btnAddVehicles);
        this.btnModifyVehicles = findViewById(R.id.btnModifyVehicles);
        this.btnDeleteVehicles = findViewById(R.id.btnDeleteVehicles);

        this.btnBackViewvehicles.setOnClickListener(this);
        this.btnAddVehicles.setOnClickListener(this);
        this.btnModifyVehicles.setOnClickListener(this);
        this.btnDeleteVehicles.setOnClickListener(this);

        this.listvehicles = findViewById(R.id.listVehicles);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBvehicles DBvehicles = new DBvehicles();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBvehicles.execute();
                    }
                });
            }
        });
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                final DBvehiclesLists DBvehiclesLists = new DBvehiclesLists();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBvehiclesLists.execute();
                    }
                });
            }
        });
        thread2.start();

    }

    @Override
    public void onClick(View v) {
        String unVehicle = this.spVehicleID.getSelectedItem().toString();
        if (v.getId() == R.id.btnBackViewVehicles) {
            Intent intent = new Intent(this, MenuAdministrateur.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnAddVehicles) {
            Intent intent = new Intent(this, AddVehicles.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnModifyVehicles) {
            Intent intent = new Intent(this, ModifyVehicles.class);
            intent.putExtra("vehicle", unVehicle);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnDeleteVehicles) {
            Intent intent  = new Intent(this, ViewAdminVehicles.class);
            intent.putExtra("vehicle", unVehicle);
            new VehicleDelete(this).execute(unVehicle);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        }
    }

    public ArrayList<Vehicles> getvehicles() {
        return vehicles;
    }

    public void remplirLV() {
        if (vehicles != null) {
            System.out.println("if");
            ArrayList<String> vehiclesString = new ArrayList<>();
            for (Vehicles vehicle : vehicles) {
                vehiclesString.add(vehicle.toString());
                System.out.println("vehicle => "+vehicle.toString());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vehiclesString);
            listvehicles.setAdapter(adapter);
        } else {
            System.out.println("no if");
        }
    }

    public void setvehicles(ArrayList<Vehicles> vehicles) {
        this.vehicles = vehicles;
        remplirLV();
    }

    class DBvehicles extends AsyncTask<Void, Void, ArrayList<Vehicles>> {

        @Override
        protected void onPostExecute(ArrayList<Vehicles> vehicles) {
            setvehicles(vehicles);
        }

        @Override
        protected ArrayList<Vehicles> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_vehicles.php";
            ArrayList<Vehicles> vehicles = new ArrayList<Vehicles>();
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
                    Vehicles vehicle = new Vehicles(unObjet.getString("idvehicles"),
                            unObjet.getString("licenseplate"),
                            unObjet.getString("mileage"),
                            unObjet.getString("brand"),
                            unObjet.getString("status"));
                    vehicles.add(vehicle);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return vehicles;
        }
    }

    public void remplirSP() {
        if (vehicles != null) {
            System.out.println("ifV");
            ArrayList<String> vehiclesString = new ArrayList<>();
            for (Vehicles vehicle : vehicles) {
                vehiclesString.add(vehicle.getlicenseplate());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vehiclesString);
            spVehicleID.setAdapter(adapter);
        } else {
            System.out.println("no if");
        }
    }

    public void setvehicleLists(ArrayList<Vehicles> vehicles) {
        this.vehicles = vehicles;
        remplirSP();
    }

    class DBvehiclesLists extends AsyncTask<Void, Void, ArrayList<Vehicles>> {

        @Override
        protected void onPostExecute(ArrayList<Vehicles> vehicles) {
            setvehicleLists(vehicles);
        }

        @Override
        protected ArrayList<Vehicles> doInBackground(Void... voids) {
            String url = Constant.SERVER + "get_Unevehicles.php";
            ArrayList<Vehicles> vehicles = new ArrayList<Vehicles>();
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
                    Vehicles vehicle = new Vehicles(unObjet.getString("licenseplate"));
                    vehicles.add(vehicle);
                }

            } catch (JSONException e) {
                Log.e("Erreur de JSON", resultat);
            }
            return vehicles;
        }
    }


    class VehicleDelete extends AsyncTask<String, Void, Void> {
        private WeakReference<ViewAdminVehicles> activityReference;

        VehicleDelete(ViewAdminVehicles context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(String... seances) {
            String unVehicle = spVehicleID.getSelectedItem().toString();
            String uri = Constant.SERVER + "deleteVehicles.php?licenseplate=" + unVehicle;
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
            ViewAdminVehicles vehicleActivity = activityReference.get();
            if (vehicleActivity == null || vehicleActivity.isFinishing()) return ;
            Toast.makeText(vehicleActivity, "Véhicule supprimé", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(vehicleActivity, ViewAdminVehicles.class);
            vehicleActivity.startActivity(intent);
        }
    }
}