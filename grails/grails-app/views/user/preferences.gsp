<%@ page import="net.kaleidos.weather.CityCode" %>
<%@ page import="net.kaleidos.weather.WeatherSource" %>
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
            
            <h2>Preferences</h2>
        
            <g:form mapping="updateLocation" method="post">
                <p>Choose your location
                    <g:select name="cityId" from="${CityCode.list(sort:'city')}" optionKey="id" optionValue="city" value="${user?.citycode?.id}" noSelection="${['null':'Select One...']}"/>
                </p>
                <p>Choose your weather source
                    <g:select name="wSourceId" from="${WeatherSource.list(sort:'name')}" optionKey="id" optionValue="name" value="${user?.wsource?.id }" noSelection="${['null':'Select One...']}"/>
                </p>
                <g:submitButton name="submit" value="Save" class="button" />
            </g:form>

            <h2>Rain alarm</h2>
            
            <g:form mapping="updateAlarm" method="post">
                <p>Probability</p>
                <div id="sliderRain"></div>
                <span id="amountRain" name="probability">${user.rainAlarm.probability}</span>
                <p>
                    <input type="checkbox" id="emailRain" name="notifyEmail" <g:if test="${user.rainAlarm.notifyEmail}">checked="checked"</g:if>> Notify by email
                </p>
                
                <g:hiddenField name="alarmId" value="${user.rainAlarm.id}" />
                <g:hiddenField name="probability" value="${user.rainAlarm.probability}" />
                <g:submitButton name="updateRain" value="Update" class="button" />
            </g:form>
        
            <g:if test="${forecastIcons.size() > 0 && forecastList.size() > 0}">
                <h2>Your Weather</h2>
                    
                    <table>
                        <tbody>
                        <tr class="head">
                                <th><strong>Today</strong></th>
                                <th><strong>Tomorrow</strong></th>
                            </tr>
                            <tr>
                                <td><img src="${resource(dir:'images/forecasts',file:forecastIcons[0])}"/></td>
                                <td><img src="${resource(dir:'images/forecasts',file:forecastIcons[1])}"/></td>
                                
                            </tr>
                            <tr>
                                <td>${forecastList[0].prettyDay} ${forecastList[0].rain}%</td>
                                <td>${forecastList[1].prettyDay} ${forecastList[1].rain}%</td>
                            </tr>
                        </tbody>
                    </table>
            </div>
            </g:if>



</body>
</html>
