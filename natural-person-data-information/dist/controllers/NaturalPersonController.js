import { RequestError } from "@hono/node-server";
import NaturalPersonDB from "../db/NaturalPersonDB.js";
class NaturalPersonController {
    async reteiveNaturalPerson(document) {
        let person = await NaturalPersonDB.getNaturalPerson(document);
        if (!person) {
            throw new RequestError("Natural Person not found");
        }
        return person;
    }
}
export default new NaturalPersonController();
