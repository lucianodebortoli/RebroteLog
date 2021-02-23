package org.luciano.splitlog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // Activity:
    private var TAG = "TAG"
    private lateinit var mainLayout: View
    private lateinit var emisorSpinner: Spinner
    private lateinit var montoEdit: EditText
    private lateinit var receptorSpinner: Spinner
    private lateinit var categoriaSpinner: Spinner
    private lateinit var motivoEdit: EditText
    private lateinit var registrarButton: Button
    private lateinit var nuevoButton: Button
    private lateinit var sheets_button: Button
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var tv3: TextView
    private lateinit var tv4: TextView
    private lateinit var tv5: TextView

    // Declare Dynamic Variables:
    private var currentMonto: Int = 0
    private lateinit var currentEmisor: String
    private lateinit var currentReceptor: String
    private lateinit var currentCategoria: String
    private lateinit var currentMotivo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initLayout()
        initVars()
        initSpinners()
        initEditTexts()
        initButtons()
        hideAll()
    }

    private fun initVars() {
        currentMonto= 0
        currentEmisor= getString(R.string.select)
        currentReceptor= getString(R.string.select)
        currentCategoria= getString(R.string.select)
        currentMotivo= getString(R.string.select)
    }

    private fun initLayout() {
        mainLayout.setOnClickListener{
            val inputMethodManager: InputMethodManager = mainLayout.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(mainLayout.windowToken,0)
        }
    }

    private fun findViews() {
        mainLayout = findViewById(R.id.main_layout)
        emisorSpinner = findViewById(R.id.emisor_spinner)
        montoEdit = findViewById(R.id.monto_edit)
        receptorSpinner = findViewById(R.id.receptor_spinner)
        categoriaSpinner = findViewById(R.id.categoria_spinner)
        motivoEdit = findViewById(R.id.motivo_edit)
        registrarButton = findViewById(R.id.registrar_button)
        nuevoButton = findViewById(R.id.nuevo_button)
        sheets_button = findViewById(R.id.sheets_button)
        tv1 = findViewById(R.id.tv1)
        tv2 = findViewById(R.id.tv2)
        tv3 = findViewById(R.id.tv3)
        tv4 = findViewById(R.id.tv4)
        tv5 = findViewById(R.id.tv5)
    }

    private fun initSpinners() {
        // Emisor Spinner:
        val emisorAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.emitter, android.R.layout.simple_spinner_item)
        emisorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emisorSpinner.adapter = emisorAdapter
        emisorSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == getString(R.string.select)) {
                    makeToast(getString(R.string.valid_name))
                } else {
                    currentEmisor = itemSelected
                    nuevoButton.isEnabled=true
                    tv1.setTextColor(resources.getColor(R.color.colorCorrect))
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
                if (itemSelected == getString(R.string.select)) {
                    makeToast(getString(R.string.valid_name))
                } else {
                    currentReceptor = itemSelected
                    nuevoButton.isEnabled=true
                    tv3.setTextColor(resources.getColor(R.color.colorCorrect))
                }
            }
        }

        // Categoría Spinner:
        val categoriaAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.category, android.R.layout.simple_spinner_item)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaSpinner.adapter = categoriaAdapter
        categoriaSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == getString(R.string.select)) {
                    makeToast(getString(R.string.valid_category))
                } else {
                    currentCategoria = itemSelected
                    nuevoButton.isEnabled=true
                    tv4.setTextColor(resources.getColor(R.color.colorCorrect))
                }
            }
        }
    }

    private fun initEditTexts() {
        // Monto Edit:
        montoEdit.setOnClickListener {
            val input: String = montoEdit.text.toString()
            if (input == "0" || input == ""){
                makeToast(getString(R.string.valid_amount))
                tv2.setTextColor(resources.getColor(R.color.colorError))
            }
            else {
                currentMonto = input.toInt()
                registrarButton.isEnabled=true
                tv2.setTextColor(resources.getColor(R.color.colorCorrect))
            }
        }

        // Motivo Edit:
        motivoEdit.setOnClickListener {
            val input = motivoEdit.text.toString()
            if (input==resources.getString(R.string.motive_default)){
                makeToast(getString(R.string.valid_detail))
                tv5.setTextColor(resources.getColor(R.color.colorError))
            } else{
                currentMotivo = input
                registrarButton.isEnabled=true
                tv5.setTextColor(resources.getColor(R.color.colorCorrect))
            }
        }
    }

    private fun initButtons() {
        // new register:
        nuevoButton.setOnClickListener{ newRegisterClicked() }

        // Registrar:
        registrarButton.setOnClickListener{ registerClicked() }

        // Sheets:
        sheets_button.setOnClickListener{ goToSheets() }
    }

    private fun newRegisterClicked() {
        emisorSpinner.setSelection(0)
        montoEdit.setText("0")
        receptorSpinner.setSelection(0)
        categoriaSpinner.setSelection(0)
        motivoEdit.setText(resources.getString(R.string.motive_default))
        showAll()
        resetColors()
        nuevoButton.isEnabled=false
    }

    private fun registerClicked() {
        if (currentReceptor==getString(R.string.all)) {
            // Multiple Register:
            val receptors: MutableList<String> = resources.getStringArray(R.array.receptor).toMutableList()
            receptors.removeAt(1)
            receptors.removeAt(0)
            val totalUsers = receptors.size
            currentMonto /= totalUsers
            receptors.forEach {
                currentReceptor = it
                submitRequest()
                Thread.sleep(1000)
            }
        } else {
            // Single Register:
            submitRequest()
        }
        registrarButton.isEnabled=false
    }

    private fun submitRequest() {
        if (isValidRequest()){
            sendRequest(currentEmisor, currentReceptor, currentMonto, currentCategoria, currentMotivo)
        }
    }

    private fun isValidRequest(): Boolean {
        if (currentEmisor==getString(R.string.select) ||
            currentCategoria==getString(R.string.select) ||
            currentReceptor==getString(R.string.select) ||
            currentMotivo == resources.getString(R.string.motive_default) ||
            currentMonto==0) {
            makeToast(getString(R.string.missing_fields))
            return false
        } else {
            return true
        }
    }

    private fun sendRequest(emitter: String, receptor: String, amount: Int, category: String, detail: String)
    {
        val url = ""
        val requestURL = url+"?emisor="+emitter+"&receptor="+receptor+"&monto="+amount+"&categoria="+category+"&motivo="+detail
        val request = Request.Builder().url(requestURL).build()
        val client = OkHttpClient()
        Log.i(TAG, "RequestURL: $requestURL")
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "Sent a GET request")
                runOnUiThread{
                    makeToast(getString(R.string.registered_successfully))
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "Failed to send GET request")
                runOnUiThread{
                    makeToast("ERROR while uploading")
                }
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
        sheets_button.visibility = View.INVISIBLE
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
        sheets_button.visibility = View.VISIBLE
        tv1.visibility = View.VISIBLE
        tv2.visibility = View.VISIBLE
        tv3.visibility = View.VISIBLE
        tv4.visibility = View.VISIBLE
        tv5.visibility = View.VISIBLE
        nuevoButton.text = getString(R.string.clean)
    }

    private fun resetColors(){
        tv1.setTextColor(resources.getColor(R.color.colorNeutral))
        tv2.setTextColor(resources.getColor(R.color.colorNeutral))
        tv3.setTextColor(resources.getColor(R.color.colorNeutral))
        tv4.setTextColor(resources.getColor(R.color.colorNeutral))
        tv5.setTextColor(resources.getColor(R.color.colorNeutral))
    }

    private fun goToSheets(){
        val url = "https://docs.google.com/spreadsheets/d/1u8hsgUbM8guJzTgguBTd5NeYh-m_1MNTzEzJUTICcBk/edit#gid=1041806097"
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}

