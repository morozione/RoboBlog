package com.morozione.roboblog.database

import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.morozione.roboblog.Constants
import com.morozione.roboblog.entity.Blog
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class BlogDao : BaseDao() {
    private val firebaseReference = Firebase.database.reference.child(Constants.DATABASE_BLOG)

    fun create(blog: Blog): Completable {
        val id = firebaseReference.push().key
            ?: return Completable.error(Exception("Failed to generate ID"))
        blog.id = id
        blog.userId = UserDao.getCurrentUserId()
        blog.rating = 0
        blog.date = System.currentTimeMillis()
        return firebaseReference.child(id).asCompletable(blog)
    }

    fun update(blog: Blog): Completable {
        return firebaseReference.child(blog.id).asCompletable(blog)
    }

    fun getBlogs(): Observable<List<Blog>> {
        return firebaseReference.orderByChild("date").asRealtimeObservable { snapshot ->
            val blogs = mutableListOf<Blog>()
            for (child in snapshot.children) {
                child.getValue(Blog::class.java)?.let { blog ->
                    blog.id = child.key!!
                    blogs.add(blog)
                }
            }
            // Sort by date descending (newest first) and return the sorted list
            blogs.sortByDescending { it.date }
            blogs
        }.share() // Share the Observable to avoid multiple Firebase listeners
    }

    fun getBlogsByUserId(userId: String): Observable<List<Blog>> = firebaseReference
        .orderByChild(Constants.BLOG_USER_ID).equalTo(userId)
        .asRealtimeObservable { snapshot ->
            val blogs = mutableListOf<Blog>()
            for (child in snapshot.children) {
                child.getValue(Blog::class.java)?.let { blog ->
                    blog.id = child.key!!
                    blogs.add(blog)
                }
            }
            // Sort by date descending (newest first)
            blogs.sortByDescending { it.date }
            blogs
        }.share() // Share the Observable to avoid multiple Firebase listeners

    fun getBlogsById(id: String): Single<Blog> = firebaseReference
        .child(id).asSingle { snapshot ->
            snapshot.getValue(Blog::class.java)?.apply {
                this.id = snapshot.key ?: id
            }
        }

    fun removeBlog(id: String): Completable {
        return Completable.create { emitter ->
            firebaseReference.child(id).removeValue()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }
    }

    fun appreciateBlog(blog: Blog, userId: String, rating: Int): Completable {
        val userDao = UserDao()
        val blogRef = firebaseReference.child(blog.id)

        var ratingDifference: Int = 0

        return Completable.fromAction {
            // Get the previous rating from this user (if any)
            val previousRating = blog.getAppreciatedStatusByUser(userId) ?: 0
            
            // Calculate the difference
            ratingDifference = rating - previousRating
            
            // Update the blog's appreciated peoples map
            blog.appreciatedPeoples[userId] = rating
            
            // Update the blog's total rating
            blog.rating += ratingDifference
        }.andThen(
            // Update the blog in Firebase with the new appreciation data
            Completable.merge(listOf(
                // Update the appreciatedPeoples field
                blogRef.child("appreciatedPeoples").child(userId).asCompletable(rating),
                // Update the blog's total rating
                blogRef.child("rating").asCompletable(blog.rating),
                // Update the blog author's rating (give them points for getting appreciation)
                if (ratingDifference != 0) {
                    userDao.changeValue(blog, ratingDifference, "rating")
                } else {
                    Completable.complete()
                }
            ))
        )
    }
}
