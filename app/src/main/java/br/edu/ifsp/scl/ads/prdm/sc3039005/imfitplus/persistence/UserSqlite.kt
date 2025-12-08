package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.persistence

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.UserEntity

class UserSqlite(context: Context): UserDao {
    companion object {
        private const val USER_DATABASE_FILE = "userDatabase"
        private const val USER_TABLE = "user"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val GENDER_COLUMN = "gender"
        private const val HEIGHT_COLUMN = "height"
        private const val WEIGHT_COLUMN = "weight"
        private const val ACTIVITY_LEVEL_COLUMN = "activityLevel"
        private const val IMC_COLUMN = "imc"
        private const val IMC_CATEGORY_COLUMN = "imcCategory"
        private const val TMB_COLUMN = "tmb"
        private const val IDEAL_WEIGHT_COLUMN = "idealWeight"
        private const val REGISTER_DATE_COLUMN = "registerDate"

        const val CREATE_USER_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS $USER_TABLE ( " +
                "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME_COLUMN TEXT NOT NULL, " +
                "$GENDER_COLUMN TEXT NOT NULL, " +
                "$HEIGHT_COLUMN REAL NOT NULL, " +
                "$WEIGHT_COLUMN REAL NOT NULL, " +
                "$ACTIVITY_LEVEL_COLUMN TEXT NOT NULL, " +
                "$IMC_COLUMN REAL NOT NULL, " +
                "$IMC_CATEGORY_COLUMN TEXT NOT NULL, " +
                "$TMB_COLUMN REAL NOT NULL, " +
                "$IDEAL_WEIGHT_COLUMN REAL NOT NULL, " +
                "$REGISTER_DATE_COLUMN TEXT NOT NULL );"
    }

    private val userDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        USER_DATABASE_FILE,
        MODE_PRIVATE,
        null
    )

    init {
        try {
            userDatabase.execSQL(CREATE_USER_TABLE_STATEMENT)
        } catch (e: Exception) {
            Log.e("UserSqlite", e.toString())
        }

    }

    override fun createUser(user: UserEntity): Long
        = userDatabase.insert(USER_TABLE, null, user.toContentValues())


    override fun retrieveUsers(): MutableList<UserEntity> {
        val userList: MutableList<UserEntity> = mutableListOf()
        val cursor = userDatabase.rawQuery("SELECT * FROM $USER_TABLE ORDER BY $REGISTER_DATE_COLUMN DESC;", null)

        while (cursor.moveToNext()) {
            userList.add(cursor.toUser())
        }

        cursor.close()
        return userList
    }

    private fun UserEntity.toContentValues() = ContentValues().apply {
        put(NAME_COLUMN, name)
        put(GENDER_COLUMN, gender)
        put(HEIGHT_COLUMN, height)
        put(WEIGHT_COLUMN, weight)
        put(ACTIVITY_LEVEL_COLUMN, activityLevel)
        put(IMC_COLUMN, imc)
        put(IMC_CATEGORY_COLUMN, imcCategory)
        put(TMB_COLUMN, tmb)
        put(IDEAL_WEIGHT_COLUMN, idealWeight)
        put(REGISTER_DATE_COLUMN, registerDate.toString())
    }

    private fun Cursor.toUser() = UserEntity(
        id = getInt(getColumnIndexOrThrow(ID_COLUMN)),
        name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
        gender = getString(getColumnIndexOrThrow(GENDER_COLUMN)),
        height = getDouble(getColumnIndexOrThrow(HEIGHT_COLUMN)),
        weight = getDouble(getColumnIndexOrThrow(WEIGHT_COLUMN)),
        activityLevel = getString(getColumnIndexOrThrow(ACTIVITY_LEVEL_COLUMN)),
        imc = getDouble(getColumnIndexOrThrow(IMC_COLUMN)),
        imcCategory = getString(getColumnIndexOrThrow(IMC_CATEGORY_COLUMN)),
        tmb = getDouble(getColumnIndexOrThrow(TMB_COLUMN)),
        idealWeight = getDouble(getColumnIndexOrThrow(IDEAL_WEIGHT_COLUMN)),
        registerDate = getString(getColumnIndexOrThrow(REGISTER_DATE_COLUMN))
    )
}
