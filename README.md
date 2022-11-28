```shell
sudo mvn -e gatling:test \
    -Dlogback.level=DEBUG \
    -Dgatling.simulationClass=com.mariana.gatling.demo.Simulations.FeedsSimulation \
    -Dusers=1 \
    -Dduration=1
```