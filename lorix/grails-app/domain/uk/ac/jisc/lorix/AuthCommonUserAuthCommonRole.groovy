package uk.ac.jisc.lorix

// Derived from https://github.com/ianibo/CommonAuthModelPlugin for quick prototype



import org.apache.commons.lang.builder.HashCodeBuilder

class AuthCommonUserAuthCommonRole implements Serializable {

  private static final long serialVersionUID = 1

  AuthCommonUser authCommonUser
  AuthCommonRole authCommonRole




  boolean equals(other) {
    if (!(other instanceof AuthCommonUserAuthCommonRole)) {
      return false
    }

    other.authCommonUser?.id == authCommonUser?.id &&
    other.authCommonRole?.id == authCommonRole?.id
  }

  int hashCode() {
    def builder = new HashCodeBuilder()
    if (authCommonUser) builder.append(authCommonUser.id)
    if (authCommonRole) builder.append(authCommonRole.id)
    builder.toHashCode()
  }

  static AuthCommonUserAuthCommonRole get(long authCommonUserId, long authCommonRoleId) {
    AuthCommonUserAuthCommonRole.where {
      authCommonUser == AuthCommonUser.load(authCommonUserId) &&
      authCommonRole == AuthCommonRole.load(authCommonRoleId)
    }.get()
  }

  static AuthCommonUserAuthCommonRole create(AuthCommonUser authCommonUser, AuthCommonRole authCommonRole, boolean flush = false) {
    new AuthCommonUserAuthCommonRole(authCommonUser: authCommonUser, authCommonRole: authCommonRole).save(flush: flush, insert: true)
  }

  static boolean remove(AuthCommonUser u, AuthCommonRole r, boolean flush = false) {

    int rowCount = AuthCommonUserAuthCommonRole.where {
      authCommonUser == AuthCommonUser.load(u.id) &&
      authCommonRole == AuthCommonRole.load(r.id)
    }.deleteAll()

    rowCount > 0
  }

  static void removeAll(AuthCommonUser u) {
    AuthCommonUserAuthCommonRole.where {
      authCommonUser == AuthCommonUser.load(u.id)
    }.deleteAll()
  }

  static void removeAll(AuthCommonRole r) {
    AuthCommonUserAuthCommonRole.where {
      authCommonRole == AuthCommonRole.load(r.id)
    }.deleteAll()
  }

  static mapping = {
    table name:'auth_common_user_role'
    id composite: ['authCommonRole', 'authCommonUser']
    version false
  }
}
