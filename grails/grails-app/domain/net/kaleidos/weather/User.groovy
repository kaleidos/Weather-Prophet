package net.kaleidos.weather

class User {

	transient springSecurityService

	String username
	String password
	String email
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	String authToken = UUID.randomUUID().toString().replaceAll('-', '')
	
	Integer hour
	Integer minute
	
    Alarm rainAlarm
    boolean rainAlarmActivedToday
	boolean rainAlarmActivedTomorrow
	
	String city
	CityCode citycode
	WeatherSource wsource
	

	static constraints = {
		username blank: false, unique: true
		password blank: false, password:true
		email nullable:false
		citycode nullable:true
		city nullable:true
        wsource nullable:true
		
		hour nullable:true
		minute nullable:true
		rainAlarm nullable:true
		rainAlarmActivedToday default:false
        rainAlarmActivedTomorrow default:false
        
		
	}

	static mapping = {
		table 'users'
        password column: '`password`'
        version false
    }

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
