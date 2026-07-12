# 0002: Rewrite as Astro/React Single Page Application

## Status

Accepted

## Date

2026-07-12

## Context

The legacy Java CLI application was outdated (unmaintained for 12 years) and not user-friendly. A modern, accessible, and responsive interface was required that users could open directly on their mobile phones.

## Decision drivers

- Responsive and accessible user interface (WCAG compliance)
- Automatic dark/light mode detection
- Direct sharing capability on mobile devices without relying on server-side emails
- Maintainability and modern web standards

## Decision

Rewrite the application as a client-side Single Page Application (SPA) using Astro, React, and Tailwind CSS v4. Replace the email functionality with the native Web Share API and URL Deeplinking.

## Rationale

Astro provides an excellent foundation for static sites, React allows for interactive components (fetching API data, handling state), and Tailwind CSS ensures rapid, responsive styling. Using a SPA architecture removes the need for a backend server or email infrastructure, reducing operational complexity.

## Consequences

- The Java codebase is completely removed.
- The application requires a web server to host static files.
- Users can share locations directly via their native mobile sharing dialog.
- The Google Places API key must be securely provided at build time or restricted via HTTP referrers since it is exposed to the client.
