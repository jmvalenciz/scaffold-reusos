FROM node:20-slim AS builder

WORKDIR /opt/app

COPY package*.json .

# Clean Install (including devDependencies)
RUN npm ci --legacy-peer-deps

COPY . .
RUN npm run build

RUN npm prune --production --legacy-peer-deps


FROM node:20-slim AS runtime

ARG APP_USER=appuser
ARG APP_HOME=/opt/app
WORKDIR ${APP_HOME}

ENV NODE_ENV=production
ENV PORT=3000

COPY --from=builder ${APP_HOME}/dist ./dist
COPY --from=builder ${APP_HOME}/node_modules ./node_modules
COPY --from=builder ${APP_HOME}/static ./static
COPY --from=builder ${APP_HOME}/package*.json ./

RUN useradd -ms /bin/bash ${APP_USER}
USER ${APP_USER}

EXPOSE 3000

CMD ["node", "dist/src/index.js"]