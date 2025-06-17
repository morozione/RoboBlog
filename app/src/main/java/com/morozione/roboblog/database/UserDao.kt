package com.morozione.roboblog.database

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.morozione.roboblog.Constants
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.utils.Utils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single


class UserDao {
    private val firebaseAuth = Firebase.auth
    private val firebaseReference = Firebase.database.reference.child(Constants.DATABASE_USER)

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    companion object {
        fun getCurrentUserId(): String {
            val firebaseUser = Firebase.auth.currentUser

            return Utils.noNull(firebaseUser?.uid)
        }
    }

    fun signUp(login: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(login, password)
                .addOnSuccessListener { _ -> emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }
    }

    fun signIn(login: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(login, password)
                .addOnSuccessListener { _ -> emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }
    }

    fun changeValue(blog: Blog, value: Int, field: String) {
        firebaseReference.child(blog.userId).ref.child(field)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        val numberOfComments = dataSnapshot.value as Long
                        dataSnapshot.ref.setValue(numberOfComments + value)
                    } else {
                        dataSnapshot.ref.setValue(1)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    fun saveUser(user: User) = Completable.create { e ->
        user.id = getCurrentUserId()
        firebaseReference.child(user.id)
            .setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    e.onComplete()
                } else {
                    e.onError(Exception(it.exception.toString()))
                }
            }.addOnCanceledListener {
                e.onError(Exception())
            }
    }

    fun loadUser(id: String) = Single.create<User> { e ->
        firebaseReference.orderByKey().equalTo(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        val user = child.getValue(User::class.java)
                        user?.let {
                            e.onSuccess(user)
                            return
                        }
                    }
                }

                override fun onCancelled(onError: DatabaseError) {
                    e.onError(onError.toException())
                }
            })
    }

    fun updateUser(user: User) = Completable.create { e ->
        firebaseReference.child(user.id).orderByKey().ref.setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    e.onComplete()
                } else {
                    e.onError(Exception(it.exception.toString()))
                }
            }.addOnCanceledListener {
                e.onError(Exception())
            }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}