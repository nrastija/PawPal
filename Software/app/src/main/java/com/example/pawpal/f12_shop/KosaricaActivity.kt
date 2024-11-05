package com.example.pawpal.f12_shop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.f12_shop.entiteti.Proizvod
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class KosaricaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_kosarica)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        dohvatiProizvodeKosarice();
    }

    private fun dohvatiProizvodeKosarice(): List<Proizvod>{
        return listOf(
            Proizvod(1,"Darling",6.31,"Test",1,null),
            Proizvod(1,"Sok",51.31,"Test2",2,null),
            Proizvod(1,"Hrana",0.31,"Test3",1,null),
        )
    }
}