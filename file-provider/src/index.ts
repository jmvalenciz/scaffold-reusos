import { serve } from '@hono/node-server'
import { serveStatic } from '@hono/node-server/serve-static';
import { Hono } from 'hono'
import { logger } from 'hono/logger'

const PORT = parseInt(process.env.PORT??"3000") 

const app = new Hono()

app.use(logger())

app.use('/static/*', serveStatic({ root: './' })); // reads files from ./static

app.get('/health', (c) => {
  c.status(200);
  return c.text('')
})

serve({
  fetch: app.fetch,
  port: PORT
}, (info) => {
  console.log(`Server is running on http://localhost:${info.port}`)
})
