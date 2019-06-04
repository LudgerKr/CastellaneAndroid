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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddVehicles extends AppCompatActivity implements View.OnClickListener {

    private EditText editAddLicensePlate, editAddMileage, editAddBrand;
    private Spinner spAddStatus;
    private Button btnAddVehicle, btnBackAddVehicle;
    private String email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_vehicles_add);

        this.btnAddVehicle = findViewById(R.id.btnAddVehicle);
        this.btnBackAddVehicle = findViewById(R.id.btnBackAddVehicle);

        this.btnAddVehicle.setOnClickListener(this);
        this.btnBackAddVehicle.setOnClickListener(this);

        this.editAddLicensePlate = findViewById(R.id.editAddLicensePlate);
        this.editAddMileage = findViewById(R.id.editAddMileage);
        this.editAddBrand = findViewById(R.id.editAddBrand);

        this.spAddStatus = findViewById(R.id.spAddStatus);

        final List<String> status = new ArrayList<String>();
        status.add("Disponible");
        status.add("Occupée");
        status.add("Réparation");
        status.add("Détruite");

        ArrayAdapter<String> unAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, status);
        this.spAddStatus.setAdapter(unAdapter);
        unAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spAddStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(AddVehicles.this, "Selected : "+ status.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBackAddVehicle) {
            Intent intent = new Intent(this, ViewAdminVehicles.class);
            intent.putExtra("email", this.email);
            intent.putExtra("userId", this.userId);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btnAddVehicle) {
            String licenseplate, brand, mileage, status;
            licenseplate = this.editAddLicensePlate.getText().toString();
            mileage = this.editAddMileage.getText().toString();
            brand = this.editAddBrand.getText().toString();
            status = this.spAddStatus.getSelectedItem().toString();

            if (licenseplate.length() == 0) {
                Toast.makeText(this, "Le plaque d'immatriculation est obligatoire", Toast.LENGTH_LONG).show();
            } else if (mileage.length() == 0) {
                Toast.makeText(this, "Le kilométrage est obligatoire", Toast.LENGTH_LONG).show();
            } else if (brand.length() == 0) {
                Toast.makeText(this, "La marque est obligatoire", Toast.LENGTH_LONG).show();
            } else {
                Vehicles vehicle = new Vehicles(licenseplate, mileage, brand, status);
                new NewVehicle(this).execute(vehicle);
                Intent intent = new Intent(this, ViewAdminVehicles.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
            }
        }
    }

    class NewVehicle extends AsyncTask<Vehicles, Void, Void> {
        private WeakReference<AddVehicles> activityReference;

        NewVehicle(AddVehicles context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Vehicles... vehicles) {
            Vehicles vehicleToAdd = vehicles[0];
            String apiURL = Constant.SERVER + "add_vehicles.php";
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
                String parameters = "licenseplate=" + vehicleToAdd.getlicenseplate() + "&mileage=" + vehicleToAdd.getMileage() + "&brand=" + vehicleToAdd.getBrand() + "&status=" + vehicleToAdd.getStatus();
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
            AddVehicles addAnnonce = activityReference.get();
            if (addAnnonce == null || addAnnonce.isFinishing()) return ;
            Toast.makeText(addAnnonce, "Votre véhicule a été ajouté", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(addAnnonce, ViewAdminVehicles.class);
            addAnnonce.startActivity(intent);
        }
    }
}
