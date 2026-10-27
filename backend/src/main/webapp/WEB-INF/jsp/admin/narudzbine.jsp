<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="_header.jspf" %>
<h1>Narudžbine</h1>

<table>
  <tr><th>ID</th><th>Datum</th><th>Kupac</th><th>Ukupna cena</th><th>Stavke</th><th>Status</th><th></th></tr>
  <c:forEach var="n" items="${narudzbine}">
    <tr>
      <td>${n.idNarudzbina}</td>
      <td>${n.datum}</td>
      <td>${n.kupac.korisnickoIme}</td>
      <td>${n.ukupnaCena}</td>
      <td>
        <c:forEach var="s" items="${n.stavke}">
          ${s.igra.naziv} x${s.kolicina}<br>
        </c:forEach>
      </td>
      <td>
        <form class="inline" method="post" action="${pageContext.request.contextPath}/admin/narudzbine">
          <input type="hidden" name="id" value="${n.idNarudzbina}">
          <select name="status">
            <c:forEach var="s" items="${statusi}">
              <option value="${s}" ${s == n.status ? 'selected' : ''}>${s}</option>
            </c:forEach>
          </select>
          <button type="submit">Sačuvaj</button>
        </form>
      </td>
      <td>
        <a class="btn" href="${pageContext.request.contextPath}/api/narudzbine/pdf/${n.idNarudzbina}">Preuzmi PDF</a>
      </td>
    </tr>
  </c:forEach>
</table>

<%@ include file="_footer.jspf" %>
