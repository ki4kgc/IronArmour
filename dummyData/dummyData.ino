int count = 0;
int led = A3;
float movement = .1;
float longitude = 4043.5715;
float lattitude = 7400.2783;

void setup(){
  Serial.begin(9600);
  while(!Serial);
  Serial.println("GPS4043.5715N, 7400.2783W");
  delay(20);
  Serial.println("GPS4043.5715N, 7400.2783W");
  delay(20);
  Serial.println("GPS4043.5715N, 7400.2783W");
  delay(20);
  Serial.println("GPS4043.5715N, 7400.2783W");
  delay(20);
  Serial.println("GPS4043.5715N, 7400.2783W");
  delay(20);
  Serial.println("<375");
  delay(20);
  Serial.println("<373");
  delay(20);
  Serial.println("<372");
  delay(20);
  Serial.println("<376");
  delay(20);
  Serial.println("<378");
  delay(20);
  Serial.println("<379");
  delay(20);
  Serial.println("close");
  pinMode(led, OUTPUT);     
}

void loop(){
  digitalWrite(led, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(1000);               // wait for a second
  digitalWrite(led, LOW);    // turn the LED off by making the voltage LOW
  delay(1000);  

}



