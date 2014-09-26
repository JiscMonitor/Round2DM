<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <h1>${org.name} :: ${user.displayName} :: Describe A Research Output</h1>
    <g:include controller="create" 
               action="inline" 
               params="${[templateType:'dataDriven',
                          view:'g:Research_Output',
                          cls:'uk.ac.jisc.lorix.ResearchOutput',
                          onSuccess:createLink(controller:'org',action:'home',params:[shortcode:params.shortcode],absolute:true)
                          ]}"/>
  </body>
</html>
