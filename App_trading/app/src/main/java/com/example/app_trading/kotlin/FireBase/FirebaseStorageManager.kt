package com.example.app_trading.kotlin.FireBase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseStorageManager {

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = storage.reference

    // Subir un archivo a Firebase Storage
    fun uploadFile(
        filePath: Uri,
        folderName: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileReference = storageReference.child("$folderName/${filePath.lastPathSegment}")
        fileReference.putFile(filePath)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString()) // Devuelve la URL del archivo subido
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Obtener una referencia a un archivo
    fun getFileReference(fileName: String, folderName: String): StorageReference {
        return storageReference.child("$folderName/$fileName")
    }
}
