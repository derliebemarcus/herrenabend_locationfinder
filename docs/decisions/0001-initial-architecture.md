# 0001: Initial Java Architecture (Legacy)

## Status

Superseded by ADR 0002

## Date

2014-01-01 (Estimated)

## Context

A simple tool was needed to query Google Places API for finding bars and cafes for a "Herrenabend" (Gentlemen's evening) based on random coordinates, and send the results via email.

## Decision drivers

- Simple execution
- Email delivery for sharing
- Integration with Google Places API

## Considered options

1. Build a Java CLI tool
2. Use an existing web service
3. Create a mobile app

## Decision

Build the application as a standalone Java CLI using `org.json` for JSON parsing and Java Mail API for email delivery.

## Rationale

Java was a familiar and robust language for API integration and email delivery at the time.

## Consequences

- The application is not accessible via a browser.
- Requires a Java Runtime Environment (JRE) to run.
- No user interface other than the terminal/email.

## Risks

- Java requirement limits accessibility.
- API changes in Google Places might break the CLI integration without warning.

## References

- Initial project discussions
