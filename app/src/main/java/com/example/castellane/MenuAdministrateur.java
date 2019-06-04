package com.example.castellane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuAdministrateur extends AppCompatActivity implements View.OnClickListener {

    private Button btnSeance, btnUserList, btnAnnonce, btnVehicle, btnProfile, btnSignout;
    private String email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_administrateur);

        this.btnSeance = findViewById(R.id.btnSeance);
        this.btnUserList = findViewById(R.id.btnUserList);
        this.btnAnnonce = findViewById(R.id.btnAnnonce);
        this.btnVehicle = findViewById(R.id.btnVehicle);
        this.btnProfile = findViewById(R.id.btnProfile);
        this.btnSignout = findViewById(R.id.btnSignout);

        this.btnSeance.setOnClickListener(this);
        this.btnUserList.setOnClickListener(this);
        this.btnAnnonce.setOnClickListener(this);
        this.btnVehicle.setOnClickListener(this);
        this.btnProfile.setOnClickListener(this);
        this.btnSignout.setOnClickListener(this);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnSeance:
                intent = new Intent(this, ViewAdminSeances.class);
                intent.putExtra("email", this.email);
                System.out.println(this.email);
                System.out.println(this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnUserList:
                intent = new Intent(this, ViewAdminUsersMenu.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnAnnonce:
                intent = new Intent(this, ViewAdminAnnonces.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnVehicle:
                intent = new Intent(this, ViewAdminVehicles.class);
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
}
