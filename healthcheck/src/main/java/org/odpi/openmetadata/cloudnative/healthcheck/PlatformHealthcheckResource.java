/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.cloudnative.healthcheck;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * PlatformHealthcheckResource provides a simple healthcheck service for the
 * egeria platform.
 *
 * This differs from the typical Egeria style in that the response is
 * returned as a simple HTTP code.
 *
 * &ge;200 &lt;400 will be interpreted by a Kubernetes Healthcheck as successfull
 */
@RestController
@RequestMapping("/open-metadata/healthcheck/platform")

@Tag(name="Healthcheck Services",
        description="The healthcheck services provide simple APIs to check egeria resources are available for use",
        externalDocs=@ExternalDocumentation(description="Egeria cloud native prototypes",url="https://github.com/odpi/egeria-cloudnative"))

public class PlatformHealthcheckResource
{
    //OMAGServerPlatformOriginServices originAPI = new OMAGServerPlatformOriginServices();


    /**
     * Return the origin of this server platform implementation.
     *
     * Currently always returns 200.
     * In future this may check status of auto-started or configured servers
     *
     * @return String text summary
     */
    @GetMapping(path = "/health")

    @Operation( summary = "Get health of this OMAG Server Platform",
            description="Return HTTP Status code representing health",
            responses = {
                    @ApiResponse(responseCode = "200",description="platform is ready for work",
                            content = @Content(mediaType ="text/plain"))
            })

    public ResponseEntity getPlatformHealth()
    {
        return new ResponseEntity<>("Platform is healthy", HttpStatus.OK);
    }

}
