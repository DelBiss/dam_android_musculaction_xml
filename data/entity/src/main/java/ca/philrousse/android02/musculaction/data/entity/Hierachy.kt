package ca.philrousse.android02.musculaction.data.entity

import androidx.room.Embedded
import androidx.room.Relation


data class CategoryHierarchic (
    @Embedded
    val item:Category= Category(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Subcategory::class
    )
    val subcategories: List<SubcategoryHierarchic> = listOf()
)

data class SubcategoryHierarchic(
    @Embedded
    val item: Subcategory = Subcategory(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Exercise::class
    )
    val exercises:List<ExerciseHierarchic> = listOf()
)

data class ExerciseHierarchic(
    @Embedded
    val item: Exercise = Exercise(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetail::class
    )
    val exercise_details:List<ExercisesDetailHierarchic> = listOf()
)

data class ExercisesDetailHierarchic(
    @Embedded
    val item:ExerciseDetail = ExerciseDetail(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetailVideo::class
    )
    val videos:List<ExerciseDetailVideo> = listOf()
)

