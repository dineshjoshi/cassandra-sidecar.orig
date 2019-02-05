package org.apache.cassandra.sidecar;

public class Configuration
{
    private String cassandraHost;
    private Integer cassandraPort;
    private Integer port;
    private Integer healthCheckFrequencyMillis;

    public Configuration(String cassandraHost, Integer cassandraPort, Integer port,
                         Integer healthCheckFrequencyMillis)
    {
        this.cassandraHost = cassandraHost;
        this.cassandraPort = cassandraPort;
        this.port = port;
        this.healthCheckFrequencyMillis = healthCheckFrequencyMillis;
    }

    public String getCassandraHost()
    {
        return cassandraHost;
    }

    public Integer getCassandraPort()
    {
        return cassandraPort;
    }

    public Integer getPort()
    {
        return port;
    }

    public Integer getHealthCheckFrequencyMillis()
    {
        return healthCheckFrequencyMillis;
    }
}
