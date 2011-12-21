import net.kaleidos.weather.*

class BootStrap {

    def init = { servletContext ->
    
		if (System.getProperty('loadfixtures')) { 
            loadFixtures() 
        }
    }
    
    def destroy = {
    }
    
    def loadFixtures() {
        
        println '************* LOADING FIXTURES '
        
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
        
        def googlesource = WeatherSource.findByName('google')
        if (!googlesource) {
            googlesource = new WeatherSource(name:'google',
                                             urlCity:'http://www.google.com/ig/api?weather=',
                                             urlWeather:'http://www.google.com/ig/api?weather=')
            googlesource.save()
        }
        
        def aemetsource = WeatherSource.findByName('aemet')
        if (!aemetsource) {
            aemetsource = new WeatherSource(name:'aemet',
                                            urlCity:'http://www.aemet.es/es/eltiempo/prediccion/municipios?modo=and&orden=n&tipo=sta&str=',
                                            urlWeather:'http://www.aemet.es/xml/municipios/')
            aemetsource.save()
        }
        
        loadCityCodes()
        
        def citycode = CityCode.findByCity('Madrid')
        
        def user = User.findByUsername('weather')
		if (!user) {
			user = new User(username:'weather', 
                            password:'weather', 
                            email:'teresa.delatorre@kaleidos.net', 
                            enabled:true, 
                            city:'Madrid',
                            citycode:citycode,
                            wsource:googlesource)
			user.save(flush:true)
        	UserRole.create(user, userRole)
		}
        
        user = User.findByUsername('yamila')
		if (!user) {
			user = new User(username:'yamila', 
                            password:'yamila', 
                            email:'yamila.moreno@kaleidos.net', 
                            enabled:true, 
                            city:'Madrid',
                            citycode:citycode,
                            wsource:aemetsource)
			user.save()
        	UserRole.create(user, userRole)
		}

		user = User.findByUsername('admin')
		if (!user) {
			user = new User(username:'admin', 
                             password:'admin', 
                             email:'ivan.lopez@kaleidos.net', 
                             enabled:true, 
                             city:'Madrid',
                             citycode:citycode)
			user.save()
			UserRole.create(user, adminRole)
		}
		
        def userW = User.findByUsername('weather')
		def rainAlarm = Alarm.findByUserAndAlarmType(userW, AlarmType.RAIN)
        if (!rainAlarm) {
            rainAlarm = new Alarm(user:userW,
                                    alarmType: AlarmType.RAIN,
                                    probability: 50,
                                    notifyEmail: true)
            rainAlarm.save()
        }
        userW.rainAlarm = rainAlarm
		userW.save()
        
        println '************* FINISHED LOADING FIXTURES'
        
    }
    
