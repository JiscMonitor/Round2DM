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


<div id="myModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content"/>
  </div>
</div>

