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

public class ViewAdminUsersMenu extends AppCompatActivity implements View.OnClickListener {

    private Button btnBackViewAdmin, btnUser, btnMonitor, btnAdmin;
    private String email, userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_view);

        this.btnUser = findViewById(R.id.btnUser);
        this.btnMonitor = findViewById(R.id.btnMonitor);
        this.btnAdmin = findViewById(R.id.btnAdmin);
        this.btnBackViewAdmin = findViewById(R.id.btnBackViewAdmin);

        this.btnUser.setOnClickListener(this);
        this.btnMonitor.setOnClickListener(this);
        this.btnAdmin.setOnClickListener(this);

        this.btnBackViewAdmin.setOnClickListener(this);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnUser:
                intent = new Intent(this, ViewAdminUserList.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnMonitor:
                intent = new Intent(this, ViewAdminMonitorList.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnAdmin:
                intent = new Intent(this, ViewAdminAdminList.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnBackViewAdmin:
                intent = new Intent(this, MenuAdministrateur.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;
        }
    }
}