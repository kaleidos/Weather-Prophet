package net.kaleidos.weather

import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class UserController {
	
	def userApiService
    def springSecurityService
    def userService
    def weatherService
    def alarmService
	
	static allowedMethods = [
		apiAlarms:"GET", updateAlarm:"POST", apiUserUpdateAlarms:"POST"
	]

    def preferences = {
        flash.selected = 'preferences'
        def user = springSecurityService.currentUser
        
        def forecastIcons = []
        def forecastList = []
        if (user.wsource && user.citycode) {
            def todayForecast = weatherService.generateForecast(user.citycode, user.wsource, "today")
            def tomorrowForecast = weatherService.generateForecast(user.citycode, user.wsource, "tomorrow")


            //The list index represents a day: today [0], tomorrow [1], ...
            //ForecastIcons contains the name of the icon file that represents the user rain probability, for days: today [0], tomorrow [1]... 
            //ForecastList are different days forecasts for a user 
            
            forecastIcons[0] = weatherService.forecastIcon(todayForecast.rain)
            forecastList[0] = todayForecast
            forecastIcons[1] = weatherService.forecastIcon(tomorrowForecast.rain)
            forecastList[1] = tomorrowForecast	
        }
        
        if (!user.rainAlarm) {
            def alarm = new Alarm(alarmType: AlarmType.RAIN, user:user)
            alarm.save()
            user.rainAlarm = alarm
            user.save()
        }

        return render(view:'preferences', model:[user:user, forecastIcons:forecastIcons, forecastList:forecastList])
    }
    
    def settings = {
        flash.selected = 'settings'
        def user = springSecurityService.currentUser
        
        return render(view:'settings', model:[user:user])
    }
    
    /**
     * Updates an alarm
     * 
     * @param alarmId The id of the alarm to update
     * @param probability The new probability
     * @param notifyEmail True to activate notification by email
     */ 
    def updateAlarm = {
        flash.selected = 'preferences'
        
        def user = springSecurityService.currentUser
        def alarmId = params.long('alarmId')
        
        def alarm = Alarm.get(alarmId)
        if (!alarm || alarm.user.id != user.id)  {
            flash.error = 'The alarm does not exist or does not belongs to the user'
            redirect(controller:'user', action:'preferences')
            return
        }

        bindData(alarm, params, [include:['probability']])
        alarm.notifyEmail = params.notifyEmail ? true : false
        alarm.probability = Integer.parseInt(params.probability)
        
        if (!alarm.hasErrors() && alarm.save(flush: true)) {
            flash.message = 'Alarm updated correctly'
            
            weatherService.checkWeather(user.citycode, user.wsource, 'today')
            weatherService.checkWeather(user.citycode, user.wsource, 'tomorrow')
            
            alarmService.checkForecastUser(user)
            
            return redirect(controller:'user', action:'preferences')
        } else {
            flash.error = 'There was an error updating the alarm'
            return redirect(controller:'user', action:'preferences')
        }
    }
    
    def updateLocation = {
        flash.selected = 'preferences'
        
        def cityId = params.long('cityId')
        def city = CityCode.get(cityId)
        if (!city) {
            flash.error = 'The city does not exist'
            return redirect(controller:'user', action:'preferences')
        }
        def wSourceId = params.long('wSourceId')
        def wsource = WeatherSource.get(wSourceId)
        if (!wsource) {
            flash.error = 'The source does not exist'
            return redirect(controller:'user', action:'preferences')
        }
        
        def user = springSecurityService.currentUser
        user.citycode = city
        user.city = city.city
        user.wsource = wsource
        user.save()
        
        weatherService.checkWeather(user.citycode, user.wsource, 'today')
        weatherService.checkWeather(user.citycode, user.wsource, 'tomorrow')
        
        alarmService.checkForecastUser(user)
    
        flash.message = 'Settings updated correctly'
        return redirect(controller:'user', action:'preferences')
    }
    
    def updateSettings = {
        flash.selected = 'settings'
        
        def user = springSecurityService.currentUser
        
        def email = params.email.trim()
        if (email) {
            user.email = email
        }
        
        def oldPassword = params.oldPassword
        def password = params.password
        def password2 = params.password2
        if (oldPassword && password && password2) {
            if (password != password2) {
                flash.error = 'New password and confirmation password are different'
                return redirect(controller:'user', action:'settings')
            }
            
            if (oldPassword == password) {
                flash.error = 'Old password and new password must be different'
                return redirect(controller:'user', action:'settings')
            }

            if (user.password != springSecurityService.encodePassword(oldPassword)) {
                flash.error = 'Old password is not valid'
                return redirect(controller:'user', action:'settings')
            }
            
            user.password = password
            user.save()
        }
        
        flash.message = 'Settings updated correctly'
        return redirect(controller:'user', action:'settings')
    }
	
	/********************************************/
	/******************* API ********************/
	/********************************************/
	
	/**
	 * Get the alarms of the user
	 * 
	 * @param authToken The auth token of the user
	 */
	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def apiAlarms = {
		
		def authToken = params.authToken ? params.authToken : ""
		
		def user = User.findByAuthToken(authToken)
		if (!user) {
			return render(text:[success:false, msg:'The user does not exist'] as JSON, contentType:'text/json')
		}
		
		return render(text:[success:true, alarms:userApiService.alarms(user)] as JSON, contentType:'text/json')
	}
    
	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def apiAlarmsActivated = {
		
		def authToken = params.authToken ? params.authToken : ""
		def day = params.day ? params.day : "tomorrow" 
        
		def user = User.findByAuthToken(authToken)
		if (!user) {
			return render(text:[success:false, msg:'The user does not exist'] as JSON, contentType:'text/json')
		}
        
        def forecast = weatherService.generateForecast(user.citycode, user.wsource, day)
        
		if(day=='today'){
            return render(text:[success:true, rainAlarmActivated:user.rainAlarmActivedToday, probRainToday:forecast.rain, prettyDay:forecast.prettyDay] as JSON, contentType:'text/json')
        } else {
            return render(text:[success:true, rainAlarmActivated:user.rainAlarmActivedTomorrow, probRainToday:forecast.rain, prettyDay:forecast.prettyDay] as JSON, contentType:'text/json')
        }
	}
    
    /**
     * Update the alarms for a user
     * 
     * @param probabilityRain The new probability of rain
     * @param notifyEmailRain True to notify by email in case of rain
     */
    @Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
    def apiUserUpdateAlarms = {
        
		def authToken = params.authToken ? params.authToken : ""
		
		def user = User.findByAuthToken(authToken)
		if (!user) {
			return render(text:[success:false, msg:'The user does not exist'] as JSON, contentType:'text/json')
		}
        
        
        
        
        weatherService.checkWeather(user.citycode, user.wsource, 'today')
        weatherService.checkWeather(user.citycode, user.wsource, 'tomorrow')
        
        alarmService.checkForecastUser(user)
        
        // Update the alarms
        userService.updateAlarms(user, params)
        

        return render(text:[success:true] as JSON, contentType:'text/json')
    }
}
