import z from "zod";
import { accountTypes } from "./Account.js";


export const UpdateAccountBalanceValidator = z.object({
    id: z.uuid(),
    type: z.enum(accountTypes),
    amount: z.int()
});

export type UpdateAccountBalance = z.infer<typeof UpdateAccountBalanceValidator>;