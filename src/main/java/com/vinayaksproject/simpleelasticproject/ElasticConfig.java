/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vinayaksproject.simpleelasticproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * The configuration class that contains elastic related properties
 *
 * @author vinayak
 */
@Component
@ConfigurationProperties("elastic")
public class ElasticConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticConfig.class);
    private String EsHost;

    private String EsProtocol;
    private int EsPort;

    private String EsIndexName;
    private String EsDoctypeName;

    private String EsClusterName;

    private String clusterNodes;
    private String esShardsNo;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {

        return buildClient();
    }

    @Bean
    public ObjectMapper defaultObjectMapper() {

        return new ObjectMapper();
    }

    private RestHighLevelClient buildClient() {
        if (LOG.isInfoEnabled()) {
            LOG.info("Creating elasticsearch client ");
        }
        if (LOG.isDebugEnabled()) {
            StringBuilder log = new StringBuilder();
            log.append("Host:").append(getEsHost());
            log.append("Port:").append(getEsPort());
            log.append("Protocol:").append(getEsProtocol());
            LOG.debug("Creating elasticsearch client ");
        }
        RestHighLevelClient restHighLevelClient = null;
        try {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(getEsHost(), getEsPort(), getEsProtocol())));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return restHighLevelClient;
    }

    /**
     * @return the EsHost
     */
    public String getEsHost() {
        return EsHost;
    }

    /**
     * @param EsHost the EsHost to set
     */
    public void setEsHost(String EsHost) {
        this.EsHost = EsHost;
    }

    /**
     * @return the EsProtocol
     */
    public String getEsProtocol() {
        return EsProtocol;
    }

    /**
     * @param EsProtocol the EsProtocol to set
     */
    public void setEsProtocol(String EsProtocol) {
        this.EsProtocol = EsProtocol;
    }

    /**
     * @return the EsPort
     */
    public int getEsPort() {
        return EsPort;
    }

    /**
     * @param EsPort the EsPort to set
     */
    public void setEsPort(int EsPort) {
        this.EsPort = EsPort;
    }

    /**
     * @return the EsClusterName
     */
    public String getEsClusterName() {
        return EsClusterName;
    }

    /**
     * @param EsClusterName the EsClusterName to set
     */
    public void setEsClusterName(String EsClusterName) {
        this.EsClusterName = EsClusterName;
    }

    /**
     * @return the clusterNodes
     */
    public String getClusterNodes() {
        return clusterNodes;
    }

    /**
     * @param clusterNodes the clusterNodes to set
     */
    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    /**
     * @return the EsIndexName
     */
    public String getEsIndexName() {
        return EsIndexName;
    }

    /**
     * @param EsIndexName the EsIndexName to set
     */
    public void setEsIndexName(String EsIndexName) {
        this.EsIndexName = EsIndexName;
    }

    /**
     * @return the EsDoctypeName
     */
    public String getEsDoctypeName() {
        return EsDoctypeName;
    }

    /**
     * @param EsDoctypeName the EsDoctypeName to set
     */
    public void setEsDoctypeName(String EsDoctypeName) {
        this.EsDoctypeName = EsDoctypeName;
    }

    /**
     * @return the esShardsNo
     */
    public String getEsShardsNo() {
        return esShardsNo;
    }

    /**
     * @param esShardsNo the esShardsNo to set
     */
    public void setEsShardsNo(String esShardsNo) {
        this.esShardsNo = esShardsNo;
    }

}
