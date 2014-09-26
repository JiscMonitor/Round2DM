<div class="container-fluid">
  <g:form class="form-horizontal" role="form" controller="create" action="process">

    <input type="hidden" name="__clazz" value="${ctxClazz.name}"/>
    <input type="hidden" name="__view" value="${params.view}"/>
    <input type="hidden" name="__onSuccess" value="${onSuccess}"/>

    <g:render contextPath="../forms" 
              template="container" 
              model="${[components:layoutDefinition.rootContainer, ctxObject:ctxObject, parentPath:'']}" />
    <button class="btn btn-success pull-right" type="submit">Create</button>
  </g:form>
</div>

