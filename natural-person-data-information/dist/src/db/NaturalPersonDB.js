import { readFileSync } from "fs";
class NaturalPersonDB {
    db;
    constructor() {
        var data = JSON.parse(readFileSync('./static/data.json', 'utf8'));
        this.db = {};
        for (let i of data) {
            this.db[`${i.document.type}-${i.document.number}`] = i;
        }
    }
    async getNaturalPerson(document) {
        return this.db[`${document.type}-${document.number}`];
    }
}
export default new NaturalPersonDB();
