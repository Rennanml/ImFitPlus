package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityIdealWeightBinding
import kotlin.math.pow

class IdealWeightActivity : AppCompatActivity() {
    private val aiwb: ActivityIdealWeightBinding by lazy {
        ActivityIdealWeightBinding.inflate(layoutInflater)
    }

    private lateinit var personalData: PersonalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aiwb.root)

        setSupportActionBar(aiwb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Peso Ideal"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        personalData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PERSONAL_DATA", PersonalData::class.java)!!
        } else {
            (intent.getSerializableExtra("PERSONAL_DATA") as? PersonalData)!!
        }

        setViewValues()

        // Buttons set on click listeners

        aiwb.backBt.setOnClickListener {
            if (aiwb.backCb.isChecked) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                prepareResult()
            }

            finish()
        }

    }

    private fun prepareResult() {
        setResult(
            RESULT_OK,
            Intent().putExtra("CALLBACK_MESSAGE", "Voltando da página: Peso ideal")
        )
    }

    private fun setViewValues() {
        val height: Double = personalData.height
        val weight: Double = personalData.weight
        val idealWeight: Double = 22 * (height.pow(2))
        aiwb.iwTv.text = idealWeight.toString()
        aiwb.wdTv.text = (weight - idealWeight).toString()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            prepareResult()
            finish()
            true
        }
        return super.onContextItemSelected(item)
    }
}