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

- Descrever Tópicos
<br><i> .\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --describe --topic NOME_TPIC</i>

- Alterando Tópico (Partitions):
<br><i> .\bin\windows\kafka-topics.bat --alter --zookeeper localhost:2181 --topic NOME_TOPICO --partitions 3</i>

- Descrevendo as Informaçoes dos Grupos:
<br><i>.\bin\windows\kafka-consumer-groups.bat --group FraudDetectorService  --bootstrap-server localhost:9092 --describe</i>

    |GROUP                |TOPIC           |PARTITION  |CURRENT-OFFSET  |LOG-END-OFFSET  |LAG             |CONSUMER-ID     |HOST            |CLIENT-ID|
    | ------------------- | -------------- | --------- | -------------- | -------------- | -------------- | -------------- | -------------- | ------- |
    |FraudDetectorService |STORE_NEW_ORDER |1          |511             |1029            |518             |-               |-               |-        | 
    |FraudDetectorService |STORE_NEW_ORDER |2          |795             |1095            |300             |-               |-               |-        | 
    |FraudDetectbiorService |STORE_NEW_ORDER |0          |812             |1153            |341             |-               |-               |-        |

- Enviandio um Novo Pedido de Compra:
<br><i>http://localhost:8080/store/new?email=user@mail.com&amount=1659.98</i>

### Desenvolvimento

 - criando um novo serviço que faz IO
 - consideramos o acesso a disco como serviço externo
 - diversas formas de trabalhar batch
 - usando o batch com http fast delegate
 - usando um processo assíncrono e mantendo o isolamento do banco de usuários
 - a importância de um correlation id
 - serialização e deserialização customizada em sua própria camada
 - wrapping de mensagens com tipo próprio 