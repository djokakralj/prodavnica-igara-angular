<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="sr">
<head>
  <meta charset="UTF-8">
  <title>Prijava — Prodavnica igara (Admin)</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<div class="login-wrap">
  <div class="card">
    <h2>Prijava administratora</h2>
    <c:if test="${not empty greska}">
      <div class="greska">${greska}</div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
      <label>Korisničko ime
        <input type="text" name="korisnickoIme" required autofocus>
      </label>
      <label>Lozinka
        <input type="password" name="lozinka" required>
      </label>
      <div style="margin-top:16px;">
        <button type="submit">Prijavi se</button>
      </div>
    </form>
  </div>
</div>
</body>
</html>
