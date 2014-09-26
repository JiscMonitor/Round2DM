package uk.ac.jisc.lorix

import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils

class CreateController {

  def grailsApplication
  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def inline() { 

    log.debug("Inline...${params}");

    def result = [:]
    result.ctxClazz = Class.forName(params.cls)
    result.ctxObject = result.ctxClazz.newInstance()
    result.onSuccess = params.onSuccess

    switch ( params.templateType ) {
      case 'static' :
        render(view:params.view, model:result)
        break;
      case 'dataDriven' :
        log.debug("lookup ${params.view}");
        result.layoutDefinition = grailsApplication.config.globalLayouts[params.view]
        render(view:'dataDriven',model:result)
        break;
    }
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def process() {
    log.debug("Process ${params}");

    def result=[:]
    def formctx=''
    def class_to_create = params[formctx+'__clazz']
    def layoutDefinition = grailsApplication.config.globalLayouts[params.'__view']

    if ( class_to_create != null ) {
      log.debug("Create instance of \"${class_to_create}\"");
      def ctxClazz = Class.forName(class_to_create)
      def ctxObject = ctxClazz.newInstance()

      // Iterate through definition, loading fields and setting as appropriate
      populate('', ctxObject, layoutDefinition.rootContainer)
    }

    if ( params.__onSuccess ) {
      log.debug("Redirect to url:: ${params.__onSuccess}");
      redirect(url:params.__onSuccess)
    }
    else {
      redirect(controller:'home');
    }
  }

  private def populate(ctx, obj, defn) {
    defn.each { component ->
      switch ( component.type ) {
        case 'textField':
          log.debug("text field.... ${ctx} ${component.property} = ${params[ctx + component.property]}"); 
          obj[component.property] = params[ctx + component.property]
          break;
      }
    }
  }
}
