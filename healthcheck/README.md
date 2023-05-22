# Health Checks

This project prototypes [Kubernetes healthcheck endpoints](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/). 

The current server status checks return status information within the body, always returning 200 once the platform/server is up.

This prototype will simply return 200 if all is good, and other standard codes otherwise.

## Build & usage
`./gradlew build` then run the server chassis as usual, from the file `build/libs/healthcheck-all.jar`

## API endpoints

`GET /open-metadata/healthcheck/platform/health`
## See also:
 * [Discussion](https://github.com/odpi/egeria/discussions/7686)
 * [Documentation PR](https://github.com/odpi/egeria-docs/pull/775)