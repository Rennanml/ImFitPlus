package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus

import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityImcresultBinding
import kotlin.properties.Delegates

class IMCResultActivity : AppCompatActivity() {
    val aimcb: ActivityImcresultBinding by lazy {
        ActivityImcresultBinding.inflate(layoutInflater)
    }

    lateinit var personalData: PersonalData
    var imcValue by Delegates.notNull<Double>()

    val df = DecimalFormat("0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aimcb.root)

        setSupportActionBar(aimcb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Dados Pessoais"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        personalData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PERSONAL_DATA", PersonalData::class.java)!!
        } else {
            (intent.getSerializableExtra("PERSONAL_DATA") as? PersonalData)!!
        }

        // Get IMC_VALUE from intent
        imcValue = intent.getDoubleExtra("IMC_VALUE", 0.0)

        setViewTexts()

        // Buttons onClickListeners
        aimcb.backBt.setOnClickListener {
            finish()
        }
    }

    private fun setViewTexts() {
        aimcb.imcCategoryTv.text = getIMCResultLabel()
        aimcb.nameTv.text = personalData.name
        aimcb.imcTv.text = df.format(imcValue)
    }

    private fun getIMCResultLabel(): String {
        return when {
            imcValue < 18.5 -> "Abaixo do peso"

            imcValue < 25.0 -> "Normal"

            imcValue < 30.0 -> "Sobrepeso"

            else -> "Obesidade"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

