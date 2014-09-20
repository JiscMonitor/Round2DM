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
  }
}
