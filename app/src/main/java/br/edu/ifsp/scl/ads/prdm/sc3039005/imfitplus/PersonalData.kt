package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus

import java.io.Serializable

data class PersonalData(
    val name: String,
    val age: Int,
    val gender: String,
    val height: Double,
    val weight: Double,
    val activityLevel: String
): Serializable
