package net.kaleidos.weather

class WeatherService {
    
    static HashMap cache
    //Google API is down
    //def googleWeatherService
    def aemetService
    
    
    def getCache(){
        if (cache==null){
            cache=new HashMap()
        }
        return cache
    }
    
    def checkWeather(CityCode citycode, WeatherSource wsource, String day) {
        
        def diff
                   
        def newKey = wsource.name + citycode.city + day
        def forecast = getCache().get(newKey)
        
        if (forecast){
            def now = System.currentTimeMillis()
            diff = now - forecast.lastModified
        }
        
        if (!forecast || ( diff > 3600000 )) {
            forecast = generateForecast(citycode, wsource, day)
            getCache()[newKey] = forecast
        }
        
        return forecast
    }
	
	def forecastIcon (int forecast) {

		String icon
			
		if (forecast <=20) {
			icon = "0_20.png" 
		} else if (forecast <=40)  {
			icon = "21_40.png"
		} else if (forecast <=60)  {
			icon = "41_60.png"
		} else if (forecast <=80)  {
			icon = "61_80.png"
		} else if (forecast <=100) {
			icon = "81_100.png"
		}
		
		return icon
		
	}
    
    def generateForecast(CityCode citycode, WeatherSource wsource, String day) {
        
        def forecast
        //Google API is down
        /*if (wsource.name == 'google') {
            forecast = googleWeatherService.generateForecast(citycode.googlecode, day)
        } else if (wsource.name == 'aemet') {
            forecast = aemetService.generateForecast(citycode.aemetcode, day)
        }*/
        
        forecast = aemetService.generateForecast(citycode.aemetcode, day)
        
        return forecast
        
    }
}


