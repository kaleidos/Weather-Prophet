package net.kaleidos.weather

class Forecast {
    
    int rain
    String prettyDay
    Date lastModified
	
	Forecast(rain, lastModified, prettyDay) {
		this.rain = rain
		this.lastModified = lastModified
        this.prettyDay = prettyDay
	}
	
}
