import { DocumentReference, Firestore, WriteBatch } from "firebase-admin/firestore";

const MaxBatchSize = 400;
export class BatchProcessor {
    db: Firestore;
    batch: WriteBatch | undefined;
    isDebug: boolean;
    count: number;

    constructor(db: Firestore, isDebug?: boolean) {
        this.count = 0;
        this.db = db;
        this.batch = db.batch();
        this.isDebug = isDebug ? isDebug : false;
    }

    async add(ref: DocumentReference, data: any) {
        if(this.count>MaxBatchSize){
            await this.commit();
        }
        if (this.batch && !this.isDebug) {
            this.count++;
            this.batch.set(ref, data);
        } 
            process.stdout.write(`${this.count} A ${ref.path}\r`);
            // console.log(`ADD ${ref.path} +++ "${data.title}"`);
        
    }
    
    async delete(ref: DocumentReference) {
        if(this.count>MaxBatchSize){
            await this.commit();
        }
        if (this.batch && !this.isDebug) {
            this.count++;
            this.batch.delete(ref);
        }
            process.stdout.write(`${this.count} D ${ref.path}\r`);
            // console.log(`DELETE ${ref.path} ---`);
        
    }

    async commit() {
        if (this.batch && !this.isDebug) {
            const oldBatch = this.batch;
            this.batch = this.db.batch();
            this.count=0;
            await oldBatch.commit();
        } 
            console.log('\nCOMMIT\n');
        
    }
    
}