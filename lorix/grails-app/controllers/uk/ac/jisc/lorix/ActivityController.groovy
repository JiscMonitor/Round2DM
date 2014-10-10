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
    result.root = lookupOrCreateReferenceProperty(j.root)
    mergeInstanceWithPostData(j.root,result.root)

    // recursiveValidate(j.root, result.root)
    // recursiveSave(j.root, result.root);

    result.status='OK'
    render result as JSON
  }

  def recursiveValidate(formPostData, contextObject) {
    return true;
  }


  // II: This is experimental - I don't think this is the way to go, but leaving the code in place
  // for now just to keep it around.
  def recursiveSave(sourceContext, contextObject) {
    log.debug("recursiveSave(...,${contextObject})");

    // contextObject.getDirtyPropertyNames()
    if ( sourceContext && contextObject ) {

      def class_name_of_result_context = contextObject.class.name
      def domain_class = grailsApplication.getDomainClass(class_name_of_result_context)

      // We need to save the parent object before we create any..
      log.debug("See if ${contextObject} (Instance of ${domain_class}  - with ID ${contextObject.id} is dirty...");
      if ( ( contextObject.isDirty() ) || ( contextObject.id == null ) ) {
        log.debug("yes.. save ${contextObject}");
        if ( contextObject.save() ) {
          log.debug("Saved ok : id is ${contextObject.id}");
        }
        else {
          log.debug("Save error ${contextObject.errors}");
        }
      }
      else {
        log.debug("Nooo");
      }

      sourceContext.each { k, v ->
        def p = domain_class.getPersistentProperty(k)  //  GrailsDomainClassProperty
        if ( p ) {
          if ( p.isAssociation() ) {
            log.debug("${k} is an association for saving..");
            if ( p.isOneToMany() ) {
              int i=0;
              contextObject[k].each { db_object ->
                log.debug("Consider ${db_object}");
                recursiveSave(v[i++],db_object)
              }
            }
            else {
            }
          }
        }
      }
    }

    log.debug("recursiveSave ${contextObject} finished...");
    return true;
  }

  def lookupOrCreateReferenceProperty(formPostData) {
    def result = null
    if ( formPostData.__oid ) {
      def oid = formPostData.__oid
      if ( oid ) {
        def oid_parts = oid.split(':');
        //def domain_class = grailsApplication.getArtefact('Domain',oid_parts[0])
        def domain_class = grailsApplication.getDomainClass(oid_parts[0])
        if ( oid_parts[1] == 'NEW' ) {
          log.debug("creating new instance of ${oid_parts[0]}");
          result = domain_class.getClazz().newInstance();
        }
        else {
          log.debug("referencing ${oid}");
          result = domain_class.getClazz().get(oid_parts[1])
        }
      }
    }

    // Grails can get really confused if we start adding non-belongsTo objects to hasMany sets when
    // the object is not saved. This, unfortunately, means we need to save objects before we start adding stuff
    // to collections. Really, I would like to validate objects before saving them.

    log.debug("lookupOrCreateReferenceProperty(${formPostData}) returns ${result}");

    result
  }

  // Look in sourceContext for element, and see if there is a corresponding thing in resultContext to set
  // Assume we are the root of the object tree for now.
  def mergeInstanceWithPostData(sourceContext, resultContext) {
    if ( resultContext ) {
      def class_name_of_result_context = resultContext.class.name
      log.debug("Iterate through all properties at this level... ${class_name_of_result_context}");
      def domain_class = grailsApplication.getDomainClass(class_name_of_result_context)

     

      // It might be that we should merge in ordinary properties before we "save" the object so that any
      // not-null validation rules will pass. that sucks ass a little.

      // Pass 1 : Only set scalar properties like strings, ints, etc.
      sourceContext.each { k, v ->
        log.debug("Consider ${k} -> ${v}");
        def p = domain_class.getPersistentProperty(k)  //  GrailsDomainClassProperty
        if ( p ) {
          log.debug("context has property ${k} :: ${p}");
          if ( p.isAssociation() ) {
            // Do nothing - pass one is just about values
          }
          else {
            log.debug("Setting scalar property ${k} to ${v}");
            resultContext[k] = v
          }
        }
      }

      log.debug("Attempting to save object after setting scalar properties, but before setting any reference data...");
      resultContext.save()

      // Stage 2 - set reference properties
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
            else {
              log.debug("Unhandled association type");
            }
          }
          else {
            // Taken care of in pass one above..
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

    def methodName = 'addTo'+propName.capitalize()
             
    valueFromPost.each { child_row_data ->
      log.debug("child row: ${child_row_data}");
      if ( idx < num_child_objects ) {
        // This seems to always trigger for the first item.. not correct!!
        log.debug("Already have a row at this position.. update");
        // Should really check that __oid matches the id of the object in our hands...
      }
      else {
        log.debug("Adding a row to the collection property.. lookup or create depending on OID");
        def new_child_object = lookupOrCreateReferenceProperty(child_row_data)

        if ( new_child_object ) {
          // addTo is a convienience method that sets any reciprocal property on the associated object
          log.debug("Adding child object to parent object");
          parentDBObject."${methodName}"(new_child_object)
          // db_coll.add(new_child_object)

          log.debug("Setting properties on child object from form post data");
          // Remember this will trigger a save after scalar properies are set, but before reference properties / collections are processed
          mergeInstanceWithPostData(child_row_data,new_child_object)

        }
        else {
          log.error("Something has gone amiss : adding null to a collection.. lookup or create reference property for ${child_row_data} returned null");
        }
      }
      idx++
    }
  }
}
