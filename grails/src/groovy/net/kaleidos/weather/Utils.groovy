package net.kaleidos.weather

class Utils {

	public static String escapeLikeJavascript(String city) {
		def specialCharsRegex = /[^\w@*-+.\/]/
		return city.replaceAll(specialCharsRegex, {
			"%${Integer.toHexString(it.codePointAt(0)).toUpperCase().padLeft(2, '0')}"
		})
	}

}