# OOKA Übung 6.1

Im Rahmen dieser Übung wurde Service Discovery mithilfe von Netflix Eureka umgesetzt.

Alle Services werden im [Eureka Server](src/main/java/com/ooka/eureka/EurekaServer.java) registriert, so dass sie über 
einen EurekaClient entdeckt werden können. Dieser wird im [AnalysisController](../analysis-control/src/main/java/com/ooka/analysis/AnalysisController.java)
verwendet, um die Services der einzelnen Analyseprozesse anzusteuern. Hierfür wird über den Namen die URL der 
registrierten Instanz generiert.
```
    InstanceInfo serviceThis = eurekaClient.getApplication(nameThis).getInstances().get(0);
    InstanceInfo serviceA = eurekaClient.getApplication(nameA).getInstances().get(0);
    InstanceInfo serviceB = eurekaClient.getApplication(nameB).getInstances().get(0);
    InstanceInfo serviceC = eurekaClient.getApplication(nameC).getInstances().get(0);

    String urlThis = serviceThis.getHomePageUrl() + "/products";
    String urlA = serviceA.getHomePageUrl() + "/powerSystems";
    String urlB = serviceB.getHomePageUrl() + "/liquidGas";
    String urlC = serviceC.getHomePageUrl() + "/management";
```

Für die Zukunft soll das Ansteuern der Services stattdessen direkt über ein REST-Template geschehen.
Auch soll der AnalysisController ebenfalls über Eureka an das Frontend angeschlossen werden.