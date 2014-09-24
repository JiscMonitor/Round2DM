package uk.ac.jisc.lorix

import grails.transaction.Transactional
import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import org.apache.http.*
import org.apache.http.protocol.*
import org.apache.log4j.*
import java.text.SimpleDateFormat


@Transactional
class AdminService {

  def syncFederationData() {

      log.debug("syncFederationData()");
    
      def target_service = new RESTClient('http://metadata.ukfederation.org.uk')

      try {
        def ukfam
        target_service.request(GET, ContentType.XML) { request ->
          uri.path='/ukfederation-metadata.xml'
          response.success = { resp, data ->
            // data is the xml document
            ukfam = data;
          }
          response.failure = { resp ->
            println("Error - ${resp.status}");
            System.out << resp
          }
        }

        println("Processing...");
        def current_status = RefdataCategory.lookupOrCreate('status', 'current')

        if ( ukfam ) {
          ukfam.EntityDescriptor.each { ed ->
            ed.Extensions?.Scope?.each { scope_from_file ->
              // println("ID: ${ed.@ID.text()}")
              // println("OrgName: ${ed.Organization?.OrganizationName?.text()}")
              // println("Display: ${ed.Organization?.OrganizationDisplayName?.text()}")
              // println("URL: ${ed.Organization?.OrganizationURL?.text()}")
              if ( scope_from_file.text().endsWith('.ac.uk') ) {
                log.debug("Scope: ${scope_from_file} ${ed.Organization?.OrganizationName?.text()} ${ed.Organization?.OrganizationDisplayName?.text()} ${ed.Organization?.OrganizationURL?.text()}");
              
                Organisation.withNewTransaction {
                  // A scope represents an identifier for an organisation
                  def matched_by_scope = Organisation.componentsByIdentifier('shibScope',scope_from_file.text().toString())

                  try {
                    if ( matched_by_scope.size() == 0 ) {
                      log.debug("None matched by scope...");
  
                      // Need to add - the question now becomes - do we already have this org
                      def matched_by_shib_id = Organisation.componentsByIdentifier('shibId',ed.@ID.text().toString())
  
                      if ( matched_by_shib_id.size() == 0 ) {
                        log.debug("New org with this scope");
                        def new_org = new Organisation(name:ed.Organization?.OrganizationName?.text(), 
                                                       reference:ed.@ID.text(),
                                                       url:ed.Organization?.OrganizationURL?.text(),
                                                       status:current_status
                                                       )
                        log.debug("saving....");
                        if ( new_org.save() ) {
                          log.debug("New org saved OK..");
                        }
                        else {
                          log.debug("Problem creating new org.. ${new_org.errors}");
                        }
  
                        def fam_id = Identifier.lookupOrCreateCanonicalIdentifier('shibId', ed.@ID.text())
                        def fam_id_combo = new Combo(fromComponent:new_org, toComponent:fam_id, rel:'hasId').save()
                        def scope_id = Identifier.lookupOrCreateCanonicalIdentifier('shibScope', scope_from_file.text().toString())
                        def fam_scope_combo = new Combo(fromComponent:new_org, toComponent:scope_id, rel:'hasId').save(flush:true)
                      }
                      else {
                        log.debug("Add scope to existing org");
                        def scope_id = Identifier.lookupOrCreateCanonicalIdentifier('shibScope', scope_from_file.text().toString())
                        def fam_scope_combo = new Combo(fromComponent:matched_by_shib_id.get(0), toComponent:scope_id, rel:'hasId').save()
                      }
                    }
                    else {
                      log.debug("Matched existing org with scope");
                    }
                  }
                  catch ( Exception e ) {
                    log.error("Problem",e);
                  }
                  finally {
                    log.debug("done");
                  }
                }
              }
            }
          }
        }
      }
      catch ( Exception e ) {
        e.printStackTrace();
      }
  }
}
