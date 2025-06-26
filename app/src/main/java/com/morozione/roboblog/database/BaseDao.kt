package com.morozione.roboblog.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

abstract class BaseDao {
    
    // Common Firebase operations as extension functions
    protected fun DatabaseReference.asCompletable(value: Any): Completable {
        return Completable.create { emitter ->
            setValue(value)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }
    }
    
    protected fun <T: Any> DatabaseReference.asSingle(mapper: (DataSnapshot) -> T?): Single<T> {
        return Single.create { emitter ->
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val result = mapper(snapshot)
                        if (result != null) {
                            emitter.onSuccess(result)
                        } else {
                            emitter.onError(NoSuchElementException("No data found"))
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            })
        }
    }
    
    protected fun <T: Any> DatabaseReference.asObservable(mapper: (DataSnapshot) -> List<T>): Observable<T> {
        return Observable.create { emitter ->
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val items = mapper(snapshot)
                        items.forEach { emitter.onNext(it) }
                        emitter.onComplete()
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            })
        }
    }
    
    protected fun DatabaseReference.updateValueCompletable(
        currentValue: Long?,
        increment: Int
    ): Completable {
        return Completable.create { emitter ->
            val newValue = (currentValue ?: 0) + increment
            setValue(newValue)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }
    }
    
    // Extension functions for Query objects
    protected fun <T: Any> Query.asSingle(mapper: (DataSnapshot) -> T?): Single<T> {
        return Single.create { emitter ->
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val result = mapper(snapshot)
                        if (result != null) {
                            emitter.onSuccess(result)
                        } else {
                            emitter.onError(NoSuchElementException("No data found"))
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            })
        }
    }
    
    protected fun <T: Any> Query.asObservable(mapper: (DataSnapshot) -> List<T>): Observable<T> {
        return Observable.create { emitter ->
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val items = mapper(snapshot)
                        items.forEach { emitter.onNext(it) }
                        emitter.onComplete()
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            })
        }
    }
    
    // Real-time Observable that continuously listens for changes and emits complete lists
    protected fun <T: Any> Query.asRealtimeObservable(mapper: (DataSnapshot) -> List<T>): Observable<List<T>> {
        return Observable.create { emitter ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val items = mapper(snapshot)
                        emitter.onNext(items) // Emit the complete list, not individual items
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            }
            
            addValueEventListener(listener)
            
            // Remove listener when Observable is disposed
            emitter.setCancellable {
                removeEventListener(listener)
            }
        }
    }
    
    // Real-time Observable for DatabaseReference that emits complete lists
    protected fun <T: Any> DatabaseReference.asRealtimeObservable(mapper: (DataSnapshot) -> List<T>): Observable<List<T>> {
        return Observable.create { emitter ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val items = mapper(snapshot)
                        emitter.onNext(items) // Emit the complete list, not individual items
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            }
            
            addValueEventListener(listener)
            
            // Remove listener when Observable is disposed
            emitter.setCancellable {
                removeEventListener(listener)
            }
        }
    }
    
    // Real-time Observable for single objects (like User)
    protected fun <T: Any> DatabaseReference.asRealtimeObservableObject(mapper: (DataSnapshot) -> T?): Observable<T> {
        return Observable.create { emitter ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val item = mapper(snapshot)
                        if (item != null) {
                            emitter.onNext(item)
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            }
            
            addValueEventListener(listener)
            
            // Remove listener when Observable is disposed
            emitter.setCancellable {
                removeEventListener(listener)
            }
        }
    }
} 