     def loadCityCodes() {
        
        println '************************** LOADING CITYCODES'
        
        def citycodes = [:]
		
        citycodes.acoruna = ['A Coruña', 'A Coruña Spain','coruna-a-id15030']
        citycodes.lugo = ['Lugo', 'Lugo Spain', 'lugo-id27028']
        citycodes.ourense = ['Ourense', 'Ourense Spain', 'ourense-id32054']
        citycodes.pontevedra = ['Pontevedra', 'Pontevedra Spain', 'pontevedra-id36038']
        citycodes.asturias = ['Asturias', 'Oviedo Spain', 'oviedo-id33044']
        citycodes.cantabria = ['Cantabria', 'Santander Spain', 'santander-id39075']
        citycodes.bizkaia = ['Bizkaia', 'Bilbao Spain', 'bilbao-id48020']
        citycodes.gipuzkoa = ['Gipuzkoa', 'Donostia Spain', 'donostia-san-sebastian-id20069']
        citycodes.araba = ['Araba', 'Gazteiz Spain', 'vitoria-gasteiz-id01059']
        citycodes.navarra = ['Navarra', 'Iruña Spain', 'pamplona-iruna-id31201']
        citycodes.huesca = ['Huesca', 'Huesca Spain', 'huesca-id22125']
        citycodes.zaragoza = ['Zaragoza', 'Zaragoza Spain', 'zaragoza-id50297']
        citycodes.teruel = ['Teruel', 'Teruel Spain', 'teruel-id44216']
        citycodes.lleida = ['Lleida', 'Lleida Spain', 'lleida-id25120']
        citycodes.girona = ['Girona', 'Girona Spain', 'girona-id17079']
        citycodes.barcelona = ['Barcelona', 'Barcelona Spain', 'barcelona-id08019']
        citycodes.tarragona = ['Tarragona', 'Tarragona Spain', 'tarragona-id43148']
        citycodes.larioja = ['La Rioja', 'Logroño Spain', 'logrono-id26089']
        citycodes.leon = ['León', 'León Spain', 'leon-id24089']
        citycodes.zamora = ['Zamora', 'Zamora Spain', 'zamora-id49275']
        citycodes.salamanca = ['Salamanca', 'Salamanca Spain', 'salamanca-id37274']
        citycodes.avila = ['Ávila', 'Ávila Spain', 'avila-id05019']
        citycodes.segovia = ['Segovia', 'Segovia Spain', 'segovia-id40194']
        citycodes.soria = ['Soria', 'Soria Spain', 'soria-id42173']
        citycodes.burgos = ['Burgos', 'Burgos Spain', 'burgos-id09059']
        citycodes.palencia = ['Palencia', 'Palencia Spain', 'palencia-id34120']
        citycodes.valladolid = ['Valladolid', 'Valladolid Spain', 'valladolid-id47186']
        citycodes.madrid = ['Madrid', 'Madrid Spain','madrid-id28079']
        citycodes.toledo = ['Toledo', 'Toledo Spain', 'toledo-id45168']
        citycodes.ciudadreal = ['Ciudad Real', 'Ciudad Real Spain', 'ciudad-real-id13034']
        citycodes.albacete = ['Albacete', 'Albacete Spain', 'albacete-id02003']
        citycodes.cuenca = ['Cuenca', 'Cuenca Spain', 'cuenca-id16078']
        citycodes.guadalajara = ['Guadalajara', 'Guadalajara Spain', 'guadalajara-id19130']
        citycodes.castellon = ['Castellón', 'Castellón Spain', 'castellon-de-la-plana-castello-de-la-plana-id12040']
        citycodes.valencia = ['Valencia', 'Valencia Spain', 'valencia-id46250']
        citycodes.alicante = ['Alicante', 'Alicante Spain', 'alacant-alicante-id03014']
        citycodes.murcia = ['Murcia', 'Murcia Spain', 'murcia-id30030']
        citycodes.caceres = ['Cáceres', 'Cáceres Spain', 'caceres-id10037']
        citycodes.badajoz = ['Badajoz', 'Badajoz Spain', 'badajoz-id06015']
        citycodes.huelva = ['Huelva', 'Huelva Spain', 'huelva-id21041']
        citycodes.sevilla = ['Sevilla', 'Sevilla Spain', 'sevilla-id41091']
        citycodes.cadiz = ['Cádiz', 'Cádiz Spain', 'cadiz-id11012']
        citycodes.cordoba = ['Córdoba', 'Córdoba Spain', 'cordoba-id14021']
        citycodes.granada = ['Granada', 'Granada Spain', 'granada-id18087']
        citycodes.jaen = ['Jaén', 'Jaén Spain', 'jaen-id23050']
        citycodes.malaga = ['Málaga', 'Málaga Spain', 'malaga-id29067']
        citycodes.almeria = ['Almería', 'Almería Spain', 'almeria-id04013']
        citycodes.balears = ['Illes Balears', 'Illes Balears Spain', 'palma-id07040']
        citycodes.santacruz = ['Santa Cruz de Tenerife', 'Santa Cruz de Tenerife Spain', 'santa-cruz-de-tenerife-id38038']
        citycodes.laspalmas = ['Las Palmas', 'Las Palmas Spain', 'palmas-de-gran-canaria-las-id35016']
        citycodes.ceuta = ['Ceuta', 'Ceuta Spain', 'ceuta-id51001']
        citycodes.melilla = ['Melilla', 'Melilla Spain', 'melilla-id52001']
        
        citycodes.each {k,v ->
            def citycode = new CityCode(city:v[0], googlecode:v[1], aemetcode:v[2])
            citycode.save(flush:true)
        }
        
    }
    
}
