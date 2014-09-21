package com.k_int.shib

import uk.ac.jisc.lorix.*
import org.springframework.security.core.context.SecurityContextHolder

public class ShibAuthFilter extends org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter {

  def grailsApplication

  def getPreAuthenticatedPrincipal(javax.servlet.http.HttpServletRequest request) {

    log.debug("ShibAuthFilter::getPreAuthenticatedPrincipal ${request}");
    request.getAttributeNames().each {
      log.debug("attr: ${it} : ${request.getAttribute(it)}");
    }
    request.getParameterNames().each {
      log.debug("param: ${it} : ${request.getParameter(it)}");
    }
    log.debug("Headers..");
    request.getHeaderNames().each {
      log.debug("param: ${it}");
    }
    request.getSession().getAttributeNames().each {
      log.debug("param: ${it} : ${request.getSession().getAttribute(it)}");
    }


    def result

    if ( grailsApplication?.config?.authmethod=='shib' ) {
      log.debug("Checking shib auth..");

      // This should get hold of the 
      def preauth_info = SecurityContextHolder.getContext().getAuthentication()
      log.debug("PreauthInfo: ${preauth_info}");

      if ( request.getRemoteUser() != null ) {
        log.debug("In shibboleth authentication mode. If we're here - the user is pre-authenticated. Extract username and make sure there is a user record");
        // User ID should be in request.getAttribute('persistent-id');
        log.debug("Remote User(fn):: ${request.getRemoteUser()} (class: ${request.getRemoteUser()?.class?.name})");
        log.debug("User Principal:: ${request.getUserPrincipal()} (class: ${request.getUserPrincipal()?.class?.name})");
        log.debug("Persistent Id:: ${request.getAttribute('persistent-id')}");
        log.debug("Remote User:: ${request.getAttribute('REMOTE_USER')} (class: ${request.getAttribute('REMOTE_USER')?.class?.name})");
  

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
