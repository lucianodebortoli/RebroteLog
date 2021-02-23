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
    private lateinit var emitterSpinner: Spinner
    private lateinit var amountEdit: EditText
    private lateinit var receptorSpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var detailEdit: EditText
    private lateinit var registerButton: Button
    private lateinit var newButton: Button
    private lateinit var sheetsButton: Button
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
        emitterSpinner = findViewById(R.id.emisor_spinner)
        amountEdit = findViewById(R.id.monto_edit)
        receptorSpinner = findViewById(R.id.receptor_spinner)
        categorySpinner = findViewById(R.id.categoria_spinner)
        detailEdit = findViewById(R.id.motivo_edit)
        registerButton = findViewById(R.id.registrar_button)
        newButton = findViewById(R.id.nuevo_button)
        sheetsButton = findViewById(R.id.sheets_button)
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
        emitterSpinner.adapter = emisorAdapter
        emitterSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == getString(R.string.select)) {
                    makeToast(getString(R.string.valid_name))
                } else {
                    currentEmisor = itemSelected
                    newButton.isEnabled=true
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
                    newButton.isEnabled=true
                    tv3.setTextColor(resources.getColor(R.color.colorCorrect))
                }
            }
        }

        // Categoría Spinner:
        val categoriaAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.category, android.R.layout.simple_spinner_item)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.adapter = categoriaAdapter
        categorySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == getString(R.string.select)) {
                    makeToast(getString(R.string.valid_category))
                } else {
                    currentCategoria = itemSelected
                    newButton.isEnabled=true
                    tv4.setTextColor(resources.getColor(R.color.colorCorrect))
                }
            }
        }
    }

    private fun initEditTexts() {
        // Monto Edit:
        amountEdit.setOnClickListener {
            val input: String = amountEdit.text.toString()
            if (input == "0" || input == ""){
                makeToast(getString(R.string.valid_amount))
                tv2.setTextColor(resources.getColor(R.color.colorError))
            }
            else {
                currentMonto = input.toInt()
                registerButton.isEnabled=true
                tv2.setTextColor(resources.getColor(R.color.colorCorrect))
            }
        }

        // Motivo Edit:
        detailEdit.setOnClickListener {
            val input = detailEdit.text.toString()
            if (input==resources.getString(R.string.motive_default)){
                makeToast(getString(R.string.valid_detail))
                tv5.setTextColor(resources.getColor(R.color.colorError))
            } else{
                currentMotivo = input
                registerButton.isEnabled=true
                tv5.setTextColor(resources.getColor(R.color.colorCorrect))
            }
        }
    }

    private fun initButtons() {
        // new register:
        newButton.setOnClickListener{ newRegisterClicked() }

        // Registrar:
        registerButton.setOnClickListener{ registerClicked() }

        // Sheets:
        sheetsButton.setOnClickListener{ goToSheets() }
    }

    private fun newRegisterClicked() {
        emitterSpinner.setSelection(0)
        amountEdit.setText("0")
        receptorSpinner.setSelection(0)
        categorySpinner.setSelection(0)
        detailEdit.setText(resources.getString(R.string.motive_default))
        showAll()
        resetColors()
        newButton.isEnabled=false
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
        registerButton.isEnabled=false
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
        emitterSpinner.visibility = View.INVISIBLE
        amountEdit.visibility = View.INVISIBLE
        receptorSpinner.visibility = View.INVISIBLE
        categorySpinner.visibility = View.INVISIBLE
        detailEdit.visibility = View.INVISIBLE
        registerButton.visibility = View.INVISIBLE
        sheetsButton.visibility = View.INVISIBLE
        tv1.visibility = View.INVISIBLE
        tv2.visibility = View.INVISIBLE
        tv3.visibility = View.INVISIBLE
        tv4.visibility = View.INVISIBLE
        tv5.visibility = View.INVISIBLE
    }

    private fun showAll(){
        emitterSpinner.visibility = View.VISIBLE
        amountEdit.visibility = View.VISIBLE
        receptorSpinner.visibility = View.VISIBLE
        categorySpinner.visibility = View.VISIBLE
        detailEdit.visibility = View.VISIBLE
        registerButton.visibility = View.VISIBLE
        sheetsButton.visibility = View.VISIBLE
        tv1.visibility = View.VISIBLE
        tv2.visibility = View.VISIBLE
        tv3.visibility = View.VISIBLE
        tv4.visibility = View.VISIBLE
        tv5.visibility = View.VISIBLE
        newButton.text = getString(R.string.clean)
    }

    private fun resetColors(){
        tv1.setTextColor(resources.getColor(R.color.colorNeutral))
        tv2.setTextColor(resources.getColor(R.color.colorNeutral))
        tv3.setTextColor(resources.getColor(R.color.colorNeutral))
        tv4.setTextColor(resources.getColor(R.color.colorNeutral))
        tv5.setTextColor(resources.getColor(R.color.colorNeutral))
    }

    private fun goToSheets(){
        val url = ""
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}

