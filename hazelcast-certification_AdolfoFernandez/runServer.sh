# modify the classpath according to your directory structure
# "../nfs/app/certification/lib/*" contains hazelcast-xxx.jar, hazelcast-client-xxx.jar, joda-time-2.7.jar and the certification project jar - build with your own implementation
# ../nfs/app/certification/resources contains FraudDetection.properties and hazelcast.xml
java -Xms16g -Xmx16g -XX:+UseParallelOldGC -XX:+UseParallelGC -XX:+UseCompressedOops -classpath "/home/ubuntu/Certificacion/lib/*:/home/ubuntu/Certificacion/target/classes/" com.hazelcast.certification.server.FraudDetectionServer