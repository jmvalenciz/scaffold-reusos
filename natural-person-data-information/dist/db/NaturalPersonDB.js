import { readFileSync } from "fs";
class NaturalPersonDB {
    db;
    constructor() {
        this.db = JSON.parse(readFileSync('example/data.json', 'utf8'));
    }
    async getNaturalPerson(document) {
        return this.db[`${document.type}-${document.number}`];
    }
}
export default new NaturalPersonDB();
