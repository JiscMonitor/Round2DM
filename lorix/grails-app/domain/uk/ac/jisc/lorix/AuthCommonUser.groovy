package uk.ac.jisc.lorix

// Derived from https://github.com/ianibo/CommonAuthModelPlugin for quick prototype



class AuthCommonUser extends AuthCommonParty {

  transient springSecurityService

  String username
  String password
  boolean enabled = true
  boolean accountExpired
  boolean accountLocked

  boolean passwordExpired
  Long defaultPageSize = new Long(10);
  RefdataValue showQuickView
  RefdataValue showInfoIcon


  static transients = ['springSecurityService']

  static constraints = {
    username blank: false, unique: true
    password blank: false, nullable:false
    defaultPageSize blank: false, nullable:true
    showQuickView blank: false, nullable:true
    showInfoIcon blank: false, nullable:true
  }

  static hasMany = [
    oAuthIDs : AuthCommonOAuthId,
    affiliations: AuthCommonAffiliation
  ]

  static mappedBy = [
    affiliations: 'user'
  ]

  static mapping = {
    password column: '`password`'
  }

  Set<AuthCommonRole> getAuthorities() {
    AuthCommonUserAuthCommonRole.findAllByAuthCommonUser(this).collect { it.authCommonRole } as Set
  }

  def beforeInsert() {
    if ( displayName == null ) {
      displayName = username;
    }
    encodePassword()
  }

  def beforeUpdate() {
    if (isDirty('password')) {
      encodePassword()
    }
  }

  protected void encodePassword() {
    password = springSecurityService.encodePassword(password)
  }
}
