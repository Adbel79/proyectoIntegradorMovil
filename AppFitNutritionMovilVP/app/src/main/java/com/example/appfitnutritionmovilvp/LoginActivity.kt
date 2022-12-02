package com.example.appfitnutritionmovilvp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.appfitnutritionmovilvp.poko.RespuestaLogin
import com.example.appfitnutritionmovilvp.util.constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsuario : EditText
    private lateinit var etContrasenia : EditText




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Iniciar Sesion"
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion);
        etUsuario = findViewById(R.id.etUsuario)
        etContrasenia = findViewById(R.id.etPassword)
        btnIniciarSesion.setOnClickListener {
            //Toast.makeText(this@MainActivity, "Clic en boton", Toast.LENGTH_LONG).show()
            validarCampos()
        }

    }
    private fun validarCampos(){
        val usuarioTxt = etUsuario.text.toString()
        val passwordTxt = etContrasenia.text.toString()
        var valido = true

        if(usuarioTxt.isEmpty()){
            valido = false
            etUsuario.setError("Campo usuario requerido")
        }
        if(passwordTxt.isEmpty()){
            valido = false
            etContrasenia.setError("Campo contraseña requerido")
        }
        if(valido){
            enviarCredenciales(usuarioTxt,passwordTxt)
        }
    }

    private fun enviarCredenciales(usuario : String, password: String){
        Ion.getDefault(this@LoginActivity).conscryptMiddleware.enable(false)
        //implementacion de consumo
        Ion.with(this@LoginActivity)
            .load("POST",constantes.URL_WS+"acceso/movil")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("usuario",usuario)
            .setBodyParameter("password",password)
            .asString()
            .setCallback { e, result ->
                if(e!=null){
                    //hubo error
                        e.printStackTrace()
                    mostrarAlerta("Error en la conexion, intente mas tarde")
                }else{
                    //ok - 200
                    validarResultadoPeticion(result)
                }
            }
    }

    private fun validarResultadoPeticion(respuesta: String){
        if(respuesta == null || respuesta.isEmpty()){
            mostrarAlerta("Por el momento el servicio no está disponible")
        }else{
            val gson = Gson()
            val respuestaWS = gson.fromJson(respuesta, RespuestaLogin::class.java)
            if(!respuestaWS.error!!){
                val nombreCompleto = "Bienvenido(a) " + respuestaWS.nombre+ " " + respuestaWS.apellidoPaterno + " " + respuestaWS.apellidoMaterno
                mostrarAlerta(nombreCompleto)
                val irPrincipal = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(irPrincipal)
                finish()
            }else{
               mostrarAlerta(respuestaWS.mensaje)
            }
        }
    }

    private fun mostrarAlerta(mensaje: String){
        Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_LONG).show()
    }

}
