package net.kaleidos.weather

class SignupController {
	
    static allowedMethods = [
		forgotPassword:["GET","POST"], register:["GET","POST"]
    ]
	
	
	def asynchronousMailService

	/**
	 * Send an email to the user with a reset link
	 * 
	 * @param username The username to reset the password
	 */
	def forgotPassword = {
		
		if (!request.post) {
			// show the form
			return render(view:'forgotPassword')
		}
		
		def user = User.findByUsername(params.username)
		if (!user) {
			flash.message = message(code:'signup.forgotPassword.error.userNotFound')
			return render(view:'forgotPassword')
		}
		
		def token = new TokenRegister(username:user.username).save(flush:true)
		
		String url = createLink(absolute:true, mapping:'resetPassword', params:[token:token.token])
		def body = g.render(template:'/emails/forgotPassword', model:[user:user, url:url])
		
		asynchronousMailService.sendAsynchronousMail {
			to user.email
			from 'weather@no-response.com'
			subject 'Password reset'
			html body.toString()
		}
		
		// WTF!!!
		flash.message = ""
		return render(view:'forgotPassword', model:[emailSent:true])
	}
	
	def preRegister = {
		if (!request.post) {
			return render(view:'preRegister')
		}
		
		if (!params.email) {
			flash.message = 'You must enter an email'
			return render(view:'preRegister')
		}
		
		def tokReg = new TokenRegister(username:params.email).save(flush:true)
		
		String url = createLink(absolute:true, mapping:'userRegister', params:[token:tokReg.token])
		def body = g.render(template:'/emails/verifyRegister', model:[url:url])
		
		asynchronousMailService.sendAsynchronousMail {
			to params.email
			from 'weather@no-response.com'
			subject 'New account'
			html body.toString()
		}
		
		flash.message = ""
		return render(view:'preRegister', model:[emailSent:true])
		
	}
	
	def register = {
		if (!request.post) {
			def tokReg = TokenRegister.findByToken(params.token)
			if (!tokReg) {
				return render(view:'/404', model:[msgError:'The token is not valid'])
			}
			return render(view:'register', model:[email:tokReg.username])
		}
		
		
        def userInstance = new User(params)
        if (!userInstance.save(flush: true)) {
            render(view: "register", model: [userInstance:userInstance])
            return
        }

        render(view:"register", model:[userCreated:true])
	}
	
	def resetPassword = {
		
		def tokenRegister = TokenRegister.findByToken(params.token)
		if (request.post) {			
			if ((!params.password || !params.password2) || (params.password != params.password2)) {
				flash.message = message(code:'signup.resetPassword.error.passwordDiferent')
				return render(view:'resetPassword', model:[tokenRegister:tokenRegister])
			}
            
			def user = User.findByUsername(tokenRegister.username)
			user.password = params.password
			user.save(flush:true)
            tokenRegister.delete()
			return render(view:'resetPassword', model:[passwordSave:true])
		}		
		
		if (tokenRegister) {
			return render(view:'resetPassword', model:[tokenRegister:tokenRegister])
		}
		def msgError = message(code:'signup.resetPassword.error.tokenNotFound')
		return render(view:'/404', model:[msgError: msgError])
		
	}
	

}
