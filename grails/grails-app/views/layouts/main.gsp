<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    
    <head>
        <meta name="Description" content="weather, forecast, kaleidos" />
        <meta name="Keywords" content="weather, forecast, kaleidos" />
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <meta name="Distribution" content="Global" />
        <meta name="Author" content="Erwin Aligam - ealigam@gmail.com" />
        <meta name="Robots" content="index,follow" />		
        <title><g:layoutTitle default="Weather Prophet" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'weather.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
    </head>
    <body>
        <!-- header starts here -->
        <div id="header">	
	
            <div id="clouds"></div>
		
            <h1 id="logo-text"><a href="/" title="">Weather Prophet</a></h1>	
            <p id="slogan">Your own source of prophecies...</p>				
	
        </div>	
    
        <!-- content-wrap starts here -->
        <div id="content-wrap">
            <div id="content">	 
	
                <div id="sidebar" >	
				<sec:ifLoggedIn>
                    <h3>Hello, <sec:loggedInUserInfo field="username"/></h3>			
                    <ul class="sidemenu">
                        
                        <li>
                            <g:if test="${flash.selected == 'settings'}">
                                <g:link class="selected" controller="user" action="settings">Settings</g:link>
                            </g:if>
                            <g:else>
                                <g:link controller="user" action="settings">Settings</g:link>
                            </g:else>
                        </li>
                        <li>
                            <g:if test="${flash.selected == 'preferences'}">
                                <g:link class="selected" controller="user" action="preferences">Preferences</g:link>
                            </g:if>
                            <g:else>
                                <g:link controller="user" action="preferences">Preferences</g:link>
                            </g:else>
                        </li>
                        <li><g:link controller="logout">Logout</g:link></li>
                        
                    </ul>
                    </sec:ifLoggedIn>
                    
                    
                    <h3>Get Android App!</h3>
                    <p>
                        Install Weather Prophet on your android device, and get a Wake Up alarm, that automagically update itself if there will be rain. You never will be late again!
                    </p>
                    <div id="android" class="center100">
                        <a href="${resource(dir:'android',file:'weatherprophet.apk')}"><img src="${resource(dir:'images',file:'android.png')}" /></a><br/>
                        v0.02
                    </div>
                    
                
                </div>
                
                <g:layoutBody />
            
            </div>
        
        </div>
        
        
        <!-- footer-wrap starts here -->
        <div id="footer-wrap">
            <div id="footer-bottom">		
                <p>
                &copy; 2011 <strong>Weather Prophet</strong> | 
                Created by: <a href="http://kaleidos.net" target="_new"><img src="${resource(dir:'images',file:'kaleidos.png')}" /></a> | 
                Design by: <a href="http://www.styleshout.com/">styleshout</a> | 
                Valid <a href="http://validator.w3.org/check?uri=referer">XHTML</a> | 
                <a href="http://jigsaw.w3.org/css-validator/check/referer">CSS</a>
                </p>		
            </div>	
        </div>
        <jq:resources />
        <jqui:resources />
        <g:javascript library="application" />
    </body>
</html>
