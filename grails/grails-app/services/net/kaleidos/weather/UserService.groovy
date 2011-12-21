package net.kaleidos.weather

class UserService {

    static transactional = true
    
    def updateAlarms(user, map) {
        user.rainAlarm.notifyEmail = map.notifyEmailRain == 'true' ? true : false
        user.rainAlarm.probability = map.long('probabilityRain')
        user.rainAlarm.save()
    }
}
