import AccountDB from "../db/AccountDB.js";
import type { Account } from "../schemas/Account.js";
import BusinessException from "../utils/BusinessException.js";

class AccountController {
    async getAccount(id: string, type: string): Promise<Account>{
        let account = await AccountDB.getAccount(id);

        if(!account){
            throw new BusinessException("Account not found", 404, "BE002");
        }

        if(account.type !== type){
            throw new BusinessException("Account type mismatch", 400, "BE003");
        }

        return account;
    }
}

export default new AccountController();