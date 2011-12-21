<head>
<title><g:message code='spring.security.ui.forgotPassword.title'/></title>
<meta name='layout' content='main'/>
</head>

<body>



<div id="main">

<g:form action='forgotPassword' name="forgotPasswordForm" autocomplete='off'>

	<g:if test='${emailSent}'>
    <div class="message"><g:message code='spring.security.ui.forgotPassword.sent'/></div>
	</g:if>

	<g:else>

	<h2>Forgot Password?</h2>
    <p><g:message code='spring.security.ui.forgotPassword.description'/></p>

	<table id="tablePassword">
		<tr>
			<td><label for="username"><g:message code='spring.security.ui.forgotPassword.username'/></label></td>
			<td><g:textField name="username" size="20" /></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><g:submitButton name="send" value="Send me my password" class="button" /></td>
		</tr>
	</table>
    
	</g:else>

	</g:form>
</div>


<script>
$(document).ready(function() {
	$('#username').focus();
});
</script>

</body>
