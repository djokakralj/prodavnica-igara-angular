<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="_header.jspf" %>
<h1>Platforme</h1>

<table>
  <tr><th>ID</th><th>Naziv</th><th>Akcije</th></tr>
  <c:forEach var="p" items="${platforme}">
    <tr>
      <td>${p.idPlatforma}</td>
      <td>${p.naziv}</td>
      <td>
        <a class="btn" href="${pageContext.request.contextPath}/admin/platforme?id=${p.idPlatforma}">Izmeni</a>
        <form class="inline" method="post" action="${pageContext.request.contextPath}/admin/platforme" onsubmit="return confirm('Obrisati platformu?');">
          <input type="hidden" name="akcija" value="obrisi">
          <input type="hidden" name="id" value="${p.idPlatforma}">
          <button type="submit" class="danger">Obriši</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

<div class="card">
  <c:choose>
    <c:when test="${not empty platformaZaIzmenu}">
      <h2>Izmeni platformu</h2>
      <form method="post" action="${pageContext.request.contextPath}/admin/platforme">
        <input type="hidden" name="akcija" value="izmeni">
        <input type="hidden" name="id" value="${platformaZaIzmenu.idPlatforma}">
        <label>Naziv <input type="text" name="naziv" value="${platformaZaIzmenu.naziv}" required></label>
        <div style="margin-top:16px;">
          <button type="submit">Sačuvaj izmene</button>
          <a class="btn secondary" href="${pageContext.request.contextPath}/admin/platforme">Otkaži</a>
        </div>
      </form>
    </c:when>
    <c:otherwise>
      <h2>Nova platforma</h2>
      <form method="post" action="${pageContext.request.contextPath}/admin/platforme">
        <input type="hidden" name="akcija" value="kreiraj">
        <label>Naziv <input type="text" name="naziv" required></label>
        <div style="margin-top:16px;"><button type="submit">Sačuvaj</button></div>
      </form>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="_footer.jspf" %>
