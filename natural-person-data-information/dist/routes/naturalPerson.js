import { Hono } from "hono";
import { describeRoute } from "hono-openapi";
import { NaturalPersonValidator } from "../schemas/NaturalPerson.js";
import { resolver, validator } from "hono-openapi/zod";
import { NaturalPersonIdValidator } from "../schemas/NaturalPersonId.js";
import NaturalPersonController from "../controllers/NaturalPersonController.js";
var router = new Hono();
router.post("/retreive-customer-data", describeRoute({
    description: "Retreive Natural Person data",
    tags: ["Natural Person"],
    responses: {
        200: {
            description: "Natural Person data",
            content: {
                'application/json': {
                    schema: resolver(NaturalPersonValidator)
                }
            }
        }
    }
}), validator('json', NaturalPersonIdValidator, (err, c) => {
    if (!err.success) {
        c.status(400);
        console.log(err.error);
        return c.json({ error: "unable to parse body", message: JSON.parse(err.error.message) });
    }
}), async (c) => {
    var document = await c.req.json();
    try {
        let person = NaturalPersonController.reteiveNaturalPerson(document);
        c.status(200);
        return c.json(person);
    }
    catch (e) {
        c.status(400);
        return c.json({
            error: "unable to handle request",
            message: e.message
        });
    }
});
export default router;
