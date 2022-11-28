1. You can add custom simulation class follow 'com.mariana.gatling.demo.Simulations.SampleSimulation'
2. Then just execute the follow command.
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