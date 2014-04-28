setlocal

SET arduino=C:\Program Files (x86)\Arduino\
SET java=C:\Program Files (x86)\Java\jre7\bin\java

SET PATH=%PATH%;"%arduino%"

javac -cp ".;mongo/;helpers/;%arduino%\lib\RXTXcomm.jar;mongo/mongo-java-driver-2.12.0.jar" Serial.java
"%java%" -cp ".;mongo/;helpers/;%arduino%\lib\RXTXcomm.jar;mongo/mongo-java-driver-2.12.0.jar" Serial