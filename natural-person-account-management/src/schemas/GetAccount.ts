import z from "zod";
import { accountTypes } from "./Account.js";


export var GetAccountValidator = z.object({
    id: z.uuid(),
    type: z.enum(accountTypes)
});

export type GetAccount = z.infer<typeof GetAccountValidator>;