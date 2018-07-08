<html>
<head>
</head>
<body>
  <table>
  <th>Severity</th>
  <th>Body</th>
  <th>User ID</th>
  <#list notifications as n>
    <tr>
      <td>
        ${n.get("severity")}
      </td>
      <td>
        ${n.get("body")}
      </td>
      <td>
        ${n.get("user_id")}
      </td>
    </tr>
  </#list>
  </table>
</body>
</html>
