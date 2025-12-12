package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.R
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityPersonalDataBinding
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.CALLBACK_MESSAGE
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.IMC_VALUE
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.Constants.PERSONAL_DATA
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.PersonalData
import java.time.LocalDate
import java.time.Period
import java.time.temporal.Temporal
import kotlin.math.pow

class PersonalDataActivity : AppCompatActivity() {
    private val apdb: ActivityPersonalDataBinding by lazy {
        ActivityPersonalDataBinding.inflate(layoutInflater)
    }

    private lateinit var imcarl: ActivityResultLauncher<Intent>

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
                it.isEnabled = false

                val data: PersonalData = createDto()

                imcarl.launch(Intent(this, IMCResultActivity::class.java)
                    .apply {
                        putExtra(PERSONAL_DATA ,data)
                        putExtra(IMC_VALUE, calculate())
                    })
            }
        }

        imcarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == RESULT_OK) {
                val msg = (result.data as Intent).getStringExtra(CALLBACK_MESSAGE)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        apdb.calculateBt.isEnabled = true
    }

    private fun calculate(): Double {
        val height = apdb.heightEt.text.toString().toDouble()
        val weight = apdb.weightEt.text.toString().toDouble()

        if (height > 0 && weight > 0) {
            return weight / height.pow(2)
        }

        return 0.0;
    }

    private fun createDto(): PersonalData {
        val birthDate = apdb.birthDateEt.text.toString()
        val birthDateObject: LocalDate = getDateObjectFromString(birthDate)

        val name = apdb.nameEt.text.toString()
        val age = calculateAge(birthDateObject)
        val height = apdb.heightEt.text.toString().toDouble()
        val weight = apdb.weightEt.text.toString().toDouble()
        val activityLevel = apdb.activityLevelSp.selectedItem.toString()

        val selectedGenderId = apdb.genreRg.checkedRadioButtonId
        val gender = if (selectedGenderId == R.id.male_rb) "Masculino" else "Feminino"

        return PersonalData(
            name,
            age,
            gender,
            height,
            weight,
            activityLevel,
            birthDateObject
        )
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (apdb.nameEt.text.isBlank()) {
            apdb.nameEt.error = "O nome é obrigatório"
            isValid = false
        }

        if (apdb.birthDateEt.text.isBlank()) {
            apdb.birthDateEt.error = "A data de nascimento é obrigatória"
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

    private fun calculateAge(birthDate: LocalDate): Int {
        val currentDate: LocalDate = LocalDate.now()

        return Period.between(birthDate, currentDate).years
    }

    private fun getDateObjectFromString(string: String): LocalDate {
        val splitResult = string.split("/")

        return LocalDate.of(splitResult[2].toInt(), splitResult[1].toInt(), splitResult[0].toInt())
    }
}