<g:each in="${components}" var="component">

  <g:if test="${component.type=='textField'}">
    <div class="form-group">
      <label class="col-sm-${component.labelWidth?:2} control-label" for="${parentPath}${component.property}"><g:message code="${component.'label.message'}"/></label>
      <div class="col-sm-${component.valueWidth?:10}">
        <input name="${parentPath}${component.property}" 
               type="text" 
               class="form-control" 
               id="${parentPath}${component.property}" 
               placeholder="${component.'placeholder.message'}"/>
      </div>
    </div>
  </g:if>

  <g:if test="${component.type=='refdataLookup'}">
    <div class="form-group">
      <label class="col-sm-${component.labelWidth?:2} control-label" for="${parentPath}${component.property}"><g:message code="${component.'label.message'}"/></label>
      <div class="col-sm-${component.valueWidth?:10}">
        <!-- The hidden property contains the OID In fullyQualifiedClassName:Identifier format -->
        <input type="hidden" name="${parentPath}${component.property}" value=""/>
      
        <div class="input-group">
          <input name="${parentPath}${component.property}_asString" 
                 type="text" 
                 class="form-control" 
                 id="${parentPath}${component.property}" 
                 placeholder="${component.'placeholder.message'}"/>
          <span class="input-group-btn">
            <button id="refdataSearch" class="btn btn-default popupRefdata" type="button" data-search='g:refdataSearch' data-create='g:refdataCreate'>...</button>
          </span>
        </div>

        <a class="wibble">TestSpan</a>

      </div>
    </div>
  </g:if>
</g:each>
