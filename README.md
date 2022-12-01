1. You can add custom simulation class follow by [CustomSimulation](src/test/java/com/mariana/gatling/demo/Simulations/CustomSimulation.java)
2. Then just run the below command.
```shell
# logback.level: see resources/logback.xml for more information
# gatling.simulationClass: which simulation class will be processed
# qps: QPS
# duration: how long will the performance test last (seconds)
sudo mvn -e gatling:test \
    -Dlogback.level=DEBUG \
    -Dgatling.simulationClass=com.mariana.gatling.demo.Simulations.SampleSimulation \
    -Dqps=1 \
    -Dduration=1
```