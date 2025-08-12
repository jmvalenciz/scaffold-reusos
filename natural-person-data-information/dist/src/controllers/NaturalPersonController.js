import { RequestError } from "@hono/node-server";
import NaturalPersonDB from "../db/NaturalPersonDB.js";
import BusinessException from "../utils/BusinessException.js";
class NaturalPersonController {
    async reteiveNaturalPerson(document) {
        let person = await NaturalPersonDB.getNaturalPerson(document);
        if (!person) {
            throw new BusinessException("Natural Person not found", 404, "BE001");
        }
        return person;
    }
}
export default new NaturalPersonController();
