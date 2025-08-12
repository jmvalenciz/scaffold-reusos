import { readFileSync } from "fs";
import type { Account } from "../schemas/Account.js";

class AccountDB{

    db: Map<string, Account>;

    constructor(){
        var data: Account[] = JSON.parse(readFileSync('./static/data.json', 'utf8'));
        this.db = new Map();
        for(let i of data){
            this.db.set(i.id, i);
        }
    }

    async getAccount(id: string): Promise<Account|undefined>{
        return this.db.get(id);
    }

    async updateAccountBalance(id: string, amount: number): Promise<Account|undefined>{
        const account = this.db.get(id);
        if(!account){
            return undefined;
        }
        account.number += amount;
        return account;
    }
}

export default new AccountDB();