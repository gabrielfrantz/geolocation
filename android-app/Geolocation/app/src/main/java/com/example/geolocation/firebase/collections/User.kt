package com.example.geolocation.firebase.collections

import android.util.Log
import com.example.geolocation.classes.mapbox.Mapview
import com.example.geolocation.global.Geolocation.Companion.user
import com.google.android.gms.tasks.Task
import com.example.geolocation.firebase.objects.User as UserObject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class User() {
    fun initializeUser() {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .whereEqualTo("uID", user.uid)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.count() == 0) {
                    addUser()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("User Collection", "Error getting documents: ", exception)
            }
    }

    fun getUserList(mapview: Mapview) {
        val userList: ArrayList<UserObject> = ArrayList()
        val db = FirebaseFirestore.getInstance()

        var promise: Task<QuerySnapshot> = db.collection("users")
            .whereNotEqualTo("uID", user.uid)
            .get()

        promise.addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        val documents = task.result
                        for (document: QueryDocumentSnapshot in documents) {
                            val user = UserObject(
                                document.data["display_name"].toString(),
                                document.data["uID"].toString(),
                                document.data["photo_url"].toString()
                            )

                            userList.add(user)
                        }

                        mapview.updateUserList(userList)
                    }
                    else -> {
                        task.exception?.let { Log.e("Error", it.toString()) }
                    }
                }
            }
    }

    private fun addUser() {
        val db = FirebaseFirestore.getInstance()

        val currentUser = hashMapOf(
            "display_name" to user.displayName,
            "uID" to user.uid,
            "photo_url" to user.photoUrl.toString()
        )

        db.collection("users")
            .add(currentUser)
            .addOnSuccessListener { documentReference ->
                Log.d("User Collection", "User added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("User Collection", "Error adding User document", e)
            }
    }
}