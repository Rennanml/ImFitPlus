package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityTmbresultBinding

class TMBResultActivity : AppCompatActivity() {
    private val atmbb: ActivityTmbresultBinding by lazy {
        ActivityTmbresultBinding.inflate(layoutInflater)
    }

    private lateinit var personalData: PersonalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atmbb.root)

        setSupportActionBar(atmbb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Gasto Calórico Diário"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        personalData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PERSONAL_DATA", PersonalData::class.java)!!
        } else {
            (intent.getSerializableExtra("PERSONAL_DATA") as? PersonalData)!!
        }

        var tmb: Double = calculeTMB()

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
}