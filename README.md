# kafka-apache
Teste de Integração com Java / Apache Kafka

- Iniciar Zookeeper:
<br><i>.\bin\windows\zookeeper-server-start.bat config/zookeeper.properties</i> 

- Iniciar Kafka:
<br><i>.\bin\windows\kafka-server-start.bat config/server.properties</i>

- Criando Tópico:
<br><i> .\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic NOME_TOPICO</i>

- Listando Tópicos
<br><i> .\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092</i>  

- Alterando Tópico (Partitions):
<br><i> .\bin\windows\kafka-topics.bat --alter --zookeeper localhost:2181 --topic NOME_TOPICO --partitions 3</i>

- Descrevendo as Informaçoes dos Grupos:
<br><i>.\bin\windows\kafka-consumer-groups.bat --group FraudDetectorService  --bootstrap-server localhost:9092 --describe</i>

    |GROUP                |TOPIC           |PARTITION  |CURRENT-OFFSET  |LOG-END-OFFSET  |LAG             |CONSUMER-ID     |HOST            |CLIENT-ID|
    | ------------------- | -------------- | --------- | -------------- | -------------- | -------------- | -------------- | -------------- | ------- |
    |FraudDetectorService |STORE_NEW_ORDER |1          |511             |1029            |518             |-               |-               |-        | 
    |FraudDetectorService |STORE_NEW_ORDER |2          |795             |1095            |300             |-               |-               |-        | 
    |FraudDetectorService |STORE_NEW_ORDER |0          |812             |1153            |341             |-               |-               |-        |
