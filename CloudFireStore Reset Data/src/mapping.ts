import { DocumentData } from "firebase-admin/firestore";
import { ICategories, ICategory, IExercise, IExercise_Detail } from "./type_firebase";
import { JS_Categories, JS_Image } from "./type_json";

function JS_ImageToString(jsImage: JS_Image[]): string | undefined {
    if (jsImage.length > 0) {
        return jsImage[0].file.split('.').slice(0, -1).join('.');
    }
}

export function JsonToData(jsData: JS_Categories): ICategories {
    const fbData: ICategories = {};
    for (const category of jsData) {
        fbData[category.title] = {
            title: category.title,
            image: JS_ImageToString(category.image),
            description: category.description,
            exercises: {},
            asDocumentData: () => CategoryToDocumentData(fbData[category.title])
        };
        for (const subcategory of category.subcategories) {

            for (const exercise of subcategory.exercises) {
                fbData[category.title].exercises[exercise.title] = {
                    title: exercise.title,
                    subcategory: subcategory.title,
                    image: JS_ImageToString(exercise.image),
                    short_description: exercise.short_description,
                    description: exercise.description,
                    details: exercise.sections.map((section, index) => {
                        return {
                            order: index,
                            title: section.title,
                            description: section.content,
                            video: section.video ? section.video[0] : undefined
                        }
                    }),
                    asDocumentData: () => ExerciseToDocumentData(fbData[category.title].exercises[exercise.title])
                };
            }
        }
    }
    return fbData;

}

function CategoryToDocumentData(category: ICategory): DocumentData {
    return {
        title: category.title,
        image: category.image,
        description: category.description
    }
}

function ExerciseToDocumentData(exercise: IExercise): DocumentData {
    return {
        title: exercise.title,
        subcategory: exercise.subcategory,
        image: exercise.image,
        short_description: exercise.short_description,
        description: exercise.description,
        details: exercise.details.map(ExerciseDetailToDocumentData)
    }
}

function ExerciseDetailToDocumentData(detail: IExercise_Detail): DocumentData {
    return {
        order: detail.order,
        title: detail.title,
        description: detail.description,
        video: detail.video ? detail.video : ""
    }
}
