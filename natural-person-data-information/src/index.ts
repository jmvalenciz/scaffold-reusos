import "./utils/environmentConfig.js";

import { serve } from '@hono/node-server'
import { swaggerUI } from '@hono/swagger-ui';
import { Hono } from 'hono'
import { openAPISpecs } from 'hono-openapi'
import { logger } from 'hono/logger'

import naturalPersonRouter from "./routes/naturalPerson.js";
import infraRouter from "./routes/infra.js";


const PORT:number = parseInt(process.env.PORT || "3000");

const app = new Hono()

app.use(logger())

app.get(
  '/openapi',
  openAPISpecs(app, {
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
  })
)

app.get("/apidoc", swaggerUI({ url: '/openapi' }))

app.route('/', naturalPersonRouter);
app.route('/', infraRouter);

serve({
  fetch: app.fetch,
  port: PORT
}, (info) => {
  console.log(`Server is running on http://localhost:${info.port}`)
})
