# Deployment

1. Jenkins validates, builds, and pushes production images to Harbor.
2. Repository contents are synchronized to `/srv/portfolio` while preserving secrets.
3. Affected services are restarted and health-checked.
4. nginx cache is refreshed and Playwright smoke tests validate the deployment.

Documentation-only changes must never execute deployment or publication stages.
