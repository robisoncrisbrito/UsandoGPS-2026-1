package br.edu.utfpr.usandogps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.UserManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvLatitude = findViewById( R.id.tvLatitude )
        tvLongitude = findViewById( R.id.tvLongitude )

        tvLatitude.text = "-26.0751195"
        tvLongitude.text = "-53.0613228"

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)



    }

    override fun onLocationChanged(location: Location) {

        tvLatitude.text = "-26.0751195"
        tvLongitude.text = "-53.0613228"

    }

    fun btVerEnderecoOnClick(view: View) {

        Thread {

            val endereco = "https://maps.googleapis.com/maps/api/geocode/xml?latlng=${tvLatitude.text},${tvLongitude.text}&key=AIzaSyDsy454kAkXofX828BEMieAQ7EbtpjohZY"

            val url = URL(endereco)
            val urlConnection = url.openConnection()

            val inputStream = urlConnection.getInputStream()
            val entrada = BufferedReader(InputStreamReader(inputStream))

            val saida = StringBuilder()

            var linha = entrada.readLine()

            while (  linha != null ) {
                saida.append(linha)
                linha = entrada.readLine()
            }

            runOnUiThread {

                val local = saida.substring(
                    saida.indexOf( "<formatted_address>")+19,
                    saida.indexOf( "</formatted_address>")
                )

                Toast.makeText(
                    this,
                    local,
                    Toast.LENGTH_LONG
                ).show()

                println( local.toString() )
            }

        }.start()

    }

    fun btVerMapaOnClick(view: View) {
        Thread {

            val endereco = "https://maps.googleapis.com/maps/api/staticmap?center=${tvLatitude.text},${tvLongitude.text}&zoom=18&size=400x400&key=AIzaSyDsy454kAkXofX828BEMieAQ7EbtpjohZY"

            val url = URL(endereco)
            val urlConnection = url.openConnection()

            val inputStream = urlConnection.getInputStream()

            val imagem = BitmapFactory.decodeStream(inputStream)

            runOnUiThread {

                val tvMapa = findViewById<ImageView>(R.id.ivMapa)
                tvMapa.setImageBitmap(imagem)

            }

        }.start()


    }
}