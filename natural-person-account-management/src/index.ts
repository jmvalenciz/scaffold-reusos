import "./utils/environmentConfig.js";

import { serve } from '@hono/node-server'
import { swaggerUI } from '@hono/swagger-ui';
import { Hono } from 'hono'
import { openAPISpecs } from 'hono-openapi'

import accountRouter from "./routes/account.js";
import infraRouter from "./routes/infra.js";
import { APP_NAME } from "./utils/config.js";
import { logger } from 'hono/logger'


const PORT:number = parseInt(process.env.PORT || "3000");

const app = new Hono()

app.use(logger())

app.get(
  '/openapi',
  openAPISpecs(app, {
    documentation: {
      info: {
        title: APP_NAME,
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

app.route('/', accountRouter);
app.route('/', infraRouter);

serve({
  fetch: app.fetch,
  port: PORT
}, (info) => {
  console.log(`Server is running on http://localhost:${info.port}`)
})
