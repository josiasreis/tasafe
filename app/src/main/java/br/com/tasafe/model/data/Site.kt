package br.com.tasafe.model.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TB_SITE")
data class Site (
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var idSite: Int?,
    @ColumnInfo(name = "nomeSite")
    var nomeSite: String,
    @ColumnInfo(name = "urlSite")
    var urlSite: String,
    @ColumnInfo(name = "usuario")
    var usuario: String,
    @ColumnInfo(name = "iv")
    var iv: String,
    @ColumnInfo(name = "encrypted")
    var encrypted: String,
    @ColumnInfo(name = "decrypt")
    var decrypt: String,
    @ColumnInfo(name = "imagem")
    var imagem: String
)