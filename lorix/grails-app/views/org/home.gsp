<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <h1>${org.name} :: Home</h1>

    <ul>
      <li>
        <g:link controller="org" action="researcher" params="${[shortcode:params.shortcode]}">Resesarcher</g:link> Activities (Describe Works, Request APC Processing)
        <ul>
          <li><g:link controller="org" action="describe" params="${[shortcode:params.shortcode]}"> Describe A Research Output (Prior to requesting APC)</g:link></li>
          <li><g:link controller="org" action="requestAPC" params="${[shortcode:params.shortcode]}">Request APC</g:link></li>
        </ul>
      </li>
      <li><g:link controller="org" action="apcProcessing" params="${[shortcode:params.shortcode]}">APC Processing</g:link> Activities (Approve / Record APCs)
      </li>
    <ul>
    </ul>

  </body>
</html>
