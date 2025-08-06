import z from "zod";
import { NaturalPersonIdValidator } from "./NaturalPersonId.js";

export var NaturalPersonValidator = z.object({
    document: NaturalPersonIdValidator,
    fullName: z.string(),
    status: z.enum(["ACTIVE", "BLOCKED"] as const),
    accounts: z.array(z.string())
});

export type NaturalPerson = z.infer<typeof NaturalPersonValidator>;