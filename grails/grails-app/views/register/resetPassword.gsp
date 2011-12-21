<head>
<title><g:message code='spring.security.ui.resetPassword.title'/></title>
<meta name='layout' content='main'/>
</head>

<body>

<div id="main">

	<g:form action='resetPassword' name='resetPasswordForm' autocomplete='off'>
	<g:hiddenField name='t' value='${token}'/>
	<div class="sign-in">

	<h2><g:message code='spring.security.ui.resetPassword.description'/></h2>

	<table id="tablePassword">
		<s2ui:passwordFieldRow name='password' labelCode='resetPasswordCommand.password.label' bean="${command}"
                             labelCodeDefault='Password' value="${command?.password}"/>

		<s2ui:passwordFieldRow name='password2' labelCode='resetPasswordCommand.password2.label' bean="${command}"
                             labelCodeDefault='Password (again)' value="${command?.password2}"/>
        <tr>
        <td>&nbsp;</td>
        <td><g:submitButton name="send" value="Update my password" class="button" /></td>
        </tr>
	</table>

    

	</div>
	</g:form>

	</div>
</div>

<script>
$(document).ready(function() {
	$('#password').focus();
});
</script>

</body>
