<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <h1>User profile</h1>

    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <dl class="dl-horizontal">
            <dt>
              Display Name
            </dt>
            <dd>
              <input name="displayName" value="${user.displayName}"/>
            </dd>
          </dl>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="well">
            <h3>Existing Memberships</h3>
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Organisation</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Date Requested / Actioned</th>
                  <th>Home?</th>
                  <th>Actions</th>
              </thead>
              <tbody>
                <g:each in="${user.affiliations}" var="a">
                  <tr>
                    <td>${a.org.name}</td>
                    <td>${a.role.value}</td>
                    <td>${a.status.value}</td>
                    <td>${a.dateRequested} / ${a.dateActioned}</td>
                    <td>${a.org.userHome?'Yes':''}</td>
                    <td></td>
                  </tr>
                </g:each>
              </tbody>
            </table>
          </div>
        </div>
        <div class="col-md-6">
          <div class="well">
            <h3>Request new membership</h3>
          </div>
        </div>
      </div>
  </body>
</html>
