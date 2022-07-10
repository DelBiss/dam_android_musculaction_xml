const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore');

const fs = require('fs');

const serviceAccount = require('./dam-android02-musculaction-6a25aace2dbb.json');

initializeApp({
    credential: cert(serviceAccount)
});
const path = require('path')
const db = getFirestore();


const jsonFile = '../data/local/src/main/assets/muculaction_data.json';

const isDebug = false

async function parseDocument(obj, docRef) {
    const PromiseList = [];
    const doc = {};
    for (const key in obj) {
        if (Object.hasOwnProperty.call(obj, key)) {
            const element = obj[key];
            if (typeof element === 'object') {

                const collectionRef = docRef.collection(key);
                PromiseList.push(parseCollection(element, collectionRef, key));

            } else {
                doc[key] = element;

                if (isDebug) {
                    docRef[key] = typeof element;
                }
            }
        }
    }

    if (Object.keys(doc).length > 0) {
        PromiseList.push(docRef.set(doc));
    }

    return Promise.all(PromiseList);
}

async function parseCollection(obj, collectionRef, collectionName) {
    const PromiseList = [];
    if (obj instanceof Array) {
        for (let i = 0; i < obj.length; i++) {
            let documentName = collectionName + "_" + i;
            if (Object.hasOwnProperty.call(obj[i], "title")) {
                documentName = i + "_" + obj[i].title;
            }

            const docRef = collectionRef.doc(documentName);
            if (typeof obj[i] === 'object') {
                PromiseList.push(parseDocument(obj[i], docRef));
            } else {
                PromiseList.push(docRef.set({ "value": obj[i] }))
            }

        }
    } else {
        console.log("Parse Collection: Object is not an array");
    }

    return Promise.all(PromiseList);
}

async function parseJson_to_Firebase(db, data) {

    return parseDocument(data, db)
}

async function deleteAll(db, initialCollection) {
    const collectionRef = db.collection(initialCollection);

    return Promise.resolve(console.log("Deleting data from " + initialCollection))
        .then(() => {
            return findCollection(db, collectionRef)
                .then(() => {
                    console.log(initialCollection + ' Data deleted');
                })
                .catch(err => {
                    console.log('Error while deleting collection: ', err);
                });
        })
}

async function findCollection(db, collectionRef) {
    const snapshot = await collectionRef.get();
    const allPromesse = [];
    const batch = db.batch();
    snapshot.forEach((doc) => {
        promesse = doc.ref.listCollections()
            .then(collections => {
                const collectIds = []
                collections.forEach(collection => {
                    collectIds.push(findCollection(db, collection))
                })
                return Promise.all(collectIds)
            });
        allPromesse.push(batch.delete(doc.ref));
        allPromesse.push(promesse)
    });
    allPromesse.push(batch.commit());
    return Promise.all(allPromesse);
}

async function resetDatabase(db, data) {
    rootCollection = []
    for (const collectionKey in data) {
        if (Object.hasOwnProperty.call(data, collectionKey)) {
            rootCollection.push(collectionKey)
        }
    }

    await Promise.all(rootCollection.map(collection => {
        return deleteAll(db, collection)
    })).then(() => {
        console.log('All data deleted');
    }).catch(err => {
        console.log('Error while deleting collection: ', err);
    });

    await parseJson_to_Firebase(db, data).then(() => {
        console.log('Data added');
    }).catch(err => {
        console.log('Error while adding data: ', err);
    });

}

let rawdata = fs.readFileSync(jsonFile);
let data = { 'categories': JSON.parse(rawdata) };

resetDatabase(db, data);