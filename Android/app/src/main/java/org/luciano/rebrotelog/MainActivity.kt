package org.luciano.rebrotelog
import android.opengl.Visibility
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
    private lateinit var nuevoButton: Button
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var tv3: TextView
    private lateinit var tv4: TextView
    private lateinit var tv5: TextView

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
        hideAll()
    }

    private fun findViews() {
        emisorSpinner = findViewById(R.id.emisor_spinner)
        montoEdit = findViewById(R.id.monto_edit)
        receptorSpinner = findViewById(R.id.receptor_spinner)
        categoriaSpinner = findViewById(R.id.categoria_spinner)
        motivoEdit = findViewById(R.id.motivo_edit)
        registrarButton = findViewById(R.id.registrar_button)
        nuevoButton = findViewById(R.id.nuevo_button)
        tv1 = findViewById(R.id.tv1)
        tv2 = findViewById(R.id.tv2)
        tv3 = findViewById(R.id.tv3)
        tv4 = findViewById(R.id.tv4)
        tv5 = findViewById(R.id.tv5)
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
                if (itemSelected == "Seleccionar Nombre") {
                    makeToast("Seleccionar nombre válido")
                } else {
                    currentEmisor = itemSelected
                }
            }
        }

        // Receptor Spinner:
        val receptorAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.receptor, android.R.layout.simple_spinner_item)
        receptorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receptorSpinner.adapter = receptorAdapter
        receptorSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == "Seleccionar Nombre") {
                    makeToast("Seleccionar nombre válido")
                } else {
                    currentReceptor = itemSelected
                }
            }
        }

        // Categoría Spinner:
        val categoriaAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.categoria, android.R.layout.simple_spinner_item)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaSpinner.adapter = categoriaAdapter
        categoriaSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == "Seleccionar Categoría") {
                    makeToast("Seleccionar categoría válida")
                } else {
                    currentCategoria = itemSelected
                }
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
        // Iniciar nuevo registro:
        nuevoButton.setOnClickListener{ newRegisterClicked() }

        // Registrar:
        registrarButton.setOnClickListener{ registerClicked() }
    }

    private fun newRegisterClicked() {
        emisorSpinner.setSelection(0)
        montoEdit.setText("0")
        receptorSpinner.setSelection(0)
        categoriaSpinner.setSelection(0)
        motivoEdit.setText("Escribir detalle simple")
        showAll()
    }

    private fun registerClicked() {
        // TODO: logica para hacer multiples logs si se hace a "todos".
        submitRegister(currentEmisor, currentReceptor, currentMonto, currentCategoria, currentMotivo)
    }

    private fun submitRegister(emisor: String, receptor: String, monto: Int, categoria: String, motivo: String)
    {
        val url = "https://script.google.com/macros/s/AKfycbyRKdBzm3WBNfp0c9J7oJyTlKmCQNIuR548dHRCX86lcaTSMEs_j-nwyA/exec"
        val requestURL = url+"?emisor="+emisor+"&receptor="+receptor+"&monto="+monto+"&categoria="+categoria+"&motivo="+motivo
        val request = Request.Builder().url(requestURL).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "Sent a GET request")
                makeToast("Registrado correctamente!")
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "Failed to send GET request")
                makeToast("Error al registrar")
            }
        })
    }

    private fun makeToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun hideAll(){
        emisorSpinner.visibility = View.INVISIBLE
        montoEdit.visibility = View.INVISIBLE
        receptorSpinner.visibility = View.INVISIBLE
        categoriaSpinner.visibility = View.INVISIBLE
        motivoEdit.visibility = View.INVISIBLE
        registrarButton.visibility = View.INVISIBLE
        tv1.visibility = View.INVISIBLE
        tv2.visibility = View.INVISIBLE
        tv3.visibility = View.INVISIBLE
        tv4.visibility = View.INVISIBLE
        tv5.visibility = View.INVISIBLE
    }

    private fun showAll(){
        emisorSpinner.visibility = View.VISIBLE
        montoEdit.visibility = View.VISIBLE
        receptorSpinner.visibility = View.VISIBLE
        categoriaSpinner.visibility = View.VISIBLE
        motivoEdit.visibility = View.VISIBLE
        registrarButton.visibility = View.VISIBLE
        tv1.visibility = View.VISIBLE
        tv2.visibility = View.VISIBLE
        tv3.visibility = View.VISIBLE
        tv4.visibility = View.VISIBLE
        tv5.visibility = View.VISIBLE
    }

    private fun showLastLog(){
        //TODO: mostrar el ultimo elemento loggeado.
    }

}

