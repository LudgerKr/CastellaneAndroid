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

public class ModifyMonitorVehicles extends AppCompatActivity implements View.OnClickListener {

    private EditText editUpdateMileage;
    private Spinner spUpdateStatus;
    private Button btnUpdateVehicle, btnBackUpdateVehicle;
    private String email, licenceplate, userId;
    private Vehicles vehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_vehicles_update);

        this.btnUpdateVehicle = findViewById(R.id.btnUpdateVehicle);
        this.btnBackUpdateVehicle = findViewById(R.id.btnBackUpdateVehicle);

        this.btnUpdateVehicle.setOnClickListener(this);
        this.btnBackUpdateVehicle.setOnClickListener(this);

        this.editUpdateMileage = findViewById(R.id.editUpdateMileage);

        this.spUpdateStatus = findViewById(R.id.spUpdateStatus);

        final List<String> status = new ArrayList<String>();
        status.add("Disponible");
        status.add("Occupée");
        status.add("Réparation");
        status.add("Détruite");

        ArrayAdapter<String> unAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, status);
        this.spUpdateStatus.setAdapter(unAdapter);
        unAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spUpdateStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ModifyMonitorVehicles.this, "Selected : "+ status.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

        this.licenceplate = this.getIntent().getStringExtra("vehicle");
        new GetVehicle(this).execute(this.licenceplate);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackUpdateVehicle) {
            Intent intent = new Intent(this, ViewMonitorVehicles.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnUpdateVehicle) {
            final String mileage, status;
            mileage = this.editUpdateMileage.getText().toString();
            status = this.spUpdateStatus.getSelectedItem().toString();
            if (mileage.length() == 0) {
                Toast.makeText(this, "Le kilométrage est obligatoire", Toast.LENGTH_LONG).show();
            } else {
                this.vehicle.setMileage(mileage);
                this.vehicle.setStatus(status);
                System.out.println(status + "TOTO");
                new UpdateVehicle(this).execute(this.vehicle);
                Intent intent = new Intent(this, ViewMonitorVehicles.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
            }
        }
    }

    class GetVehicle extends AsyncTask<String, Void, Vehicles> {
        private WeakReference<ModifyMonitorVehicles> activityReference;

        GetVehicle(ModifyMonitorVehicles context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Vehicles doInBackground(String... licenseplates) {
            String licenseplate = licenseplates[0];
            String apiURL = Constant.SERVER + "my_vehicles.php?licenseplate=" +licenseplate;
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
                vehicle = new Vehicles(
                        object.getString("idvehicles"),
                        object.getString("licenseplate"),
                        object.getString("mileage"),
                        object.getString("brand"),
                        object.getString("status"));
            } catch (JSONException e) {
                Log.e("Erreur de json", e.toString());
                System.out.println("Erreur de json"+e.toString());
            }
            System.out.println(apiURL + " URL");
            return vehicle;
        }

        @Override
        protected void onPostExecute(Vehicles vehicle) {
            ModifyMonitorVehicles vehicleActivity = activityReference.get();
            if (vehicle != null) {
                vehicleActivity.vehicle = vehicle;
                vehicleActivity.editUpdateMileage.setText(vehicle.getMileage());
                vehicleActivity.spUpdateStatus.getSelectedItemId();
            } else {
                System.out.println("vehicle vide");
            }
        }
    }

    class UpdateVehicle extends AsyncTask<Vehicles, Void, Void> {
        private WeakReference<ModifyMonitorVehicles> activityReference;

        UpdateVehicle(ModifyMonitorVehicles context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Vehicles... vehicles) {
            Vehicles vehicleToUpdate = vehicles[0];
            String apiURL = Constant.SERVER + "update_vehicles.php";
            String result = "";
            int status = 0;
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
                if (spUpdateStatus.getSelectedItem().toString().equals("Disponible")) {
                        status = 1;
                } else if (spUpdateStatus.equals("Détruite")) {
                    status = 2;
                } else if (spUpdateStatus.getSelectedItem().toString().equals("Réparation")) {
                    status = 3;
                } else {
                    status = 4;
                }

                String parameters = "mileage=" + vehicleToUpdate.getMileage() + "&status=" + spUpdateStatus + "&licenseplate=" + vehicleToUpdate.getlicenseplate();
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
            ModifyMonitorVehicles modifyvehicle = activityReference.get();
            if (modifyvehicle == null || modifyvehicle.isFinishing()) return ;
            Toast.makeText(modifyvehicle, "Vehicle mis à jour", Toast.LENGTH_LONG).show();
        }
    }
}
