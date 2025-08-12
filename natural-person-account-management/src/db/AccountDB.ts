import { readFileSync } from "fs";
import type { Account } from "../schemas/Account.js";

class AccountDB{

    db: Record<string, Account>;

    constructor(){
        var data: Account[] = JSON.parse(readFileSync('./static/data.json', 'utf8'));
        this.db = {};
        for(let i of data){
            this.db[i.id] = i;
        }
    }

    async getAccount(id: string): Promise<Account>{
        return this.db[id];
    }
}

export default new AccountDB();