# modify the classpath according to your directory structure
# "/home/ubuntu/hazelcast-certification_AdolfoFernandez/lib/*" contains hazelcast-xxx.jar, hazelcast-aws-xxx.jar, hazelcast-client-xxx.jar and joda-time-2.7.jar
# "/home/ubuntu/hazelcast-certification_AdolfoFernandez/target/classes/" contains FraudDetection.properties, hazelcast.xml, hazelcast-client.xml and the compiled classes

java -Xms8g -Xmx8g -XX:+UseParallelOldGC -XX:+UseParallelGC -XX:+UseCompressedOops -classpath "/home/ubuntu/hazelcast-certification_AdolfoFernandez/lib/*:/home/ubuntu/hazelcast-certification_AdolfoFernandez/target/classes/" com.hazelcast.certification.cluster.IMDGCertification