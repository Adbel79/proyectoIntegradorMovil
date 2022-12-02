package com.example.appfitnutritionmovilvp.poko

class Medico {
    var idMedico: Int =0
    var nombre: String = ""
    var apellidoPaterno =""
    var apellidoMaterno =""
    var foto :String = ""

    override fun toString(): String {
        return "- "+nombre
    }
}