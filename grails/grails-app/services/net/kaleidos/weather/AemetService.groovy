package net.kaleidos.weather
import groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResult
import org.ccil.cowan.tagsoup.Parser

class AemetService {
    
    @Grapes(@Grab('org.ccil.cowan.tagsoup:tagsoup:1.2'))
    
	/**
	* 
	*/
    def generateWeatherURL(String codeCity) {
        
        def urlWeather = WeatherSource.findByName('aemet')?.urlWeather
        def aemetCode = CityCode.findByAemetcode(codeCity).aemetcode
        aemetCode = 'localidad_' + aemetCode[-5..-1] + '.xml'
        urlWeather = urlWeather + aemetCode
        return urlWeather
    }
    
    def weatherParser(String codeCity, String day) {
		
        def rawText = generateWeatherURL(codeCity).toURL().text		
		def weatherParsed = new XmlSlurper().parseText(rawText)
        def maxRain
        def prettyDay = ''
        
        if(day=='today') {
            maxRain = 0
            for ( i in (2..5) ) {
                try{
                    if (Integer.valueOf(weatherParsed.prediccion.dia[0].prob_precipitacion[i].text()) > maxRain) {                    
                        maxRain = Integer.valueOf(weatherParsed.prediccion.dia[0].prob_precipitacion[i].text())                                            
                    }
                }catch (NumberFormatException e){
                        maxRain=0;
                }
                prettyDay = weatherParsed.prediccion.dia[0].estado_cielo[i].@descripcion.text()
            }
            return [maxRain, prettyDay]
            
        } else if (day=='tomorrow') {
            maxRain = 0
            for ( i in (2..5) ) {
                try{
                    if (Integer.valueOf(weatherParsed.prediccion.dia[1].prob_precipitacion[i].text()) > maxRain) {
                        maxRain = Integer.valueOf(weatherParsed.prediccion.dia[1].prob_precipitacion[i].text())                        
                    }
                }catch (NumberFormatException e){
                        maxRain=0;
                }
                prettyDay = weatherParsed.prediccion.dia[1].estado_cielo[i].@descripcion.text()
            }
            return [maxRain, prettyDay]
        }
	}
	
	
	def generateForecast (String codeCity, String day) {
		def parsedResults = weatherParser(codeCity, day)
        def now = System.currentTimeMillis()
        def forecast = new Forecast(parsedResults[0],now, parsedResults[1])
        return forecast
		
	}
	
	def generateCityURL(String city) {
		
		def urlCity = WeatherSource.findByName('aemet')?.urlCity
		urlCity = urlCity + city
		return urlCity
	}
	
    
    def searchAndCreateCity(String city) {
    
        def urlCity = generateCityURL(city)
        
        def parser = new XmlSlurper(new Parser())
        def parsedHtml
        new URL(urlCity).withReader ('UTF-8') { reader ->
            parsedHtml = parser.parse(reader)
        }
        
        parsedHtml.'**'.find{ it['@class'] == 'resultados_busqueda'}.ul.li.a.each { 
            if (it.text() == city) {
                println 'TODO'
            }
        }

        
    }
    
}
