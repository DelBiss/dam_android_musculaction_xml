import { initializeApp, cert, ServiceAccount } from "firebase-admin/app";
import { CollectionReference, Firestore, getFirestore } from "firebase-admin/firestore";
import fs from "fs";
import { BatchProcessor } from "./batchProcessor";

import * as serviceAccount from './dam-android02-musculaction-6a25aace2dbb.json';
import { JsonToData } from "./mapping";
import { Childs, ICategories, IExercise, IExercise_Detail } from "./type_firebase";
import { JS_Categories } from "./type_json";

const resolve = require('path').resolve


initializeApp({
    credential: cert(serviceAccount as ServiceAccount)
});

const jsonFile = '../data/local/src/main/assets/musculaction_data.json';
const isDebug = false;
const jsonData: JS_Categories = JSON.parse(fs.readFileSync(jsonFile, 'utf8'));
const firebaseData = JsonToData(jsonData);

const db = getFirestore();


UpdateDatabase(db, firebaseData);

async function UpdateDatabase(db: Firestore, firebaseData: ICategories) {
    const batch = new BatchProcessor(db, isDebug);
    const collection = db.collection('categories');
    console.log('DeleteCollection');
    await DeleteCollection(collection, batch);
    await batch.commit();
    console.log('parseCategory');
    await parseCategory(firebaseData, collection, batch);
    await batch.commit();
}

async function parseCategory(firebaseData: ICategories, collection: CollectionReference, batch: BatchProcessor) {
    for (const key in firebaseData) {
        if (Object.prototype.hasOwnProperty.call(firebaseData, key)) {
            const category = firebaseData[key];
            const ref = collection.doc(key);
            await batch.add(ref, category.asDocumentData());
            parseExercise(category.exercises, ref.collection('exercises'), batch);
        }
    }
}

async function parseExercise(firebaseData: Childs<IExercise>, collection: CollectionReference, batch: BatchProcessor) {
    for (const key in firebaseData) {
        if (Object.prototype.hasOwnProperty.call(firebaseData, key)) {
            const exercise = firebaseData[key];
            const ref = collection.doc(key);
            await batch.add(ref, exercise.asDocumentData());
            parseDetail(exercise.details, ref.collection('details'), batch);
        }
    }
}

async function parseDetail(firebaseData: Childs<IExercise_Detail>, collection: CollectionReference, batch: BatchProcessor) {
    for (const key in firebaseData) {
        if (Object.prototype.hasOwnProperty.call(firebaseData, key)) {
            const detail = firebaseData[key];
            const ref = collection.doc(key);
            await batch.add(ref, detail.asDocumentData());
        }
    }
}
async function DeleteCollection(collectionRef: CollectionReference, batch: BatchProcessor) {
    const query = collectionRef.limit(1000);
    const promesse = collectionRef.get()
    .then(
        async snapshot => {
            return await Promise.all(
                snapshot.docs.map(
                    async doc => {
                        await batch.delete(doc.ref);
                        const collections = await doc.ref.listCollections();
                        return await Promise.all(
                            collections.map(
                                collection => {
                                    return DeleteCollection(collection, batch);
                                }
                            )
                        );
                    }
                )
            )
        }
    )
    return promesse;
    // for (const doc of querySnapshot.docs) {
    //     const collections = await doc.ref.listCollections().then(async collections => {
    //         return Promise.all(collections.map( collection => {
    //              return DeleteCollection(collection, batch);
    //         }))
    //     });
    //     batch.delete(doc.ref);
    // }
}