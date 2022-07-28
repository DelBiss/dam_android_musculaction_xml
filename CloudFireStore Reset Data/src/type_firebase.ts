import { DocumentData } from "firebase-admin/firestore";

type asDocumentDataFunction = () => DocumentData;

export interface Childs<T> {
    [k: string]: T
}

export interface ICategories extends Childs<ICategory> {}


export interface ICategory  {
    asDocumentData (): DocumentData
    name: string
    image?: string
    description: string
    exercises: Childs<IExercise>
}

export interface IExercise  {
    asDocumentData (): DocumentData
    name: string
    image?: string
    short_description: string
    subcategory: string
    description: string
    details: Childs<IExercise_Detail>
}

export interface IExercise_Detail {
    asDocumentData (): DocumentData
    order: number
    name: string
    description: string
    video?: string
  }


    