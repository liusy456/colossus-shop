package com.colossus.sso.flow;

import org.apereo.cas.web.flow.AbstractCasMultifactorWebflowConfigurer;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

public class CustomAuthenticatorWebflowConfigurer extends AbstractCasMultifactorWebflowConfigurer {
    public static final String MFA_EVENT_ID = "mfa-custom";
    private final FlowDefinitionRegistry flowDefinitionRegistry;

    public CustomAuthenticatorWebflowConfigurer(FlowBuilderServices flowBuilderServices,
                                                FlowDefinitionRegistry loginFlowDefinitionRegistry,
                                                FlowDefinitionRegistry flowDefinitionRegistry) {
        super(flowBuilderServices, loginFlowDefinitionRegistry);
        this.flowDefinitionRegistry = flowDefinitionRegistry;
    }

    @Override
    protected void doInitialize() throws Exception {
        registerMultifactorProviderAuthenticationWebflow(getLoginFlow(),
                MFA_EVENT_ID, this.flowDefinitionRegistry);
    }
}
