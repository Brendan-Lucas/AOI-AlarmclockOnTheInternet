import time
from gpiozero import LED
import cjitb; cjitb.enable();
import requests;

AlarmOn = false;

check = requests.get("https://www.git-up.com",params=AlarmOn);


if (check)
    #led2 = LED(4)
    led = LED(18)
    led.on()
    #led2.on()
    time.sleep(5)
    print("Turning Vibrator Off!")

    #if (AlarmOff)
else  
    led.off()
    #led2.off()

