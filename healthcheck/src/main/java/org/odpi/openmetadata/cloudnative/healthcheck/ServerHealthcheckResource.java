/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.cloudnative.healthcheck;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.properties.ServerActiveStatus;
import org.odpi.openmetadata.adminservices.properties.ServerServicesStatus;
import org.odpi.openmetadata.adminservices.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.adminservices.server.OMAGServerOperationalServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * PlatformHealthcheckResource provides a simple healthcheck service for the
 * egeria platform.
 * <p>
 * This differs from the typical Egeria style in that the response is
 * returned as a simple HTTP code.
 * <p>
 * &ge;200 &lt;400 will be interpreted by a Kubernetes Healthcheck as successfull
 */
@RestController
@RequestMapping("/open-metadata/health")

@Tag(name = "Healthcheck Services",
        description = "The healthcheck services provide simple APIs to check that Egeria resources are available for use",
        externalDocs = @ExternalDocumentation(description = "Egeria cloud native prototypes", url = "https://github.com/odpi/egeria-cloudnative"))

public class ServerHealthcheckResource {
    private final OMAGServerOperationalServices opsAPI = new OMAGServerOperationalServices();

    /**
     * Return the origin of this server platform implementation.
     * <p>
     * Currently, always returns 200.
     * In future this may check status of auto-started or configured servers
     *
     * @return String text summary
     */
    @GetMapping(path = "/platform")

    @Operation(summary = "Get health of this OMAG Server Platform",
            description = "Return HTTP Status code representing health",
            responses = {
                    @ApiResponse(responseCode = "200", description = "platform is ready for work",
                            content = @Content(mediaType = "text/plain"))
            })

    public ResponseEntity<?> getPlatformHealth() {
        return new ResponseEntity<>("Platform is healthy", HttpStatus.OK);
    }


    /**
     * Return the status of the specified server
     * <p>
     * Note: No userid - skips security checks
     *
     * @param serverName server name
     * @return return
     */
    @GetMapping(path = "/server/{serverName}")

    @Operation(summary = "Get health of this OMAG Server Platform",
            description = "Return HTTP Status code representing health",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Server available",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(responseCode = "503", description = "Server not available",
                            content = @Content(mediaType = "text/plain"))
            })

    public ResponseEntity<?> getServerHealth(@PathVariable String serverName) {
        HttpStatus status;
        //HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        //
        // Normal reponse for this (json) is:
        //{
        //    "class": "OMAGServerStatusResponse",
        //        "relatedHTTPCode": 200,
        //        "serverStatus": {
        //    "serverName": "cocoMDS2",
        //            "serverType": "Metadata Server",
        //            "serverActiveStatus": "STARTING",
        //            "services": [
        //    {
        //        "serviceName": "Open Metadata Repository Services (OMRS)",
        //            "serviceStatus": "STARTING"
        //    }
        //    ]
        //}
        OMAGServerStatusResponse serverStatus = opsAPI.getActiveServerStatus("garygeeke", serverName);

        ServerServicesStatus serverServicesStatus = serverStatus.getServerStatus();

        if (null == serverServicesStatus) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            ServerActiveStatus statusValue = serverServicesStatus.getServerActiveStatus();
            // Just check if server is running for now
            // UNKNOWN     (0,   "Unknown",  "The state of the server is unknown.  This is equivalent to a null value"),
            // STARTING    (1,   "Starting", "The server is starting."),
            // RUNNING     (2,   "Running",  "The server has completed start up and is running."),
            // STOPPING    (3,   "Stopping", "The server has received a request to shutdown."),
            // INACTIVE    (99,  "Inactive", "The server is not running.");
            if (statusValue.equals(ServerActiveStatus.RUNNING)) {
                status = HttpStatus.OK;
            } else
                status = HttpStatus.SERVICE_UNAVAILABLE;
        }

        return new ResponseEntity<>(status.getReasonPhrase(), status);
    }
}
