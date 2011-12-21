<html>
<head>
<title><g:message code='user.preferences.title' /></title>
<meta name='layout' content='main' />
</head>

    <body>
    
        <div id="main">

            <g:if test="${flash.error}">
                <div class="errors">${flash.error}</div>
            </g:if>
        
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
        
            <h2>Settings</h2>
            <g:form class="update-settings" mapping="updateSettings" method="post">
                <p>
                    <label>Email</label>
                    <input type="text" name="email" value="${user.email}">
                </p>
                
                <p>
                    <label>Old Password</label>
                    <input type="password" name="oldPassword">
                </p>
                <p>
                    <label>New Password</label>
                    <input type="password" name="password">
                </p>
                <p>
                    <label>Confirm Password</label>
                    <input type="password" name="password2">
                </p>    
                <g:submitButton name="updateRain" value="Update" class="button"/>
            </g:form>
        </div>

</body>
