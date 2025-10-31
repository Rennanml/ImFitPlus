package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.view

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.R
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityHealthSummaryBinding
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.CALLBACK_MESSAGE
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.PERSONAL_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.RESULT_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.PersonalData
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.ResultData
import kotlin.math.pow

class HealthSummaryActivity : AppCompatActivity() {
    private val ahsmb: ActivityHealthSummaryBinding by lazy {
        ActivityHealthSummaryBinding.inflate(layoutInflater)
    }

    val df = DecimalFormat("0.00")

    private lateinit var personalData: PersonalData

    private lateinit var resultData: ResultData

    private lateinit var imcCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ahsmb.root)

        setSupportActionBar(ahsmb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Resumo da Saúde"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            personalData = intent.getSerializableExtra(PERSONAL_DATA, PersonalData::class.java)!!
            resultData = intent.getSerializableExtra(RESULT_DATA, ResultData::class.java)!!
        } else {
            personalData = (intent.getSerializableExtra(PERSONAL_DATA) as? PersonalData)!!
            resultData = (intent.getSerializableExtra(RESULT_DATA) as? ResultData)!!
        }

        imcCategory = intent.getStringExtra("IMC_CATEGORY").toString()

        with(ahsmb) {
            nameResultTv.text = personalData.name
            imcResultTv.text = df.format(resultData.imcValue)
            idealResultTv.text = (22 * (personalData.height.pow(2))).toString()
            calResultTv.text = resultData.tmbValue.toString()
            waterResultTv.text = calculateWaterConsume().toString()
            categoryResultTv.text = imcCategory
        }
    }

    private fun calculateWaterConsume(): Double {
        return (personalData.weight * 350) / 1000
    }
}