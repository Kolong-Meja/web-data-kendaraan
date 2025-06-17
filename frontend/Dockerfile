# build stage.
FROM node:22-alpine AS builder

WORKDIR /frontend

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build

# runtime stage.
FROM nginx:alpine

COPY --from=builder /frontend/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1
  
CMD ["nginx", "-g", "daemon off;"]