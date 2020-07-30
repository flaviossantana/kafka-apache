# kafka-apache
Teste de Integração com Apache Kafka

- Iniciar Zookeeper:
<br><i>.\bin\windows\zookeeper-server-start.bat config/zookeeper.properties</i> 

- Iniciar Kafka:
<br><i>.\bin\windows\kafka-server-start.bat config/server.properties</i>

- Criando Tópico:
<br><i> .\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic NOME_TOPICO</i>  