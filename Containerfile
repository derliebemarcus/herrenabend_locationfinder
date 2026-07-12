# Stage 1: Build
FROM node:22-slim AS build
WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm ci

# Copy the rest of the application
COPY . .

# Build the Astro application (needs PUBLIC_GOOGLE_PLACES_API_KEY if using it at build time, 
# but since it's a client side SPA with PUBLIC_ env vars, Vite bakes them into the code)
ARG PUBLIC_GOOGLE_PLACES_API_KEY
ENV PUBLIC_GOOGLE_PLACES_API_KEY=$PUBLIC_GOOGLE_PLACES_API_KEY

RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine

# Copy the built static files to Nginx's default public directory
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
