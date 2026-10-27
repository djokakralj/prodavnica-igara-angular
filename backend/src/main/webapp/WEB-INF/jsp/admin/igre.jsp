<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="_header.jspf" %>
<h1>Igre</h1>

<form method="get" action="${pageContext.request.contextPath}/admin/igre" style="margin-bottom:16px;">
  <input type="text" name="pretraga" placeholder="Pretraga po nazivu" value="${param.pretraga}" style="width:240px;padding:6px;">
  <button type="submit">Pretraži</button>
</form>

<table>
  <tr><th>ID</th><th>Naziv</th><th>Cena</th><th>Žanr</th><th>Na stanju</th><th>Izdavač</th><th>Akcije</th></tr>
  <c:forEach var="igra" items="${igre}">
    <tr>
      <td>${igra.idIgra}</td>
      <td>${igra.naziv}</td>
      <td>${igra.cena}</td>
      <td>${igra.zanr}</td>
      <td>${igra.kolicinaNaStanju}</td>
      <td>${igra.izdavac.naziv}</td>
      <td>
        <a class="btn" href="${pageContext.request.contextPath}/admin/igre?id=${igra.idIgra}">Izmeni</a>
        <form class="inline" method="post" action="${pageContext.request.contextPath}/admin/igre" onsubmit="return confirm('Obrisati igru?');">
          <input type="hidden" name="akcija" value="obrisi">
          <input type="hidden" name="id" value="${igra.idIgra}">
          <button type="submit" class="danger">Obriši</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

<div class="card">
  <c:choose>
    <c:when test="${not empty igraZaIzmenu}">
      <h2>Izmeni igru</h2>
      <form method="post" action="${pageContext.request.contextPath}/admin/igre">
        <input type="hidden" name="akcija" value="izmeni">
        <input type="hidden" name="id" value="${igraZaIzmenu.idIgra}">
        <label>Naziv <input type="text" name="naziv" value="${igraZaIzmenu.naziv}" required></label>
        <label>Opis <textarea name="opis" rows="3">${igraZaIzmenu.opis}</textarea></label>
        <label>Cena <input type="number" step="0.01" min="0" name="cena" value="${igraZaIzmenu.cena}" required></label>
        <label>Godina izdanja <input type="number" name="godinaIzdanja" value="${igraZaIzmenu.godinaIzdanja}"></label>
        <label>Žanr <input type="text" name="zanr" value="${igraZaIzmenu.zanr}"></label>
        <label>Količina na stanju <input type="number" min="0" name="kolicinaNaStanju" value="${igraZaIzmenu.kolicinaNaStanju}" required></label>
        <label>Izdavač
          <select name="idIzdavac">
            <option value="">—</option>
            <c:forEach var="iz" items="${izdavaci}">
              <option value="${iz.idIzdavac}" ${iz.idIzdavac == igraZaIzmenu.izdavac.idIzdavac ? 'selected' : ''}>${iz.naziv}</option>
            </c:forEach>
          </select>
        </label>
        <div style="margin-top:16px;">
          <button type="submit">Sačuvaj izmene</button>
          <a class="btn secondary" href="${pageContext.request.contextPath}/admin/igre">Otkaži</a>
        </div>
      </form>

      <h2 style="margin-top:32px;">Platforme za "${igraZaIzmenu.naziv}"</h2>
      <table>
        <tr><th>Platforma</th><th>Datum portovanja</th><th>Ekskluzivno</th><th></th></tr>
        <c:forEach var="ip" items="${igraZaIzmenu.platforme}">
          <tr>
            <td>${ip.platforma.naziv}</td>
            <td>${ip.datumPortovanja}</td>
            <td>${ip.ekskluzivno ? 'Da' : 'Ne'}</td>
            <td>
              <form class="inline" method="post" action="${pageContext.request.contextPath}/admin/igre">
                <input type="hidden" name="akcija" value="ukloniPlatformu">
                <input type="hidden" name="idIgra" value="${igraZaIzmenu.idIgra}">
                <input type="hidden" name="idPlatforma" value="${ip.platforma.idPlatforma}">
                <button type="submit" class="danger">Ukloni</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </table>

      <form method="post" action="${pageContext.request.contextPath}/admin/igre">
        <input type="hidden" name="akcija" value="dodajPlatformu">
        <input type="hidden" name="idIgra" value="${igraZaIzmenu.idIgra}">
        <label>Platforma
          <select name="idPlatforma" required>
            <c:forEach var="p" items="${platforme}">
              <option value="${p.idPlatforma}">${p.naziv}</option>
            </c:forEach>
          </select>
        </label>
        <label>Datum portovanja <input type="date" name="datumPortovanja"></label>
        <label><input type="checkbox" name="ekskluzivno"> Ekskluzivno</label>
        <div style="margin-top:16px;"><button type="submit">Dodaj platformu</button></div>
      </form>
    </c:when>
    <c:otherwise>
      <h2>Nova igra</h2>
      <form method="post" action="${pageContext.request.contextPath}/admin/igre">
        <input type="hidden" name="akcija" value="kreiraj">
        <label>Naziv <input type="text" name="naziv" required></label>
        <label>Opis <textarea name="opis" rows="3"></textarea></label>
        <label>Cena <input type="number" step="0.01" min="0" name="cena" required></label>
        <label>Godina izdanja <input type="number" name="godinaIzdanja"></label>
        <label>Žanr <input type="text" name="zanr"></label>
        <label>Količina na stanju <input type="number" min="0" name="kolicinaNaStanju" required></label>
        <label>Izdavač
          <select name="idIzdavac">
            <option value="">—</option>
            <c:forEach var="iz" items="${izdavaci}">
              <option value="${iz.idIzdavac}">${iz.naziv}</option>
            </c:forEach>
          </select>
        </label>
        <div style="margin-top:16px;"><button type="submit">Sačuvaj</button></div>
      </form>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="_footer.jspf" %>
