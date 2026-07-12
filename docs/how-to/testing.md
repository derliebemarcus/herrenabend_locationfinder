# Testing

Run the repository's local quality checks before opening a pull request:

```bash
cd frontend && npm ci
cd frontend && npm run lint
cd frontend && npm run typecheck
cd frontend && npm run test:run
cd frontend && npm run build
cd strapi && npm ci && npm run build
```

Jenkins remains authoritative for the complete quality, analysis, security, and release contract.
