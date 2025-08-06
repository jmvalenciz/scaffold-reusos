import { readFileSync } from "fs";
import type { NaturalPerson } from "../schemas/NaturalPerson.js";
import type { NaturalPersonId } from "../schemas/NaturalPersonId.js";

class NaturalPersonDB {

    db: Record<string, NaturalPerson>;

    constructor(){
        var data: NaturalPerson[] = JSON.parse(readFileSync('./static/data.json', 'utf8'));
        this.db = {};
        for(let i of data){
            this.db[`${i.document.type}-${i.document.number}`] = i;
        }
    }

    async getNaturalPerson(document: NaturalPersonId): Promise<NaturalPerson>{
        return this.db[`${document.type}-${document.number}`];
    }
}

export default new NaturalPersonDB();