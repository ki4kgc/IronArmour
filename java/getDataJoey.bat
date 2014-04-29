setlocal

SET arduino=C:\Program Files (x86)\Arduino\
SET java=C:\Program Files (x86)\Java\jdk1.8.0_05\bin

SET PATH=%PATH%;"%arduino%"

javac -cp ".;mongo/;helpers/;%arduino%\lib\RXTXcomm.jar;mongo/mongo-java-driver-2.12.0.jar" Serial.java
"%java%"\java -cp ".;mongo/;helpers/;%arduino%lib\RXTXcomm.jar;mongo/mongo-java-driver-2.12.0.jar" Serial

pause