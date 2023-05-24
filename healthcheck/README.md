# Health Checks

This project prototypes [Kubernetes healthcheck endpoints](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/). 

The current server status checks return status information within the body, always returning 200 once the platform/server is up.

This prototype will simply return 200 if all is good, and other standard codes otherwise.

## Build & usage
* `./gradlew build`
* `cd healthcheck/build/libs`
* `cp ../../src/main/resources/truststore.p12 .`
* `java -jar ./healthcheck.jar`

Then configure and use the server as normal.

Note that only a minimal chassis is configured, so only
basic services can be used.

## API endpoints

`GET /open-metadata/health/platform`

`GET /open-metadata/health/server/(serverName}`

### Examples:

#### Server not running
```
➜  egeria-cloudnative git:(main) ✗ http --verify=no GET https://localhost:9443/open-metadata/health/server/mds1
HTTP/1.1 503 
Connection: close
Content-Length: 19
Content-Type: text/plain;charset=UTF-8
Date: Wed, 24 May 2023 22:10:40 GMT

Service Unavailable
```
Server running:
```
➜  egeria-cloudnative git:(main) ✗ http --verify=no GET https://localhost:9443/open-metadata/health/server/mds1
HTTP/1.1 200 
Connection: keep-alive
Content-Length: 2
Content-Type: text/plain;charset=UTF-8
Date: Wed, 24 May 2023 22:11:09 GMT
Keep-Alive: timeout=60

OK
```
## Caveats & Notes

## See also:
 * [Discussion](https://github.com/odpi/egeria/discussions/7686)
 * [Documentation PR](https://github.com/odpi/egeria-docs/pull/775)
