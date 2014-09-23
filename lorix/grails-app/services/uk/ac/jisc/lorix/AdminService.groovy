package uk.ac.jisc.lorix

import grails.transaction.Transactional

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
        if ( ukfam ) {
          ukfam.EntityDescriptor.each { ed ->
            ed.Extensions?.Scope?.each {
              // println("ID: ${ed.@ID.text()}")
              // println("OrgName: ${ed.Organization?.OrganizationName?.text()}")
              // println("Display: ${ed.Organization?.OrganizationDisplayName?.text()}")
              // println("URL: ${ed.Organization?.OrganizationURL?.text()}")
              if ( it.text().endsWith('.ac.uk') ) {
                log.debug("Scope: ${it} ${ed.Organization?.OrganizationName?.text()} ${ed.Organization?.OrganizationDisplayName?.text()} ${ed.Organization?.OrganizationURL?.text()}");
              
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
