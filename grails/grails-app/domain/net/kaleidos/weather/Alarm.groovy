package net.kaleidos.weather

class Alarm {

	User user
	AlarmType alarmType
	Integer probability = 100
	Boolean notifyEmail = false
    

	static mapping = {
		version false
		
		probability range:0..100
	}
}


enum AlarmType {
	RAIN,
	SNOW
}
