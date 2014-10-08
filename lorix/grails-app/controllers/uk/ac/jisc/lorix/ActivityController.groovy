package uk.ac.jisc.lorix

import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils

class ActivityController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def edit() { 
    // Step 1:: iterate through tree processing __oid entries
    def oid_params = request.getParameterNames().findAll{it.endsWith('__oid')}
    oid_params.sort()

    def result = [:]

    process(result, oid_params)

    log.debug("result.root = ${result.root}.. calling bind data ${params}");

    // Do databinding..
    bindData(result.root, params, "root")

    log.debug("After bind data result.root.name = ${result.root.name}");

    result.saveResult = result.root.save(flush:true)
    if ( result.saveResult ) {
      log.debug("Save::OK");
    }
    else {
      log.debug("Save::Error");
      result.root.errors.each { 
        log.debug("Problem: ${it}");
      }
    }

    redirect(controller:'home', action:'index');
  }

  // Return the context
  def process(result, oidattrlist) {

    result.root = ''

    oidattrlist.each { oidattr ->
      log.debug("Processing oidattr :: ${oidattr}");
      def oid_parts = params[oidattr].split(':');
      def access_path = oidattr.replace('.__oid','') // trim .__oid
      if ( oid_parts[1]=='NEW' ) {
        log.debug("Request to create a new instance of class ${oid_parts[0]}")
        def domain_class = grailsApplication.getArtefact('Domain',oid_parts[0])
        def rslt = domain_class.getClazz().newInstance();
        log.debug("evaluate x.${access_path}=y");
        groovy.util.Eval.xy(result,rslt,"x.${access_path}=y");
      }
      else {
        log.debug("lookup existing oid...");
      }
      log.debug("set ${access_path} to ${params[oidattr]}");
      
    }
  }

}
