package com.morozione.roboblog.entity

class Blog {
    var id = ""
    var userId = ""
    var title = ""
    var descrption = ""
    var icon : String? = null
    var rating = 0
    var date = 0L
    val appreciatedPeoples = hashMapOf<String, Int>()

    /**
     * 0 - if user didn't appreciate blog
     * -1 - if user disliked blog
     *  1 - if user liked blog
     */
    fun getAppreciatedStatusByUser(userId: String): Int? {
        if (appreciatedPeoples.keys.contains(userId)) {
            return appreciatedPeoples[userId]
        }

        return 0
    }
}