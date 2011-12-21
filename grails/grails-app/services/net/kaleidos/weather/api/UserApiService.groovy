package net.kaleidos.weather.api

class UserApiService {
	
	/**
	 * Returns a map with the alarms of the user
	 *
	 * @param user The user to get his alarms
	 */
	def alarms(user) {
		
		def results = []

		if (user.rainAlarm) {
			results << mapAlarm(user.rainAlarm)
		}
		
		return results
	}


	/**
	 * Maps between the domain class and the result Map	
	 * 
	 * @param alarm The Alarm object
	 */
	private mapAlarm(alarm) {
		def result = [:]
		
		result.type = alarm.alarmType.name()
		result.probability = alarm.probability
		result.notifyEmail = alarm.notifyEmail
		
		return result
	}
}
