package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.controller

import android.content.Context
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.UserEntity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.persistence.UserDao
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.persistence.UserSqlite

class UserController(context: Context) {
    private val userDao: UserDao = UserSqlite(context)

    fun insertUser(user: UserEntity) = userDao.createUser(user)
    fun retrieveUsers() = userDao.retrieveUsers()
}
