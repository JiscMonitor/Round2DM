package uk.ac.jisc.lorix


import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.plugin.springsecurity.SpringSecurityUtils


class HomeController {

  def springSecurityService

  private static final String HEADER_PRAGMA = "Pragma";
  private static final String HEADER_EXPIRES = "Expires";
  private static final String HEADER_CACHE_CONTROL = "Cache-Control";

  protected preventCache (response) {
    response.setHeader(HEADER_PRAGMA, "no-cache");
    response.setDateHeader(HEADER_EXPIRES, 1L);
    response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
    response.addHeader(HEADER_CACHE_CONTROL, "no-store");
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    def user = springSecurityService.getCurrentUser()
    if ( ( user.affiliations.size() == 0 ) || ( user.displayName == null ) || ( user.displayName.length() == 0 ) ) {
      redirect(controller:'user', action:'profile');
    }
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index2() {
    log.debug(request)
    redirect(action:'index')
  }
}
