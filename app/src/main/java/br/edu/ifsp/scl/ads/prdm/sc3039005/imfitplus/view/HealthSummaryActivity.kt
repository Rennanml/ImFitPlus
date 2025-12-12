package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.view

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.controller.UserController
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityHealthSummaryBinding
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.PERSONAL_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.RESULT_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.PersonalData
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.ResultData
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.UserEntity
import java.time.LocalDateTime
import kotlin.math.pow

class HealthSummaryActivity : AppCompatActivity() {
    private val ahsmb: ActivityHealthSummaryBinding by lazy {
        ActivityHealthSummaryBinding.inflate(layoutInflater)
    }

    val df = DecimalFormat("0.00")

    private lateinit var personalData: PersonalData

    private lateinit var resultData: ResultData

    private lateinit var imcCategory: String

    private val userController: UserController by lazy {
        UserController(this)
    }

    @SuppressLint("SetTextI18n")
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

        putCardiacData()


        ahsmb.finishBt.setOnClickListener {
            if (ahsmb.logCb.isChecked) {
                val newItem = createUserEntity(resultData)

                userController.insertUser(newItem)

                startActivity(Intent(this, LogActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun calculateWaterConsume(): Double {
        return (personalData.weight * 350) / 1000
    }

    private fun calculateCardiacFrequency(): Int {
        return 220 - personalData.age
    }

    private fun createUserEntity(resultData: ResultData): UserEntity {
        return UserEntity(
            name = personalData.name,
            gender = personalData.gender,
            height = personalData.height,
            weight = personalData.weight,
            activityLevel = personalData.activityLevel,
            imc = resultData.imcValue!!,
            tmb = resultData.tmbValue!!,
            registerDate = LocalDateTime.now().toString(),
            age = personalData.age,
            birthDate = personalData.birthDate.toString(),
            maxCardiacFrequency = calculateCardiacFrequency()
        )
    }

    @SuppressLint("SetTextI18n")
    private fun putCardiacData() {
        val maxFrequency = calculateCardiacFrequency()
        ahsmb.maxFrequencyResultTv.text = maxFrequency.toString()
        ahsmb.lightFrequencyResultTv.text = ((maxFrequency * 50) / 100).toString() + " - " + ((maxFrequency * 60) / 100 ).toString()
        ahsmb.burnFrequencyResultTv.text = ((maxFrequency * 60) / 100).toString() + " - " + ((maxFrequency * 70) / 100 ).toString()
        ahsmb.aerobicFrequencyResultTv.text = ((maxFrequency * 70) / 100).toString() + " - " + ((maxFrequency * 80) / 100).toString()
        ahsmb.anaerobicFrequencyResultTv.text = ((maxFrequency * 80) / 100).toString() + " - " + ((maxFrequency * 90) / 100).toString()
    }
}