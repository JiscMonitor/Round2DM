package com.k_int.shib

import uk.ac.jisc.lorix.*
import org.springframework.security.core.context.SecurityContextHolder

public class ShibAuthFilter extends org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter {

  def grailsApplication

  def getPreAuthenticatedPrincipal(javax.servlet.http.HttpServletRequest request) {

    log.debug("ShibAuthFilter::getPreAuthenticatedPrincipal ${request}");
    def result

    if ( grailsApplication?.config?.authmethod=='shib' ) {

      if ( request.getRemoteUser() != null ) {

        log.debug("In shibboleth authentication mode. If we're here - the user is pre-authenticated. Extract username and make sure there is a user record");

        // Hmm.. Remember that in tomcat getAttributeNames does not return certain scoped attrs - which messes with my head!
        log.debug("Persistent Id:: ${request.getAttribute('persistent-id')}");

        def tst_attrs = [ 'persistent-id', 
                      'eppn',
                      'mail',
                      'givenname',
                      'affiliation', 
                      'uid', 
                      'Shib-Session-Index', 
                      'Shib-Session-ID', 
                      'Shib-AuthnContext-Class', 
                      'Shib-Application-ID', 
                      'unscoped-affiliation',
                      'primary-affiliation',
                      'entitlement',
                      'targeted-id',
                      'primary-orgunit-dn',
                      'orgunit-dn',
                      'org-dn',
                      'cn',
                      'employeeNumber',
                      'displayName',
                      'description'
                    ]
        tst_attrs.each { it ->
          log.debug("tst:: ${it} : ${request.getAttribute(it)}");
        }
   
        AuthCommonUser.withTransaction { status ->
          def existing_user = AuthCommonUser.findByUsername(request.getRemoteUser())
          if ( existing_user ) {
            log.debug("User ${request.getRemoteUser()} found, all is well");
          }
          else {

            log.debug("User ${request.getRemoteUser()} not found.. create");

            existing_user = new AuthCommonUser(
                                     username:request.getRemoteUser(),
                                     password:'**',
                                     enabled:true,
                                     accountExpired:false,
                                     accountLocked:false,
                                     passwordExpired:false,
                                     email:request.getAttribute('email'))
                                     // email:request.getAttribute('email'))

            if ( existing_user.save(flush:true) ) {
              log.debug("Created user, allocating user role");
              def userRole = AuthCommonRole.findByAuthority('ROLE_USER')
  
              if ( userRole ) {
                log.debug("looked up user role: ${userRole}");
                def new_role_allocation = new AuthCommonUserAuthCommonRole(authCommonUser:existing_user,authCommonRole:userRole);
  
                if ( new_role_allocation.save(flush:true) ) {
                  log.debug("New role created...");
                }
                else {
                    new_role_allocation.errors.each { e ->
                    log.error(e);
                  }
                }
              }
              else {
                log.error("Unable to look up ROLE_USER");
              }
            }
          }
        }

        result = request.getRemoteUser()
      }
    }
  }

  def getPreAuthenticatedCredentials(javax.servlet.http.HttpServletRequest request) {
    log.debug("getPreAuthenticatedCredentials..${request}");
    return "";
  }
}
