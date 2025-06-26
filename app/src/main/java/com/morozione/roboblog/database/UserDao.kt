package com.morozione.roboblog.database

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.morozione.roboblog.Constants
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.utils.Utils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class UserDao : BaseDao() {
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

    fun changeValue(blog: Blog, value: Int, field: String): Completable {
        val userRef = firebaseReference.child(blog.userId).child(field)
        return userRef.asSingle { snapshot ->
            snapshot.value as? Long
        }.flatMapCompletable { currentValue ->
            userRef.updateValueCompletable(currentValue, value)
        }.onErrorResumeNext {
            // If field doesn't exist, set initial value
            userRef.asCompletable(value)
        }
    }

    fun saveUser(user: User): Completable {
        user.id = getCurrentUserId()
        return firebaseReference.child(user.id).asCompletable(user)
    }

    // One-time user load (for backwards compatibility if needed)
    fun loadUserOnce(id: String): Single<User> {
        return firebaseReference.child(id).asSingle { snapshot ->
            snapshot.getValue(User::class.java)?.apply { 
                this.id = snapshot.key ?: id 
            }
        }
    }
    
    // Real-time reactive user loading (now the main method)
    fun loadUser(id: String): Observable<User> {
        return firebaseReference.child(id).asRealtimeObservableObject { snapshot ->
            snapshot.getValue(User::class.java)?.apply { 
                this.id = snapshot.key ?: id 
            }
        }.share() // Share the Observable to avoid multiple Firebase listeners
    }

    fun updateUser(user: User): Completable {
        return firebaseReference.child(user.id).asCompletable(user)
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}