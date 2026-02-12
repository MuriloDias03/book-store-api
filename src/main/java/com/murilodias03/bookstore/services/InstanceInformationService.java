package com.murilodias03.bookstore.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


// Serviço apenas para estudar Kubernetes
@Service
public class InstanceInformationService {

    private static final String HOST_NAME = "HOSTNAME";

    private static final String DEFAULT_ENV_INSTANCE_GUID = "LOCAL";

    @Value("${" + HOST_NAME + ":" + DEFAULT_ENV_INSTANCE_GUID + "}")
    private String hostName;

    public String retrieveInstanceInfo() {
        return hostName.substring(hostName.length()-5);
    }

}