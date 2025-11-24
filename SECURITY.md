# Security Policy

## Reporting a Vulnerability

If you discover a security vulnerability in xapi-java, please report it to the project maintainers. Please do not create a public GitHub issue for security vulnerabilities.

## Security Best Practices

### Credential Management

The xAPI client library supports multiple authentication methods for connecting to Learning Record Stores (LRS):

#### Configuration Properties

The library accepts the following configuration properties:

- `xapi.client.username` - Username for basic authentication
- `xapi.client.password` - Password for basic authentication  
- `xapi.client.authorization` - Full authorization header value (takes precedence over username/password)

#### Security Recommendations

**DO NOT** hardcode credentials in application.properties files:

```properties
# ❌ NEVER DO THIS IN PRODUCTION
xapi.client.username = admin
xapi.client.password = password
```

**DO** use one of these secure approaches:

1. **Environment Variables** (Recommended)
   ```properties
   xapi.client.username = ${XAPI_USERNAME}
   xapi.client.password = ${XAPI_PASSWORD}
   ```

2. **External Configuration**
   - Use Spring Cloud Config Server
   - Use Kubernetes Secrets
   - Use AWS Secrets Manager, Azure Key Vault, or similar services

3. **Authorization Header with Tokens**
   ```properties
   xapi.client.authorization = ${XAPI_AUTH_TOKEN}
   ```

4. **System Properties**
   ```bash
   java -jar myapp.jar \
     --xapi.client.username=${XAPI_USERNAME} \
     --xapi.client.password=${XAPI_PASSWORD}
   ```

### Sample Applications

The sample applications in the `samples/` directory contain hardcoded credentials for demonstration purposes only:

```properties
xapi.client.username = admin
xapi.client.password = password
```

**These are example credentials and MUST NOT be used in production environments.**

Before running samples against a real LRS:
1. Copy the `application.properties` file
2. Replace the credentials with your actual LRS credentials
3. Add the properties file to `.gitignore` to prevent accidental commits
4. Or use environment variables to override the properties

### HTTPS Usage

The xAPI client library uses Spring WebClient which supports HTTPS by default. Always use HTTPS URLs when connecting to production LRS endpoints:

```properties
# ✅ Good - HTTPS
xapi.client.baseUrl = https://lrs.example.com/xapi/

# ❌ Bad - HTTP (only for local development)
xapi.client.baseUrl = http://localhost:8080/xapi/
```

### Dependency Security

This project uses:
- Dependabot for automated dependency updates
- GitHub Actions for CI/CD security scanning
- SonarCloud for static code analysis

Regular dependency updates help maintain security. Review and merge Dependabot PRs promptly.

## Security Hotspots Review

### Hardcoded Credentials (Addressed)

**Issue**: Sample application.properties files contained hardcoded credentials without security warnings.

**Resolution**: Added prominent security warnings to all sample configuration files explaining that credentials are for demo purposes only and providing guidance on secure credential management.

**Status**: ✅ Resolved - Demo credentials are clearly marked with security warnings

### Additional Findings

No additional security hotspots requiring immediate action were identified during the review. The codebase follows security best practices:

- Uses Spring Security's built-in authentication mechanisms
- Employs HTTPS for external communications
- Validates all xAPI data against the specification
- Uses parameterized queries (no SQL injection vulnerabilities)
- No use of insecure cryptographic functions
- No dynamic class loading or unsafe deserialization

## Security Scan Results

This repository is regularly scanned using:
- **SonarCloud**: For code quality and security analysis
- **GitHub CodeQL**: For security vulnerability detection
- **Dependabot**: For dependency vulnerability alerts

## Supported Versions

Security updates are provided for the latest stable release. Users are encouraged to upgrade to the latest version to receive security fixes.

## Additional Resources

- [xAPI Specification Security Considerations](https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-About.md#def-security)
- [Spring Boot Security Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.security)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
