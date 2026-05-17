# Security Policy

## Supported Versions

Security fixes are provided for the latest code on the `main` branch.

## Reporting a Vulnerability

If you discover a security vulnerability, please report it privately:

- Do not open a public issue with exploit details.
- Contact project maintainers via your internal communication channel.
- Include reproduction steps, impact, and affected components.

We will acknowledge the report as soon as possible and coordinate remediation.

## Security Best Practices for This Project

- Never commit `.env`, `.env.production`, secrets, or private keys.
- Rotate `JWT_SECRET` and service credentials regularly.
- Use different secrets for development, staging, and production.
- Keep dependencies updated and review container images before deployment.
