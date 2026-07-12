# Architectural constraints

- Strapi and frontend Node.js compatibility ranges differ.
- Secrets remain host-local and are never overwritten by deployment.
- The deployment is tied to the klymene host layout and internal registry.
- Public routes must support both German and English.
