package com.example.castellane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ShopMenu extends AppCompatActivity implements View.OnClickListener {

    private Button btnShops1, btnShops2, btnShops3, btnBackShopMenu;
    private String email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_menu);

        this.btnShops1 = findViewById(R.id.btnShops1);
        this.btnShops2 = findViewById(R.id.btnShops2);
        this.btnShops3 = findViewById(R.id.btnShops3);
        this.btnBackShopMenu = findViewById(R.id.btnBackShopMenu);

        this.btnShops1.setOnClickListener(this);
        this.btnShops2.setOnClickListener(this);
        this.btnShops3.setOnClickListener(this);

        this.btnBackShopMenu.setOnClickListener(this);

        this.email = this.getIntent().getStringExtra("email");
        this.userId = this.getIntent().getStringExtra("userId");

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnShops1:
                intent = new Intent(this, ViewShops1.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnShops2:
                intent = new Intent(this, ViewShops2.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnShops3:
                intent = new Intent(this, ViewShops3.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;

            case R.id.btnBackShopMenu:
                intent = new Intent(this, MenuUtilisateur.class);
                intent.putExtra("email", this.email);
                intent.putExtra("userId", this.userId);
                this.startActivity(intent);
                break;
        }
    }
}
