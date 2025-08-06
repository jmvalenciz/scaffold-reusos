import z from "zod";
import { NaturalPersonIdValidator } from "./NaturalPersonId.js";

const currencies = [
    "COP"
] as const;

export const accountTypes = [
    "CORRIENTE",
    "AHORROS"
] as const;

export var AccountValidator = z.object({
    id: z.uuid(),
    type: z.enum(accountTypes),
    owner: NaturalPersonIdValidator,
    currency: z.enum(currencies),
    number: z.int().min(0)
});

export type Account = z.infer<typeof AccountValidator>;