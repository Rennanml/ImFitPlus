package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityPersonalDataBinding
import kotlin.math.pow

class PersonalDataActivity : AppCompatActivity() {
    private val apdb: ActivityPersonalDataBinding by lazy {
        ActivityPersonalDataBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apdb.root)

        setSupportActionBar(apdb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Dados Pessoais"

        ArrayAdapter.createFromResource(
            this,
            R.array.activity_levels,
            android.R.layout.simple_spinner_item
        ).let {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            apdb.activityLevelSp.adapter = adapter
        }

        apdb.calculateBt.setOnClickListener {
            if (!validateFields()) {
                Toast.makeText(this, "Corrija os campos marcados", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, calculate().toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calculate(): Double {
        val height = apdb.heightEt.text.toString().toDouble()
        val weight = apdb.weightEt.text.toString().toDouble()

        if (height > 0 && weight > 0) {
            return weight / height.pow(2)
        }

        return 0.0;
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (apdb.nameEt.text.isBlank()) {
            apdb.nameEt.error = "O nome é obrigatório"
            isValid = false
        }

        if (apdb.ageEt.text.isBlank()) {
            apdb.ageEt.error = "A idade é obrigatória"
            isValid = false
        } else if (apdb.ageEt.text.toString().toInt() <= 0) {
            apdb.ageEt.error = "A idade deve ser um valor positivo"
            isValid = false
        }

        if (apdb.heightEt.text.isBlank()) {
            apdb.heightEt.error = "A altura é obrigatória"
            isValid = false
        } else if (apdb.heightEt.text.toString().toDouble() <= 0) {
            apdb.heightEt.error = "A altura deve ser um valor positivo"
            isValid = false
        }

        if (apdb.weightEt.text.isBlank()) {
            apdb.weightEt.error = "O peso é obrigatório"
            isValid = false
        } else if (apdb.weightEt.text.toString().toDouble() <= 0) {
            apdb.weightEt.error = "O peso deve ser um valor positivo"
            isValid = false
        }

        if (apdb.activityLevelSp.selectedItemPosition == 0) {
            val errorText = "Selecione um nível de atividade"
            (apdb.activityLevelSp.selectedView as? TextView)?.error = errorText
            isValid = false
        }


        return isValid
    }

}