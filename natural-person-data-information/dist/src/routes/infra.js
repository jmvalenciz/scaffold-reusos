import { Hono } from "hono";
import { describeRoute } from "hono-openapi";
var router = new Hono();
router.get('/health', describeRoute({
    description: "Healt check",
    tags: ["infra"],
    responses: {
        200: {
            description: "Healthy server"
        }
    }
}), c => {
    c.status(200);
    return c.text('');
});
export default router;
