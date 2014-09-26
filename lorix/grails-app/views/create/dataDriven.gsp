<div class="container-fluid">
  <g:form class="form-horizontal" role="form" controller="create" action="process">
    <input type="hidden" name="_clazz" value="${ctxClazz}"/>
    <g:render contextPath="../forms" 
              template="container" 
              model="${[components:layoutDefinition.rootContainer, ctxObject:ctxObject, parentPath:'']}" />
    <button class="btn btn-success pull-right" type="submit">Create</button>
  </g:form>
</div>

