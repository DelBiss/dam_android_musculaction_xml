import { DocumentData } from "firebase-admin/firestore";

type asDocumentDataFunction = () => DocumentData;

export interface Childs<T> {
    [k: string]: T
}

export interface ICategories extends Childs<ICategory> {}


export interface ICategory  {
    asDocumentData (): DocumentData
    title: string
    image?: string
    description: string
    exercises: Childs<IExercise>
}

export interface IExercise  {
    asDocumentData (): DocumentData
    title: string
    image?: string
    short_description: string
    subcategory: string
    description: string
    details: IExercise_Detail[]
}

export interface IExercise_Detail {
    order: number
    title: string
    description: string
    video?: string
  }


    