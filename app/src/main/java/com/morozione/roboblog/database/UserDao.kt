package com.morozione.roboblog.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.morozione.roboblog.Constants
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.utils.Utils
import io.reactivex.Completable
import io.reactivex.Single


class UserDao {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseReference =
        FirebaseDatabase.getInstance().reference.child(Constants.DATABASE_USER)

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    companion object {
        fun getCurrentUserId(): String {
            val firebaseUser = FirebaseAuth.getInstance().currentUser

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
        val key = firebaseReference.push().key
        user.id = key!!
        firebaseReference.child(key)
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
}