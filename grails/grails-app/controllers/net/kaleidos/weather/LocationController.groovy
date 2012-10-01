package net.kaleidos.weather

class LocationController {
    
    static allowedMethods = [
		search:"GET"
    ]
    
    def weatherService
	//def googleWeatherService
    def alarmService
	
    def search = {
        
        def city = weatherService.escapeLikeJavascript(params.city?.trim())
        def citycode = CityCode.findByCity(city)
		
        if (!citycode) {
            return 'error: no debería estar aquí'
/*          YMS: never enters here for now
            def citycode = cityCodeService.search(city)
*/
        }
		
        render citycode
    }


/*
    def test = {
		
		def codeCity = "Cuenca Spain"
		//def citycode = CityCode.findByCity(city)
		//Prueba Dani (Borrar)
		def forecast = googleWeatherService.generateForecast(codeCity, 'today')
		forecast = googleWeatherService.generateForecast(codeCity, 'tomorrow')
		
		render "TEST" 

	}
    
    def aemetService
    def testaemet = {
        
        def codeCity = Utils.escapeLikeJavascript("madrid-id28079")
        def forecast = aemetService.generateForecast(codeCity)
		render forecast.rain
    }
    
    def alarmService
	def testneedalarm = {
        
        def codeCity = "madrid-id28079"
        def forecast = aemetService.generateForecast(codeCity)
        def user = User.get(1L)
        def needAlarm = alarmService.needsAlarm(forecast, user)
        render needAlarm        
    }
    
    def asynchronousMailService
*/
    def testsendemail = {
        
        def user = User.get(1L)
        def sendNotification = alarmService.sendNotification(user)

        render "TEST" 
    }


}
