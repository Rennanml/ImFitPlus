package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.view

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityIdealWeightBinding
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.CALLBACK_MESSAGE
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.PERSONAL_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.RESULT_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.LogItem
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.PersonalData
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.ResultData
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.persistence.LogRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

        val resultData: ResultData

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            personalData = intent.getSerializableExtra(PERSONAL_DATA, PersonalData::class.java)!!
            resultData = intent.getSerializableExtra(RESULT_DATA, ResultData::class.java)!!
        } else {
            personalData = (intent.getSerializableExtra(PERSONAL_DATA) as? PersonalData)!!
            resultData = (intent.getSerializableExtra(RESULT_DATA) as? ResultData)!!
        }

        setViewValues()

        // Buttons set on click listeners

        aiwb.finishBt.setOnClickListener {
            if (aiwb.logCb.isChecked) {
                val newItem = createLogItem(resultData)

                LogRepository.saveLogItem(this, newItem)

                startActivity(Intent(this, LogActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }

    private fun createLogItem(resultData: ResultData): LogItem {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        return LogItem(
            name = personalData.name,
            imcValue = resultData.imcValue.toString(),
            tmbValue = resultData.tmbValue.toString(),
            date = currentDate
        )
    }

    private fun prepareResult() {
        setResult(
            RESULT_OK,
            Intent().putExtra(CALLBACK_MESSAGE, "Voltando da página: Peso ideal")
        )
    }

    private fun setViewValues() {
        val height: Double = personalData.height
        val weight: Double = personalData.weight
        val idealWeight: Double = 22 * (height.pow(2))
        aiwb.iwTv.text = idealWeight.toString()
        aiwb.wdTv.text = (weight - idealWeight).toString()
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