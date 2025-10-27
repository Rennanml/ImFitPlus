package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityImcresultBinding
import kotlin.properties.Delegates

class IMCResultActivity : AppCompatActivity() {
    val aimcb: ActivityImcresultBinding by lazy {
        ActivityImcresultBinding.inflate(layoutInflater)
    }

    private lateinit var tmbarl: ActivityResultLauncher<Intent>

    lateinit var personalData: PersonalData
    var imcValue by Delegates.notNull<Double>()

    val df = DecimalFormat("0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aimcb.root)

        setSupportActionBar(aimcb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Calculo IMC"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        personalData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PERSONAL_DATA", PersonalData::class.java)!!
        } else {
            (intent.getSerializableExtra("PERSONAL_DATA") as? PersonalData)!!
        }

        tmbarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if (result.resultCode == RESULT_OK) {
                val msg = (result.data as Intent).getStringExtra("CALLBACK_MESSAGE")
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        // Get IMC_VALUE from intent
        imcValue = intent.getDoubleExtra("IMC_VALUE", 0.0)

        setViewTexts()

        // Buttons onClickListeners
        aimcb.backBt.setOnClickListener {
            prepareResult()
            finish()
        }

        aimcb.calculateBt.setOnClickListener {
            tmbarl.launch(
                Intent(this, TMBResultActivity::class.java).apply {
                    putExtra("PERSONAL_DATA", personalData)
                })
        }
    }

    private fun prepareResult() {
        setResult(
            RESULT_OK,
            Intent().putExtra("CALLBACK_MESSAGE", "Voltando da tela: Calculo IMC")
        )
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
            prepareResult()
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

