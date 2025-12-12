package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model

import java.io.Serializable
import java.time.LocalDate

data class PersonalData(
    val name: String,
    val age: Int,
    val gender: String,
    val height: Double,
    val weight: Double,
    val activityLevel: String,
    val birthDate: LocalDate
): Serializable