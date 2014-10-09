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

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def ngEdit() {

    def result = [:]

    log.debug("ngEdit::${params}");
    def j = request.JSON
    log.debug("Post data: ${j}");

    // Look in j for property 'root' and lookup or create based on root.__oid
    result.root = lookupOrCreateReferenceProperty('root', j)
    processThree(j.root,result.root)

    result.status='OK'
    render result as JSON
  }

  def lookupOrCreateReferenceProperty(elementName, formPostData) {
    def result = null
    if ( formPostData[elementName] ) {
      def oid = formPostData[elementName].__oid
      if ( oid ) {
        def oid_parts = oid.split(':');
        //def domain_class = grailsApplication.getArtefact('Domain',oid_parts[0])
        def domain_class = grailsApplication.getDomainClass(oid_parts[0])
        if ( oid_parts[1] == 'NEW' ) {
          log.debug("creating new instance...${domain_class}");
          result = domain_class.getClazz().newInstance();
        }
        else {
          result = domain_class.getClazz().get(oid_parts[1])
        }
      }
    }
    result
  }

  // Look in sourceContext for element, and see if there is a corresponding thing in resultContext to set
  // Assume we are the root of the object tree for now.
  def processThree(sourceContext, resultContext) {
    if ( resultContext ) {
      def class_name_of_result_context = resultContext.class.name
      log.debug("Iterate through all properties at this level... ${class_name_of_result_context}");
      def domain_class = grailsApplication.getDomainClass(class_name_of_result_context)

      sourceContext.each { k, v ->
        log.debug("Consider ${k} -> ${v}");
        def p = domain_class.getPersistentProperty(k)  //  GrailsDomainClassProperty
        if ( p ) {
          log.debug("context has property ${k} :: ${p}");
          if ( p.isAssociation() ) {
            log.debug("${k} is an association");
            if ( p.isOneToMany() ) {
              // Synchronize the data from the form post with the data in the DB, create, update and delete records as needed.
              processOneToManyProperty(k, v, resultContext);
            }
          }
        }
      }
    }
  }

  def processOneToManyProperty(propName, valueFromPost, parentDBObject) {
    log.debug("${propName} is One to many");
    int idx=0;
 
    // See if we need to initialise the local collection
    if ( parentDBObject[propName] == null ) {
      // Collection property is null - we need to initialise it
      parentDBObject[propName] = []
    }

    // Grab the collection of persistent objects under this property
    def db_coll = parentDBObject[propName]
    int num_child_objects = db_coll.size();
             
    valueFromPost.each { child_row_data ->
      log.debug("child row: ${child_row_data}");
      if ( idx <= num_child_objects ) {
        log.debug("Already have a row at this position.. update");
      }
      else {
        log.debug("Adding a row to the collection property.. lookup or create depending on OID");
      }
      idx++
    }
  }
}
