<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="_header.jspf" %>
<h1>Izdavači</h1>

<form method="get" action="${pageContext.request.contextPath}/admin/izdavaci" style="margin-bottom:16px;">
  <input type="text" name="pretraga" placeholder="Pretraga po nazivu" value="${param.pretraga}" style="width:240px;padding:6px;">
  <button type="submit">Pretraži</button>
</form>

<table>
  <tr><th>ID</th><th>Naziv</th><th>Država</th><th>Broj igara</th><th>Akcije</th></tr>
  <c:forEach var="i" items="${izdavaci}">
    <tr>
      <td>${i.idIzdavac}</td>
      <td>${i.naziv}</td>
      <td>${i.drzava}</td>
      <td>${i.igre.size()}</td>
      <td>
        <a class="btn" href="${pageContext.request.contextPath}/admin/izdavaci?id=${i.idIzdavac}">Izmeni</a>
        <form class="inline" method="post" action="${pageContext.request.contextPath}/admin/izdavaci" onsubmit="return confirm('Obrisati izdavača?');">
          <input type="hidden" name="akcija" value="obrisi">
          <input type="hidden" name="id" value="${i.idIzdavac}">
          <button type="submit" class="danger">Obriši</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

<div class="card">
  <c:choose>
    <c:when test="${not empty izdavacZaIzmenu}">
      <h2>Izmeni izdavača</h2>
      <form method="post" action="${pageContext.request.contextPath}/admin/izdavaci">
        <input type="hidden" name="akcija" value="izmeni">
        <input type="hidden" name="id" value="${izdavacZaIzmenu.idIzdavac}">
        <label>Naziv <input type="text" name="naziv" value="${izdavacZaIzmenu.naziv}" required></label>
        <label>Država <input type="text" name="drzava" value="${izdavacZaIzmenu.drzava}"></label>
        <div style="margin-top:16px;">
          <button type="submit">Sačuvaj izmene</button>
          <a class="btn secondary" href="${pageContext.request.contextPath}/admin/izdavaci">Otkaži</a>
        </div>
      </form>
    </c:when>
    <c:otherwise>
      <h2>Novi izdavač</h2>
      <form method="post" action="${pageContext.request.contextPath}/admin/izdavaci">
        <input type="hidden" name="akcija" value="kreiraj">
        <label>Naziv <input type="text" name="naziv" required></label>
        <label>Država <input type="text" name="drzava"></label>
        <div style="margin-top:16px;"><button type="submit">Sačuvaj</button></div>
      </form>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="_footer.jspf" %>
