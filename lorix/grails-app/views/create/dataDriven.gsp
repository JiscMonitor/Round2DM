Render a data driven inline form
<div class="container">
  <g:form class="form-horizontal" role="form" controller="create" action="process">
    <pre>
      ${layoutDefinition}
      <g:render contextPath="../forms" template="container" model="${[components:layoutDefinition.rootContainer]}" />
    </pre>
  </g:form>
</div>

