Based on 

* https://www.baeldung.com/micrometer
* https://stackoverflow.com/questions/73425905/how-to-debug-when-a-jdk-or-cglib-dynamic-proxy-will-be-used-in-java-by-spring-box
* https://www.springcloud.io/post/2022-01/springboot-aop/

Using this approach to debug

> Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'orchestratorBuilder' defined in file [/Users/shanegannon/Development/indexer/server/target/indexer-server/WEB-INF/classes/com/cisco/wx2/indexer/server/data/OrchestratorBuilder.class]: Unsatisfied dependency expressed through constructor parameter 2; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'newDataSourceCreator' defined in class path resource [com/cisco/wx2/indexer/server/config/DataSourceCreatorBeans.class]: Unsatisfied dependency expressed through method 'newDataSourceCreator' parameter 1; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'delegatingIndexingDataSourceBuilder' defined in file [/Users/shanegannon/Development/indexer/server/target/indexer-server/WEB-INF/classes/com/cisco/wx2/indexer/server/kafka/indexing/DelegatingIndexingDataSourceBuilder.class]: Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'indexingKafkaDataSourceBuilder' defined in file [/Users/shanegannon/Development/indexer/server/target/indexer-server/WEB-INF/classes/com/cisco/wx2/indexer/server/kafka/IndexingKafkaDataSourceBuilder.class]: Unsatisfied dependency expressed through constructor parameter 7; nested exception is org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'authIndexing' is expected to be of type 'com.cisco.wx2.indexer.server.orgkey.indexing.AuthIndexing' but was actually of type 'jdk.proxy3.$Proxy230'