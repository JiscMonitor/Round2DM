package uk.ac.jisc.lorix

import grails.util.GrailsNameUtils
import groovy.util.logging.*

import javax.persistence.Transient

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty


/**
 * Abstract base class for OA Monitor Components.
 */

@Log4j
abstract class LorixComponent {

  static final String RD_STATUS         = "KBComponent.Status"
  static final String STATUS_CURRENT       = "Current"
  static final String STATUS_DELETED       = "Deleted"
  static final String STATUS_EXPECTED       = "Expected"
  static final String STATUS_RETIRED       = "Retired"

  static final String RD_EDIT_STATUS      = "KBComponent.EditStatus"
  static final String EDIT_STATUS_APPROVED    = "Approved"
  static final String EDIT_STATUS_IN_PROGRESS  = "In Progress"
  static final String EDIT_STATUS_REJECTED    = "Rejected"

  static auditable = true

  private static refdataDefaults = [
    "status"     : STATUS_CURRENT,
    "editStatus"  : EDIT_STATUS_IN_PROGRESS
  ]

  private static final Map fullDefaultsForClass = [:]

  @Transient
  private def springSecurityService

  @Transient
  private def grailsApplication

  @Transient
  public setSpringSecurityService(sss) {
    this.springSecurityService = sss
  }

  @Transient
  public setGrailsApplication(ga) {
    this.grailsApplication = ga
  }

  /**
   * Generic name for the component. For packages, package name, for journals the journal title. Try to follow DC-Title style naming
   * conventions when trying to decide what to map to this property in a subclass. The name should be a string that reasonably identifies this
   * object when placed in a list of other components.
   */ 
  String name

  /**
   * The normalised name of this component. Lower-case, strip diacritics
   */
  String normname

  /**
   * A URI style shortcode for the component referenced. Used to create unique but human readable URIs for this item.
   */
  String shortcode

  /**
   * Component Status. Linked to refdata table.
   */
  RefdataValue status

  /**
   * Edit Status. Linked to refdata table.
   */
  RefdataValue editStatus

  /**
   *  Provenance
   */
  String provenance

  /**
   * Reference
   */
  String reference

  /**
   * Last updated by
   */
  Object lastUpdatedBy

  /**
   * The source for the record (Whatever it is)
   */
  Source source

  // Timestamps
  Date dateCreated
  Date lastUpdated

  static mappedBy = [
    outgoingCombos: 'fromComponent',
    incomingCombos:'toComponent',
  ]

  static hasMany = [
    outgoingCombos:Combo,
    incomingCombos:Combo,
  ]

  static mapping = {
    id column:'lxc_id'
    version column:'lxc_version'
    name column:'lxc_name'
    normname column:'lxc_normname'
    source column:'lxc_source_fk'
    status column:'lxc_status_rv_fk'
    shortcode column:'lxc_shortcode', index:'kbc_shortcode_idx'
    dateCreated column:'lxc_date_created'
    lastUpdated column:'lxc_last_updated'
  }

  static constraints = {
    name    (nullable:true, blank:false, maxSize:2048)
    shortcode  (nullable:true, blank:false, maxSize:128)
    normname  (nullable:true, blank:false, maxSize:2048)
    status    (nullable:true, blank:false)
    editStatus  (nullable:true, blank:false)
    reference  (nullable:true, blank:false)
    provenance  (nullable:true, blank:false)
    source (nullable:true, blank:false)
  }

  /**
   * Defined parameter-less method to allow for overrides in classes, wishing to define
   * their own way of generating a shortcode.
   * @return
   */
  protected def generateShortcode () {
    if (!shortcode && name) {
      // Generate the short code.
      shortcode = generateShortcode(name)
    }
  }

  static def generateShortcode(String text) {
    def candidate = text.trim().replaceAll(" ","_")

    if ( candidate.length() > 100 )
      candidate = candidate.substring(0,100)

    return incUntilUnique(candidate);
  }

  static def incUntilUnique(name) {
    def result = name;
    if ( KBComponent.findWhere([shortcode : (name)]) ) {
      // There is already a shortcode for that identfier
      int i = 2;
      while ( KBComponent.findWhere([shortcode : "${name}_${i}"]) ) {
        i++
      }
      result = "${name}_${i}"
    }

    result;
  }

  /**
   *  refdataFind generic pattern needed by inplace edit taglib to provide reference data to typedowns and other UI components.
   *  objects implementing this method can be easily located and listed / selected
   */
  static def refdataFind(params) {
    def result = [];
    def ql = null;
    ql = KBComponent.findAllByNameIlike("${params.q}%",params)

    if ( ql ) {
      ql.each { t ->
        result.add([id:"${t.class.name}:${t.id}",text:"${t.name}"])
      }
    }

    result
  }

  protected def generateNormname () {

    // Get the norm_name
    def nname = GOKbTextUtils.normaliseString(name);

    // Set to null if blank.
    normname = nname == "" ? null : nname
  }

  def beforeInsert() {

    // Generate the any necessary values.
    generateShortcode()
    generateNormname()

    // Ensure any defaults defined get set.
    ensureDefaults()

  }

  def afterInsert() {
  }

  def afterUpdate() {
  }

  def afterDelete() {
  }

  def beforeUpdate() {
    if ( name ) {
      if ( !shortcode ) {
        shortcode = generateShortcode(name);
      }
      generateNormname();
    }
    def user = springSecurityService?.currentUser
    if ( user != null ) {
      this.lastUpdatedBy = user
    }
  }

}
