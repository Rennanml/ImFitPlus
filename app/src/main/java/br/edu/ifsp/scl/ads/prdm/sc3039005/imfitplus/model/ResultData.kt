package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model

import java.io.Serializable

data class ResultData(
    var imcValue: Double?,
    var tmbValue: Double?,
    var maxCardiacFrequency: Int?
): Serializable
