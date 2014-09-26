
_container.gsp
<g:each in="${components}" var="component">
  ${component}
  <g:if test="${component.type=='textField'}">
    <div class="form-group">
      <label for="${component.property}"><g:message code="${component.prompt?:component.property}"/></label>
      <input type="text" class="form-control" id="${component.property}" placeholder="${component.placeholder?:component.property}">
    </div>
  </g:if>
</g:each>
