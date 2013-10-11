package dk.kb.yggdrasil;

import java.util.Map;

/**
 * This class contains the known settings for the rabbitmq broker.
 * 
 * development:
 *   mq_uri: "amqp://localhost:5672"
 *   preservation:
 *       destination: "dev-queue"
 *    dessemination: 
 *       destination: "dev-queue" // Tbd
 */
public final class RabbitMqSettings {

    /** The property for the rabbitmq broker_uri setting in our rabbitmq.yml. */
    public static final String RABBIT_MQ_URI_PROPERTY  = "mq_uri";
    /** The property for the rabbitmq preservation setting in our rabbitmq.yml */
    public static final String RABBIT_MQ_PRESERVATION_PROPERTY  = "preservation";
    /** The property for the rabbitmq dissemination setting in our rabbitmq.yml */
    public static final String RABBIT_MQ_DISSEMINATION_PROPERTY  = "dissemination";
    /** The property for the destination subsetting in our rabbitmq.yml */
    public static final String RABBIT_MQ_DESTINATION_PROPERTY  = "destination";   
    
    
    /** The broker address as a URI. */
    private String brokerUri;
    /** The name of the preservation queue. */
    private String preservationDestination;
    
    /** The name of the dissemination queue. */
    private String disseminationDestination;
    
    /**
     * Constructor. Reads RabbitMQ settings from a LinkedHashMap read from a YAML file.
     * @param settings A LinkedHashMap containing RabbitMQ settings.
     * @throws Exception If some or all of the required RabbitMQ settings are missing.
     */
    public RabbitMqSettings(Map settings) throws Exception {
        if (settings.containsKey(RABBIT_MQ_URI_PROPERTY) 
                && settings.containsKey(RABBIT_MQ_PRESERVATION_PROPERTY)
                && settings.containsKey(RABBIT_MQ_DISSEMINATION_PROPERTY)) {
            brokerUri = (String) settings.get(RABBIT_MQ_URI_PROPERTY);
            Map preservationMap = (Map) settings.get(RABBIT_MQ_PRESERVATION_PROPERTY);
            preservationDestination = (String) preservationMap.get(RABBIT_MQ_DESTINATION_PROPERTY);
            Map disseminationMap = (Map) settings.get(RABBIT_MQ_DISSEMINATION_PROPERTY);
            disseminationDestination = (String) disseminationMap.get(RABBIT_MQ_DESTINATION_PROPERTY);
        } else {
            throw new Exception("Missing some or all of the required properties in the settings file");
        }
    }
   
    /**
     * @return the brokerUri
     */
    public String getBrokerUri() {
        return brokerUri;
    }
       
    /**
     * @return the preservation destination
     */
    public String getPreservationDestination() {
        return preservationDestination;
    }

    /**
     * @return the dissemination destination
     */
    public String getDisseminationDestination() {
        return disseminationDestination;
    }

}
