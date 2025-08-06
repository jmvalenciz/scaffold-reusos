import z from "zod";
import { NaturalPersonIdValidator } from "./NaturalPersonId.js";
export var NaturalPersonValidator = z.object({
    document: NaturalPersonIdValidator,
    fullName: z.string(),
    status: z.enum(["ACTIVE", "BLOCKED"]),
    accounts: z.array(z.string())
});
