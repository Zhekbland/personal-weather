
// Library 2.5.1
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>

// Подключаем библиотеку DHT для работы с DHT11
#include <DHT.h>
// пин подключения контакта DATA
#define DHTPIN 2
// датчик DHT11
#define DHTTYPE DHT11

// создание экземпляра объекта DHT
DHT dht(DHTPIN, DHTTYPE);

// переменные для хранения температуры и влажности
float temp;
float humidity;

#define USE_SERIAL Serial

ESP8266WiFiMulti WiFiMulti;

void setup() {

  USE_SERIAL.begin(115200);
  // USE_SERIAL.setDebugOutput(true);
  USE_SERIAL.println();
  USE_SERIAL.println();
  USE_SERIAL.println();

  for (uint8_t t = 4; t > 0; t--) {
    USE_SERIAL.printf("[SETUP] WAIT %d...\n", t);
    USE_SERIAL.flush();
    delay(1000);
  }

  WiFi.mode(WIFI_STA);
  WiFiMulti.addAP("Arduino", "InnaInna");

  // запуск dht
  dht.begin();
}

void loop() {
  // wait for WiFi connection
  if ((WiFiMulti.run() == WL_CONNECTED)) {
    USE_SERIAL.print("redy listening\n");

    // получить данные температуры
    temp = dht.readTemperature();
    String strTemp = String(temp, 2);
    humidity = dht.readHumidity();
    String strHumidity = String(humidity, 2);
    USE_SERIAL.print("Hum: ");
    USE_SERIAL.print(humidity);
    USE_SERIAL.print(" Temp: ");
    USE_SERIAL.print(temp);
    USE_SERIAL.println(" C");

    HTTPClient http;
    //type=post - send dats to server
    String hostresource = "http://34.28.73.60:8080";
    String request = "";
    // if (USE_SERIAL.available()) {
    // request = USE_SERIAL.readStringUntil('\n');
    // hostresource = hostresource + request + "\n";
    //type=post - get command dats server in arduino
    // if (request.indexOf("GET") >= 0) {
    hostresource = "http://34.28.73.60:8080/sensor?temp=" + strTemp + "&hum=" + strHumidity;
    // }
    //USE_SERIAL.print("Recive request "+hostresource+" \n");
    // send start client begining
    //USE_SERIAL.print("[HTTP] begin...\n");
    // configure traged server and url
    //http.begin("https://192.168.1.12/test.html", "7a 9c f4 db 40 d3 62 5a 6e 21 bc 5c cc 66 c8 3e a1 45 59 38"); //HTTPS
    //http.begin("http://192.168.1.2/index.php?type=wait&sensor=dht11&data=none"); //HTTP

    http.begin(hostresource);  //HTTP

    //USE_SERIAL.print("[HTTP] SEND DATA \n");
    // start connection and send HTTP header
    int httpCode = http.GET();
    // httpCode will be negative on error
    if (httpCode > 0) {
      // HTTP header has been send and Server response header has been handled
      //USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);
      // file found at server
      if (httpCode == HTTP_CODE_OK) {
        String payload = http.getString();
        USE_SERIAL.println(payload);
      }
    } else {
      String payload = http.getString();
      USE_SERIAL.println(payload);
      //USE_SERIAL.printf("\n\n[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }
    http.end();
    // }
  }

  delay(10000);
}