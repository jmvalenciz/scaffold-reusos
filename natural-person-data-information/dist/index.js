import { serve } from '@hono/node-server';
import { swaggerUI } from '@hono/swagger-ui';
import { Hono } from 'hono';
import { describeRoute } from 'hono-openapi';
import { openAPISpecs } from 'hono-openapi';
import { resolver, validator } from 'hono-openapi/zod';
import { NaturalPersonValidator } from './schemas/NaturalPerson.js';
import { NaturalPersonIdValidator } from './schemas/NaturalPersonId.js';
import naturalPersonRouter from "./routes/naturalPerson.js";
import infraRouter from "./routes/infra.js";
const PORT = parseInt(process.env.PORT || "3000");
const app = new Hono();
app.get('/openapi', openAPISpecs(app, {
    documentation: {
        info: {
            title: 'Hono API',
            version: '1.0.0',
            description: 'Greeting API',
        },
        servers: [
            { url: `http://localhost:${PORT}`, description: 'Local Server' },
        ],
    },
}));
app.get("/apidoc", swaggerUI({ url: '/openapi' }));
app.route('/', naturalPersonRouter);
app.route('/', infraRouter);
serve({
    fetch: app.fetch,
    port: PORT
}, (info) => {
    console.log(`Server is running on http://localhost:${info.port}`);
});
