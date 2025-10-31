package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.view

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityTmbresultBinding
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.CALLBACK_MESSAGE
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.PERSONAL_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.RESULT_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.PersonalData
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.ResultData

class TMBResultActivity : AppCompatActivity() {
    private val atmbb: ActivityTmbresultBinding by lazy {
        ActivityTmbresultBinding.inflate(layoutInflater)
    }

    private lateinit var iwarl: ActivityResultLauncher<Intent>

    private lateinit var personalData: PersonalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atmbb.root)

        setSupportActionBar(atmbb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Gasto Calórico Diário"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        iwarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == RESULT_OK) {
                val msg = (result.data as Intent).getStringExtra(CALLBACK_MESSAGE)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        var resultData: ResultData

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            personalData = intent.getSerializableExtra(PERSONAL_DATA, PersonalData::class.java)!!
            resultData = intent.getSerializableExtra(RESULT_DATA, ResultData::class.java)!!
        } else {
            personalData = (intent.getSerializableExtra(PERSONAL_DATA) as? PersonalData)!!
            resultData = (intent.getSerializableExtra(RESULT_DATA) as? ResultData)!!
        }


        setViewValues()
        resultData.tmbValue = calculeTMB()

        // Buttons set on click listeners
        atmbb.backBt.setOnClickListener {
            prepareResult()
            finish()
        }


        atmbb.iwBt.setOnClickListener {
            iwarl.launch(Intent(this, IdealWeightActivity::class.java).apply {
                putExtra(PERSONAL_DATA, personalData)
                putExtra(RESULT_DATA, resultData)
                putExtra("IMC_CATEGORY", intent.getStringExtra("IMC_CATEGORY").toString())
            })
        }
    }

    private fun prepareResult() {
        setResult(
            RESULT_OK, Intent().putExtra(
                CALLBACK_MESSAGE,
                "Voltando da página: Gasto calórico diário"
            )
        )
    }

    private fun setViewValues() {
        atmbb.tmbTv.text = calculeTMB().toString()
        atmbb.nameTv.text = personalData.name
        atmbb.genreTv.text = personalData.gender
    }

    private fun calculeTMB(): Double {
        val weight: Double = personalData.weight
        val heigth: Double = personalData.height
        val age: Int = personalData.age

        val gender = personalData.gender
        return if (gender == "Masculino") {
            66 + (13.7 * weight) + (5 * heigth * 100) - (6.8 * age)
        } else {
            655 + (9.6 * weight) + (1.8 * heigth * 100) - (4.7 * age)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            prepareResult()
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}