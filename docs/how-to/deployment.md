# Deployment

The Herrenabend Locationfinder is a static Single Page Application (SPA) that can be deployed anywhere static files are supported.

## Deployment via Podman

This project is configured to be deployed via Podman (or Docker) using `podman-compose`.

1. Ensure your `.env` file contains the Google API key:
   ```env
   PUBLIC_GOOGLE_PLACES_API_KEY=YOUR_KEY
   ```
2. Build and start the container:
   ```bash
   podman-compose up -d --build
   ```
3. The application will be served via Nginx and exposed on `http://127.0.0.1:8205` by default.

### How it works

- The `Containerfile` uses a multi-stage build.
- **Stage 1 (Node.js)**: Installs dependencies and runs `npm run build`. The API key is baked into the static bundle.
- **Stage 2 (Nginx)**: Copies the built `dist/` directory into a lightweight `nginx:alpine` container to serve the static files securely and efficiently.

## Alternative Deployment

You can also deploy the application on platforms like **Vercel**, **Netlify**, or **GitHub Pages**:
1. Connect the GitHub repository to the platform.
2. Set the build command to `npm run build`.
3. Set the publish directory to `dist/`.
4. Add the `PUBLIC_GOOGLE_PLACES_API_KEY` to the environment variables in the platform's dashboard.
