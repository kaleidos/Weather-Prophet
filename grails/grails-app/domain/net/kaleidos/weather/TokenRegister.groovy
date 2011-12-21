package net.kaleidos.weather

class TokenRegister {

	String username
	String token = UUID.randomUUID().toString().replaceAll('-', '')
	Date dateCreated

	static mapping = {
		version false
	}
}
