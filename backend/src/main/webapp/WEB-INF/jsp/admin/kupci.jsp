<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="_header.jspf" %>
<h1>Kupci</h1>

<form method="get" action="${pageContext.request.contextPath}/admin/kupci" style="margin-bottom:16px;">
  <input type="text" name="pretraga" placeholder="Pretraga po korisničkom imenu ili emailu" value="${param.pretraga}" style="width:280px;padding:6px;">
  <button type="submit">Pretraži</button>
</form>

<table>
  <tr><th>ID</th><th>Korisničko ime</th><th>Email</th><th>Adresa</th><th>Telefon</th><th>Status</th><th></th></tr>
  <c:forEach var="k" items="${kupci}">
    <tr>
      <td>${k.idKorisnik}</td>
      <td>${k.korisnickoIme}</td>
      <td>${k.email}</td>
      <td>${k.adresa}</td>
      <td>${k.brojTelefona}</td>
      <td>
        <c:choose>
          <c:when test="${k.blokiran}"><span class="blokiran">Blokiran</span></c:when>
          <c:otherwise>Aktivan</c:otherwise>
        </c:choose>
      </td>
      <td>
        <form class="inline" method="post" action="${pageContext.request.contextPath}/admin/kupci">
          <input type="hidden" name="id" value="${k.idKorisnik}">
          <c:choose>
            <c:when test="${k.blokiran}">
              <input type="hidden" name="akcija" value="odblokiraj">
              <button type="submit">Odblokiraj</button>
            </c:when>
            <c:otherwise>
              <input type="hidden" name="akcija" value="blokiraj">
              <button type="submit" class="danger">Blokiraj</button>
            </c:otherwise>
          </c:choose>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

<%@ include file="_footer.jspf" %>
