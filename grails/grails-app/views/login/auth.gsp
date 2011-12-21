<head>
<title><g:message code='spring.security.ui.login.title'/></title>
<meta name='layout' content='main'/>
</head>

<body>
    
<div id="main">		
    <h1><g:message code='spring.security.ui.login.signin'/></h1>
    <form action='${postUrl}' method='POST' id="loginForm" name="loginForm" autocomplete='off'>
        <p>
            <label for="username"><g:message code='spring.security.ui.login.username'/></label>
            <input name="j_username" id="username" size="20" />
            <label for="password"><g:message code='spring.security.ui.login.password'/></label>
            <input type="password" name="j_password" id="password" size="20" /><br/><br/>
            <input type="checkbox" class="checkbox-login" name="${rememberMeParameter}" id="remember_me" checked="checked" />
            <label class="label-checkbox-login" for='remember_me'><g:message code='spring.security.ui.login.rememberme'/></label>
            <span class="label-checkbox-login">
                &nbsp;| <g:link controller='register' action='forgotPassword'><g:message code='spring.security.ui.login.forgotPassword'/></g:link>	
            </span>
            <br><br>            
            <g:link class="button" controller='register' action='index'><g:message code='spring.security.ui.login.register'/></g:link> &nbsp;&nbsp;
            <g:submitButton class="button" name="login" value="Login" />            
        </p>		
    </form>	

    <h2>What is this about?</h2>				
    <p>
        <img src="${resource(dir:'images',file:'partial-cloudy.png')}" alt="" class="float-left" />
        
        What happens when, in the morning, we find out that it's raining? 
        We've got two options: drive the car and make your way to the jam which brings 
        the cities to a standstill, or take the public transportation, much slower... Bottom line, either way you get late!
    </p>
    <p>
        Thanks to Weather Prophet, it will never happen again; you will be sent an email as long as the next day 
        is rainy, so you can make up your mind. Moreover, with the android mobile app, you can configure your 
        clock alarm so that you can wake up on time if it's expected to rain. And you will get on time!
    </p>
    <p>
        Weather Prophet is a free app, so feel free to use it.
    </p>     
    
    <p class="align-right">
        Weather Prophet Team        
    </p>			
</div>

<script>
$(document).ready(function() {
	$('#username').focus();
});

<s2ui:initCheckboxes/>

</script>

</body>
