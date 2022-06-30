package ca.philrousse.android02.musculaction.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryExercicesViewsList(
    @Embedded
    val category:Category=Category(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Subcategory::class
    )
    val childs: List<CategoryExercicesViewsItem> = listOf()
)

data class CategoryExercicesViewsItem(
    @Embedded
    val subcategory: Subcategory = Subcategory(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Exercise::class
    )
    val exercises:List<Exercise> = listOf()
)

data class ExerciceViewList(
    @Embedded
    val exercise: Exercise = Exercise(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetail::class
    )
    val sections:List<ExerciceViewItem> = listOf()
)

data class ExerciceViewItem(
    @Embedded
    val detail:ExerciseDetail = ExerciseDetail(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetailVideo::class
    )
    val videos:List<ExerciseDetailVideo> = listOf()
)