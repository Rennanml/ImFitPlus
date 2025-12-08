package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model

import java.io.Serializable
import java.time.LocalDateTime

data class UserEntity(
    var id: Int? = -1,
    var name: String = "",
    var gender: String = "",
    var height: Double = -1.0,
    var weight: Double = -1.0,
    var activityLevel: String = "",
    var imc: Double = -1.0,
    var imcCategory: String = "",
    var tmb: Double = -1.0,
    var idealWeight: Double = -1.0,
    var registerDate: String = LocalDateTime.now().toString()
): Serializable
