package ca.philrousse.android02.musculaction.data.entity

import ca.philrousse.android02.musculaction.data.entity.table.*

import androidx.room.Embedded
import androidx.room.Relation


data class CategoryHierachy (
    @Embedded
    val item:ExercisesCategory= ExercisesCategory(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExercisesSubcategory::class
    )
    val childs: List<SubcategoryHierachy> = listOf()
)

data class SubcategoryHierachy(
    @Embedded
    val item: ExercisesSubcategory = ExercisesSubcategory(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Exercise::class
    )
    val childs:List<ExerciseHierachy> = listOf()
)

data class ExerciseHierachy(
    @Embedded
    val item: Exercise = Exercise(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetail::class
    )
    val childs:List<ExerciceDetailHierachy> = listOf()
)

data class ExerciceDetailHierachy(
    @Embedded
    val item:ExerciseDetail = ExerciseDetail(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetailVideo::class
    )
    val childs:List<ExerciseDetailVideo> = listOf()
)

