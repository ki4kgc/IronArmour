IronArmour
==========

#About
IronArmour is a concept for an excersize shirt with components to help track running data. The components used are Adafruit GPS, PulseSensor, Rf24 tranceivers and a Lithium Ion power supply connected through an Arduino Lilypad.

#Min Requirements
- Windows
- 32 bit java installed in Program Files (x86)
- MongoDb
- Arduino board

#Usage
To test the java back-end and website, start mongo (you may have to adjust the startmongo.bat file to point to your instance of mongodb).     

Load the "dummyData" program onto your arduino.

Once mongo is started, run the "getData.bat" file (once again, may have to edit the file to get it to execute.    
This program should look for the correct port to connect to your arduino and echo any Serial prints. 

If the proper data is caught, it will be placed into the mongo database.    

###now to see your data!
To view the data from your Arduino, open the ironarmour yii application in a php environment and viola. It should output the data structure created as well as a map with the dummy path sent by the Arduino.


