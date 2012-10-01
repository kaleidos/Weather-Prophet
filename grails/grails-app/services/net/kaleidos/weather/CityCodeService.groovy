package net.kaleidos.weather

class CityCodeService {
    
    def aemetService
    //def googleWeatherService
    
    def search(city){
		def aemetCity = aemetService.searchAndCreateCity(city)
        //Google API is down
        //def googleCity = googleWeatherService.searchAndCreateCity(city)
        return 'citycodeservice'
    }
    
}
