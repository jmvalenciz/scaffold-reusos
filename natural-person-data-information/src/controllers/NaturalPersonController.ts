import { RequestError } from "@hono/node-server";
import NaturalPersonDB from "../db/NaturalPersonDB.js";
import type { NaturalPerson } from "../schemas/NaturalPerson.js";
import type { NaturalPersonId } from "../schemas/NaturalPersonId.js";
import BusinessException from "../utils/BusinessException.js";

class NaturalPersonController{
    async reteiveNaturalPerson(document: NaturalPersonId): Promise<NaturalPerson>{
        let person = await NaturalPersonDB.getNaturalPerson(document);
        if(!person){
            throw new BusinessException("Natural Person not found", 404, "BE001");
        }
        return person;
    }
}

export default new NaturalPersonController();