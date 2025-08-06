import { serve } from '@hono/node-server'
import { serveStatic } from '@hono/node-server/serve-static';
import { Hono } from 'hono'

const app = new Hono()

app.use('/static/*', serveStatic({ root: './' })); 

app.get('/health', (c) => {
  c.status(200);
  return c.text('')
})

serve({
  fetch: app.fetch,
  port: 3001
}, (info) => {
  console.log(`Server is running on http://localhost:${info.port}`)
})
