package com.redhat.che.workspace.services.telemetry.woopra;

import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import org.eclipse.che.incubator.workspace.telemetry.base.AbstractAnalyticsManager;
import org.eclipse.che.incubator.workspace.telemetry.base.BaseConfiguration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class MainConfiguration extends BaseConfiguration {
    @ConfigProperty(name = "che.fabric8.analytics.segment_write_key")
    Optional<String> segmentWriteKey;

    @ConfigProperty(name = "che.fabric8.analytics.woopra_domain")
    Optional<String> woopraDomain;

    @ConfigProperty(name = "woopra.domain.endpoint")
    Optional<String> woopraDomainEndpoint;

    @ConfigProperty(name = "segment.write.key.endpoint")
    Optional<String> segmentWriteKeyEndpoint;


    @Produces
    public AbstractAnalyticsManager analyticsManager() {
      return new AnalyticsManager(segmentWriteKey.orElse(null), woopraDomain.orElse(null), woopraDomainEndpoint.orElse(null), segmentWriteKeyEndpoint.orElse(null), apiEndpoint, workspaceId, machineToken, requestFactory(), new AnalyticsProvider(), new HttpUrlConnectionProvider());
    }
}
