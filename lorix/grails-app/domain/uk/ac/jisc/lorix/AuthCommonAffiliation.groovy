package uk.ac.jisc.lorix

// Derived from https://github.com/ianibo/CommonAuthModelPlugin for quick prototype

class AuthCommonAffiliation {

  AuthCommonUser user
  Organisation org
  RefdataValue status;  // 0=Pending, 1=Approved, 2=Rejected
  RefdataValue role  // 0=unspecified, 5=admin

  static mapping = {
    table name:'auth_common_affiliation'
  }

  static constraints = {
  }
}
