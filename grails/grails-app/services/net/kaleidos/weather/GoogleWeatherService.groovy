package net.kaleidos.weather
import groovy.util.XmlSlurper
import org.ccil.cowan.tagsoup.Parser

class GoogleWeatherService {

    @Grapes(@Grab('org.ccil.cowan.tagsoup:tagsoup:1.2'))
	
	/**
	* 
	*/
    def generateWeatherURL(String codeCity) {
		
        def urlWeather = WeatherSource.findByName('google')?.urlWeather
        urlWeather = urlWeather + CityCode.findByGooglecode(codeCity)?.googlecode
        return urlWeather.replace(" ","%20")
    }
    
	def weatherParser(String codeCity, String day) {
		
		def rawText = generateWeatherURL(codeCity).toURL().text		
		def weatherParsed = new XmlSlurper().parseText(rawText)
		
		
		if (day=='today') {
			return weatherParsed.weather.forecast_conditions[0].condition.@data.text()			
		} else if (day=='tomorrow'){ 
			return weatherParsed.weather.forecast_conditions[1].condition.@data.text()		
		}	


	}
	
	
	def generateForecast (String codeCity, String day) {
		
		
		def textForecast = weatherParser(codeCity, day)
		int rain
			
		switch (textForecast) {
			case "Cloudy": rain = 20; break;
			case "Chance of Rain": rain = 40; break;
			case "Chance of Storm": rain = 40; break;
			case "Chance of Tstorm": rain = 40; break;
			case "Chance of Snow": rain = 40; break;
			case "Rain": rain = 60; break;
			case "Sleet": rain = 60; break;
			case "Snow": rain = 60; break;
			case "Storm": rain = 80; break;
			case "Thunderstorm": rain = 100; break;
			default: rain = 0; break;
		}
			
		def now = new Date()
		
		def forecast = new Forecast(rain, now, textForecast)
		return forecast
		
	}

	
	def generateCityURL(String city) {
		
		def urlCity = WeatherSource.findByName('google')?.urlCity
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
		
		parsedHtml.'**'.find{ it['@class'] == 'resultados_busqueda'}?.ul?.li?.a?.each {
			def it_text = Utils.escapeLikeJavascript(it.text())
			if (it.text() == city) {
				println '===>'
				println it.attributes().get("href")
			}
		}
    }       
}
