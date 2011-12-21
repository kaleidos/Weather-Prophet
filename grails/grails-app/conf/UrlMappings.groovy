class UrlMappings {

	static mappings = {
        
        // Home
        "/" (controller:'login', action:'auth')
        
		// Sign Up
        name userRegister: "/register/register" {controller = 'register'; action = 'register'}
        name userPreRegister: "/signup/preregister" {controller = 'signup'; action = 'preRegister'}
        
        // Password 
        name forgotPassword: "/signup/password/forgot" {controller = 'signup'; action = 'forgotPassword'}
		name resetPassword: "/signup/password/reset" {controller = 'signup'; action = 'resetPassword'}
        
        // User Preferences
        name userPreferences: "/user/preferences" {controller = 'user'; action = 'preferences'}
        name searchCity: "/city/search"{controller = 'location'; action = 'search'}
        name updateAlarm: "/user/alarm/update" {controller='user'; action='updateAlarm'}
        name updateLocation: "/user/location/update" {controller='user'; action='updateLocation'}
        
        // User Settings
        name updateSettings: "/user/settings/update" {controller='user'; action='updateSettings'}
		
		//Tests
		name test: "/test" {controller='location'; action='test'}
		name test: "/testaemet" {controller='location'; action='testaemet'}
        name test: "/testneedalarm" {controller='location'; action='testneedalarm'}
        name test: "/testsendemail" {controller='location'; action='testsendemail'}
		
		/***************************** API ********************************/
		name apiUserAlerts: "/api/user/alarms" {controller='user'; action='apiAlarms'}
		name apiUserAlertsActivated: "/api/user/alarms/activated" {controller='user'; action='apiAlarmsActivated'}
        name apiUserUpdateAlarms: "/api/user/alarms/update" {controller='user'; action='apiUserUpdateAlarms'}
		
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"500"(view:'/error')
		"404"(view:'/404')
	}
}
