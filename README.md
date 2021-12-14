# TemporalSagaWithRESTDemo
temporal.io support for Saga along with REST demo.

Pre-requisite
1. Temporal server should be running. Create "poc" namespace in temporal.
2. mysql server should be running.
3. Cassandra should be running.

To start mysql and Cassandra in docker, follow below steps.

$cd docker-compose

$docker compose up 

If you are on MAC then use
$docker-compose up

After starting docker container for mysql, 

$docker exec -it <MYSQL container ID> /bin/bash
#mysql -u root -p
Enter Password: root

mysql>


Now create poc_db database and transaction history table as follows.

mysql> create database poc_db;

mysql> create table poc_db.transactionhistory(historyid bigint NOT NULL AUTO_INCREMENT, customerid bigint NOT NULL, name varchar(50) not null, balance not null double, update_timestamp timestamp not null, activity varchar(100) not null, primary key(historyid));

mysql> desc poc_db.transactionhistory;
+------------------+--------------+------+-----+---------+----------------+
| Field            | Type         | Null | Key | Default | Extra          |
+------------------+--------------+------+-----+---------+----------------+
| historyid        | bigint       | NO   | PRI | NULL    | auto_increment |
| customerid       | bigint       | NO   |     | NULL    |                |
| name             | varchar(50)  | NO   |     | NULL    |                |
| balance          | double       | NO   |     | NULL    |                |
| update_timestamp | timestamp    | NO   |     | NULL    |                |
| activity         | varchar(100) | NO   |     | NULL    |                |
+------------------+--------------+------+-----+---------+----------------+


Now create a table in Cassandra.

$docker exec -it <Cassandra container ID> /bin/bash
#cqlsh

cqlsh> create keyspace if not exists account with replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
cqlsh> create table if not exists account.customer(customerid bigint PRIMARY KEY, customer_name text, balance double, update_date timestamp);

Now execute TemporalSagaWithRESTDemo.jar and test using curl or postman.

1. Start transaction

On Windows
$curl -X POST -H "Content-type:application/json" -d "{\"senderAcctNum\": 1, \"receiverAcctNum\": 3, \"amount\": 300}" http://localhost:9070/startTransaction

On Linux OR MAC
$curl -X POST -H "content-type:application/json" -d '{"senderAcctNum": 1, "receiverAcctNum": 3, "amount": 300}' http://localhost:9070/startTransaction

2. Start transfer

On Windows
$curl -X POST -H "Content-type:application/json" -d "{\"senderAcctNum\": 1, \"receiverAcctNum\": 3, \"amount\": 300}" http://localhost:9070/transfer

On Linux OR MAC
$curl -X POST -H "content-type:application/json" -d '{"senderAcctNum": 1, "receiverAcctNum": 3, "amount": 300}' http://localhost:9070/transfer

3. End transaction

On Windows
$curl -X POST -H "Content-type:application/json" -d "{\"senderAcctNum\": 1, \"receiverAcctNum\": 3, \"amount\": 300}" http://localhost:9070/completeTransfer

On Linux OR MAC
$curl -X POST -H "content-type:application/json" -d '{"senderAcctNum": 1, "receiverAcctNum": 3, "amount": 300}' http://localhost:9070/completeTransfer
