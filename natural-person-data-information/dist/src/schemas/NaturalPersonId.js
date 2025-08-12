import z from "zod";
var documentTypes = [
    "TIPDOC_FS000",
    "TIPDOC_FS001",
    "TIPDOC_FS002",
    "TIPDOC_FS003",
    "TIPDOC_FS004",
    "TIPDOC_FS005",
    "TIPDOC_FS006",
    "TIPDOC_FS007",
    "TIPDOC_FS008",
    "TIPDOC_FS009"
];
export var NaturalPersonIdValidator = z.object({
    number: z.int32().min(0),
    type: z.enum(documentTypes)
});
