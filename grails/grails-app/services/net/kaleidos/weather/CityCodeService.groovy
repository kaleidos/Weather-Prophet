package net.kaleidos.weather

class CityCodeService {
    
    def aemetService
    def googleWeatherService
    
    def search(city){
		def aemetCity = aemetService.searchAndCreateCity(city)
        def googleCity = googleWeatherService.searchAndCreateCity(city)
        return 'citycodeservice'
    }
    
}
