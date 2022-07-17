package ca.philrousse.android02.musculaction.data.entity

import android.content.Context
import android.graphics.drawable.Drawable
import ca.philrousse.android02.musculaction.data.entity.views.IListElement

interface IDataImage {
    fun getDrawable(context: Context, default: Drawable?): Drawable?
}

interface IDataCategory : IListElement {
    var description: String
    var imageID: String?
    val isUserGenerated: Boolean
}

interface IDataSubcategory : IListElement {
    val isUserGenerated: Boolean
}

interface IDataExercise : IListElement {
    var short_description: String
    var description: String
    var imageID: String?
    val isUserGenerated: Boolean
}

interface IDataExerciseDetail : IListElement {
    var description: String
    val isUserGenerated: Boolean
}

interface IDataExerciseDetailVideo {
    var videoUrl: String
    val id: Long?
    val isUserGenerated: Boolean
}