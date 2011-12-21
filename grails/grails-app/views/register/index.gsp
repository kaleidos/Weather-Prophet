<head>
	<meta name='layout' content='main'/>
	<title><g:message code='spring.security.ui.register.title'/></title>
</head>

<body>

<div id="main">		
    <h1><g:message code='spring.security.ui.register.title'/></h1>
    
    <g:if test="${flash.error}">
        <div class="errors">${flash.error}</div>
    </g:if>

    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    
            
    <g:form mapping="userRegister" method='POST' autocomplete='off'>
        <g:if test='${emailSent}'>
        <br/>
        <g:message code='spring.security.ui.register.sent'/>
        </g:if>
        <g:else>    
        <br/>
        <p>
            <s2ui:textFieldRow name='username' labelCode='user.username.label' bean="${command}"
                         size='40' labelCodeDefault='Username' value="${command.username}"/>

            <s2ui:textFieldRow name='email' bean="${command}" value="${command.email}"
                               size='40' labelCode='user.email.label' labelCodeDefault='E-mail'/>
    
            <s2ui:passwordFieldRow name='password' labelCode='user.password.label' bean="${command}"
                                 size='40' labelCodeDefault='Password' value="${command.password}"/>
    
            <s2ui:passwordFieldRow name='password2' labelCode='user.password2.label' bean="${command}"
                                 size='40' labelCodeDefault='Password (again)' value="${command.password2}"/>
            <br><br>
                     
            <g:submitButton class="button" name="register" value="Save" />            
        </p>	
        </g:else>	
    </g:form>	
</div>

<script>
$(document).ready(function() {
	$('#username').focus();
});
</script>

</body>
