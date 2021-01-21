package org.luciano.rebrotelog

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // Activity:
    private var TAG = "RebroteTAG"
    private lateinit var emisorSpinner: Spinner
    private lateinit var montoEdit: EditText
    private lateinit var receptorSpinner: Spinner
    private lateinit var categoriaSpinner: Spinner
    private lateinit var motivoEdit: EditText
    private lateinit var registrarButton: Button

    // Dynamic Variables:
    private var currentMonto: Int = 0
    private lateinit var currentEmisor: String
    private lateinit var currentReceptor: String
    private lateinit var currentCategoria: String
    private lateinit var currentMotivo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initSpinners()
        initEditTexts()
        initButtons()
    }

    private fun findViews() {
        emisorSpinner = findViewById(R.id.emisor_spinner)
        montoEdit = findViewById(R.id.monto_edit)
        receptorSpinner = findViewById(R.id.receptor_spinner)
        categoriaSpinner = findViewById(R.id.categoria_spinner)
        motivoEdit = findViewById(R.id.motivo_edit)
        registrarButton = findViewById(R.id.registrar_button)
    }

    private fun initSpinners() {
        // Emisor Spinner:
        val emisorAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.emisor, android.R.layout.simple_spinner_item)
        emisorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emisorSpinner.adapter = emisorAdapter
        emisorSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
            }
        }

        // Receptor Spinner:
        val receptorAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.receptor, android.R.layout.simple_spinner_item)
        emisorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emisorSpinner.adapter = receptorAdapter
        emisorSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
            }
        }

        // Categoría Spinner:
        val categoriaAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.categoria, android.R.layout.simple_spinner_item)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emisorSpinner.adapter = categoriaAdapter
        emisorSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
            }
        }
    }

    private fun initEditTexts() {
        // Monto Edit:
        montoEdit.setOnClickListener {
            val input: String = montoEdit.text.toString()
            currentMonto = input.toInt()
        }

        // Motivo Edit:
        motivoEdit.setOnClickListener {
            currentMotivo = motivoEdit.text.toString()
        }
    }

    private fun initButtons() {
        registrarButton.setOnClickListener{
            // TODO: logica para hacer multiples logs si se hace a "todos".
            logToSheets(currentEmisor, currentReceptor, currentMonto, currentCategoria, currentMotivo)
        }
    }

    private fun logToSheets(emisor: String, receptor: String, monto: Int, categoria: String, motivo: String)
    {
        val url = "https://script.google.com/macros/s/AKfycbyRKdBzm3WBNfp0c9J7oJyTlKmCQNIuR548dHRCX86lcaTSMEs_j-nwyA/exec"
        val requestURL = url+"?emisor="+emisor+"&receptor="+receptor+"&monto="+monto+"&categoria="+categoria+"&motivo="+motivo
        val request = Request.Builder().url(requestURL).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "Sent a GET request")
                Toast.makeText(this@MainActivity, "Registrado correctamente", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "Failed to send GET request")
                Toast.makeText(this@MainActivity, "Error al registrar!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLastLog(){
        //TODO: mostrar el ultimo elemento loggeado.
    }

}

