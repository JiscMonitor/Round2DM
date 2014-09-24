<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <h1>Admin :: Pending User Affiliation Requests</h1>

    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <table class="table table-striped">
            <thead>
              <tr>
                <th>User</th>
                <th>Organisation</th>
                <th>Role Requested</th>
                <th>Date Requested</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${pending}" var="a">
                <tr>
                  <td>${a.user.displayName}</td>
                  <td>${a.org.name}</td>
                  <td>${a.role.value}</td>
                  <td>${a.dateRequested}</td>
                  <td>${a.status.value}</td>
                  <td>
                    <g:link controller="admin" action="approveAffiliation" id="${a.id}" class="btn btn-success">Approve</g:link>
                    <g:link controller="admin" action="rejectAffiliation" id="${a.id}" class="btn btn-warning">Reject</g:link>
                  </td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </body>
</html>
