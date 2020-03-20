package br.com.tasafe.model.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TB_USER")
data class User(

    @NonNull
    @PrimaryKey(autoGenerate = true)
    var idUser: Int?,

    @ColumnInfo(name = "nome")
    var nome: String,

    @ColumnInfo(name = "email")
    var email: String
)