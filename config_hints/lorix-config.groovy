localauth=true
sysusers = [
  [
    name:'admin',
    pass:'admin',
    display:'Admin',
    email:'admin@localhost',
    roles: [ 'ROLE_USER', 'ROLE_ADMIN']
  ]
]
