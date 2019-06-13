package com.morozione.roboblog.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.morozione.roboblog.Constants
import com.morozione.roboblog.entity.Blog
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class BlogDao {
    private val firebaseReference =
        FirebaseDatabase.getInstance().reference.child(Constants.DATABASE_BLOG)

    fun create(blog: Blog): Completable = Completable.create { e ->
        val id = firebaseReference.push().key
        blog.id = id!!
        blog.userId = UserDao.getCurrentUserId()
        blog.rating = 0
        blog.date = System.currentTimeMillis()
        firebaseReference.child(id).setValue(blog).addOnCompleteListener {
            if (it.isSuccessful) {
                e.onComplete()
            } else {
                e.onError(Exception(it.exception.toString()))
            }
        }.addOnCanceledListener {
            e.onError(Exception())
        }
    }

    fun update(blog: Blog) = Completable.create { t ->
        firebaseReference.child(blog.id).ref.setValue(blog)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    t.onComplete()
                } else {
                    t.onError(Exception())
                }
            }
    }

    fun getBlogs(): Observable<Blog> = Observable.create { e ->
        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val blog = child.getValue(Blog::class.java)
                    blog?.let {
                        blog.id = child.key!!
                        e.onNext(blog)
                    }
                }
                e.onComplete()
            }

            override fun onCancelled(onError: DatabaseError) {
                e.onError(onError.toException())
            }
        })
    }

    fun getBlogsByUserId(userId: String): Observable<Blog> = Observable.create { e ->
        firebaseReference.orderByChild(Constants.BLOG_USER_ID).equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        val blog = child.getValue(Blog::class.java)
                        blog?.let {
                            e.onNext(blog)
                        }
                    }
                    e.onComplete()
                }

                override fun onCancelled(onError: DatabaseError) {
                    e.onError(onError.toException())
                }
            })
    }

    fun getBlogsById(id: String): Single<Blog> = Single.create { e ->
        firebaseReference.orderByKey().equalTo(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        val blog = child.getValue(Blog::class.java)
                        blog?.let {
                            e.onSuccess(blog)
                        }
                    }
                }

                override fun onCancelled(onError: DatabaseError) {
                    e.onError(onError.toException())
                }
            })
    }

    fun removeBlog(id: String) = Completable.create { emitter ->
        firebaseReference.orderByKey().equalTo(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
//                        val blog = child.getValue(Blog::class.java)
//                        blog?.appreciatedPeoples
                        child.ref.removeValue().addOnCompleteListener {
                            if (it.isSuccessful) {
                                emitter.onComplete()
                            } else {
                                emitter.onError(it.exception ?: Exception())
                            }
                        }
                    }
                }

                override fun onCancelled(onError: DatabaseError) {
                    emitter.onError(onError.toException())
                }
            })
//            if (it.isSuccessful) {
//                emitter.onComplete()
//            } else {
//                emitter.onError(it.exception ?: Exception())
//            }
    }

    fun appreciateBlog(blog: Blog, userId: String, rating: Int) = Completable.create { emitter ->
        blog.appreciatedPeoples[userId] = rating
        firebaseReference.child(blog.id).ref.setValue(blog)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception ?: Exception())
                }
            }
    }
}