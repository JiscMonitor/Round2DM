<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <h1>${org.name} :: ${user.displayName} :: Describe A Research Output</h1>

    <p>Before</p>

    <g:include controller="create" action="inline" params="${[templateType:'dataDriven',view:'researchOutput',cls:'a.b.c']}"/>

    <p>After</p>

  </body>
</html>
