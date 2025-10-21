package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus

import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            personalData = intent.getSerializableExtra("PERSONAL_DATA", PersonalData::class.java)!!
        } else {
            personalData = (intent.getSerializableExtra("PERSONAL_DATA") as? PersonalData)!!
        }

        imcValue = intent.getDoubleExtra("IMC_VALUE", 0.0)


        aimcb.resultTv.text = buildString {
            append(personalData.toString())
            append(df.format(imcValue))
        }
    }
}