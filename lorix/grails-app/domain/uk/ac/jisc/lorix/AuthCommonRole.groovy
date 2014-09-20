package uk.ac.jisc.lorix

// Derived from https://github.com/ianibo/CommonAuthModelPlugin for quick prototype



class AuthCommonRole {

  String authority

  static mapping = {
    table name:'auth_common_role'
    cache true
  }



  static constraints = {
    authority blank: false, unique: true
  }
}
