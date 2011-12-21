package net.kaleidos.weather

class AlarmService {
    
    def weatherService
    def asynchronousMailService
    
    def checkForecast() {
        
        User.findAll().each { user ->
            if (user.wsource) {
                checkForecastUser(user)
            }
        }
    }
    
    def checkForecastUser(User user) {
        def forecast = weatherService.checkWeather(user.citycode, user.wsource, 'today')
        if (needsAlarm(forecast, user)) {
            user.rainAlarmActivedToday = true
        } else {
            user.rainAlarmActivedToday = false
        }
        
        
        
        forecast = weatherService.checkWeather(user.citycode, user.wsource, 'tomorrow')
        if (needsAlarm(forecast, user)) {
            user.rainAlarmActivedTomorrow = true
        } else {
            user.rainAlarmActivedTomorrow = false
        }
        
        println "--->needsAlarm "+user.username+": " +user.rainAlarmActivedTomorrow
        
        user.save()
    }
    
    def needsAlarm(Forecast forecast, User user) {        
        if (user.rainAlarm && (forecast.rain >= user.rainAlarm.probability)) {
            return true
        }   
        return false
        
    }
    
    def checkAlarms() {
    
        //First of all, update forecasts
        checkForecast()
        
        User.findAll().each { user ->
            if (user.wsource) {
                if (user.rainAlarmActivedTomorrow) {
                    sendNotification(user)
                }
            }
        }
    }
    
    
    
    def sendNotification(User user) {
        
        def forecast = weatherService.checkWeather(user.citycode, user.wsource, 'tomorrow')
		def body = """
        <p>Hello ${user.username},</p>
        <p>The probability of rain tomorrow in ${user.city} is ${forecast.rain}%</p>
        """
		asynchronousMailService.sendAsynchronousMail {
			to user.email
			from 'no-reply@weatherprophet.kaleidos.net'
			subject 'Tomorrow will rain and you will get wet'
			html body
		}        
    }
    
}
