<html>
<head>
</head>
<body>
  <#list users as u>
    <h3>${u.get("name")}</h3>
    id: ${u.get("id")}
    <br>
    email: ${u.get("email")}
  </#list>
</body>
</html>
