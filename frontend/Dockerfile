# build stage.
FROM node:22-alpine AS builder
WORKDIR /frontend
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# runtime stage.
FROM node:22-alpine AS runner
WORKDIR /frontend
COPY --from=builder /frontend/dist ./dist
COPY --from=builder /frontend/package*.json ./
RUN npm install -g serve

# non-root for security
RUN addgroup -S app && adduser -S -G app app
USER app

ENV PORT=3000
EXPOSE ${PORT}

HEALTHCHECK --interval=30s --timeout=5s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT}/ || exit 1

CMD ["serve", "-s", "dist", "-l", "3000"]