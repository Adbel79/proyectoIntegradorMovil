package com.example.appfitnutritionmovilvp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import com.example.appfitnutritionmovilvp.poko.Medico
import com.koushikdutta.ion.Ion
import com.example.appfitnutritionmovilvp.util.constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var spMedico: Spinner
    lateinit var tvNombre: TextView
    lateinit var imgFoto: ImageView
    var listaMedicos = ArrayList<Medico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spMedico = findViewById(R.id.spMedicos)
        tvNombre = findViewById(R.id.tvNombreMedico)
        imgFoto = findViewById(R.id.ivFoto)
        descargarListaMedicos()
        }
    private fun descargarListaMedicos(){
        Ion.with(this@MainActivity)
            .load("GET", constantes.URL_WS+"medicos/all")
            .asString()
            .setCallback{e, result->
                if(e !=null){
                   mostrarAlerta(e.message!!)
                }else{
                    convertirJson(result)
                }
            }
    }

    private fun descargarFotoMedico(idMedico: Int){
        Ion.with(this@MainActivity)
            .load("GET", constantes.URL_WS+"medicos/obtenerFoto/${idMedico}")
            .asString()
            .setCallback{
                    e, result->
                if(e != null){
                    mostrarAlerta(e.message!!)
                }else{
                    //temporal
                mostrarInformacionMedico(result)
                }
            }
    }

    private fun convertirJson(respuestaJson:String){
        val gson = Gson()
        val typeMedicos = object : TypeToken<ArrayList<Medico>>(){}.type
        listaMedicos = gson.fromJson(respuestaJson, typeMedicos)
        cargarInformacionSpinner()
    }

    private fun cargarInformacionSpinner(){
        val adaptadorSpinner = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item,listaMedicos)
        spMedico.adapter = adaptadorSpinner
        spMedico.onItemSelectedListener = this
    }


    private fun mostrarInformacionMedico(informacion: String){
        val gson = Gson()
        val infoMedico = gson.fromJson(informacion, Medico::class.java)
        tvNombre.text = infoMedico.nombre + " " + infoMedico.apellidoPaterno + " " + infoMedico.apellidoMaterno;
        val imgByte = Base64.decode(infoMedico.foto, Base64.DEFAULT)
        val imgBitmap = BitmapFactory.decodeByteArray(imgByte,0, imgByte.size)
        imgFoto.setImageBitmap(imgBitmap)
    }

    private fun mostrarAlerta(mensaje: String){
        Toast.makeText(this@MainActivity, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
       val idMedicoSeleccionado = listaMedicos.get(p2).idMedico
        descargarFotoMedico(idMedicoSeleccionado)

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}