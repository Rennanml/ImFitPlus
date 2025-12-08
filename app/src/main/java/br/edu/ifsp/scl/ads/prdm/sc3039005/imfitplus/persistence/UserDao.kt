package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.persistence

import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.UserEntity

interface UserDao {
    fun createUser(user: UserEntity): Long
    fun retrieveUsers(): MutableList<UserEntity>
}