package net.kaleidos.weather

class CityCode {

    String city
    String googlecode
    String aemetcode

    static constraints = {
		city blank: false, unique: true
        googlecode nullable: true, blank: true
        aemetcode nullable: true, blank: true
	}
    
    static mapping = {
        version false
    }
    

}
