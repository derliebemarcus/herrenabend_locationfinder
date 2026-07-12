# 0003: Containerized Deployment with Podman

## Status

Accepted

## Date

2026-07-12

## Context

The application needs a reliable and reproducible way to be deployed, matching the user's operational preferences for containerization. 

## Decision drivers

- Easy and reproducible local and production environments
- Lightweight serving of static assets
- Compatibility with existing Podman infrastructure

## Decision

Use a multi-stage `Containerfile` and `docker-compose.yml` (compatible with `podman-compose`) to build the Astro application using Node.js and serve the resulting static files using a lightweight Nginx container (`nginx:alpine`).

## Rationale

Nginx is highly optimized for serving static content. The multi-stage build ensures that the final container image only contains the compiled static assets and the web server, without the overhead of Node.js or development dependencies, resulting in a tiny footprint and enhanced security.

## Consequences

- Deployment requires a container engine (Podman or Docker).
- The `PUBLIC_GOOGLE_PLACES_API_KEY` must be passed as a build argument during the image build process so Astro can bake it into the static bundle.
- The application will be exposed on a defined port (default 8205) and can easily be put behind a reverse proxy.
