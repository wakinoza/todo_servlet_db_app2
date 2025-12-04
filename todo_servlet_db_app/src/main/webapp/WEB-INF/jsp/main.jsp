<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<%@ page import="model.TodoItem, model.Progress, java.util.List" %>
<% 
String errorMsg = (String )request.getAttribute("errorMsg");
String pending = "未実施";
String in_progress = "実施中";
String completed = "完了済";
%>

<h1>Todo管理メインページ</h1>
<p>
<c:out value="${loginUser.name}" />さん、ログイン中
<a href="Logout">ログアウトする</a>
</p>

<form action="Main?action=make" method="post">
  <input type="text" name="text">
  <input type="submit" value="Todoを新規追加">
</form>

<c:if test="not empty errorMsg">
  <p><%= errorMsg %></p>
</c:if>

 <c:choose>
  <c:when test="${empty todoItemList}">
   <p>Todoはありません</p>
  </c:when>
  <c:otherwise>
  　<div style="margin: 5px; background-color:#1760a0; color:white; padding-left: 10px;">
    　<h2　style="margin: 0;">Todo一覧</h2>
    </div>
  </c:otherwise>
</c:choose>

<c:forEach var="todoItem" items="${todoItemList}">
<div style="margin: 5px; border-bottom: 1px solid #ccc; padding-bottom: 5px;">
   <c:choose>
      <c:when test="${todoItem.progress == 'PENDING'}"> 
        <span style="margin-right: 15px;">
          <%= pending %> : ${todoItem.text}
        </span>
        <form action="Main" method="post" style="display:inline-block;">
          <input type="hidden" name="id" value="${todoItem.id}">
          <button type="submit" style="background:blue; border:none; border-radius:5px; color:white; cursor:pointer; padding:5px;">進捗を更新</button>
        </form>
      </c:when>

      <c:when test="${todoItem.progress == 'IN_PROGRESS'}"> 
        <span style="margin-right: 15px;">
          <%= in_progress %> : ${todoItem.text}
        </span>
        <form action="Main" method="post" style="display:inline-block;">
          <input type="hidden" name="id" value="${todoItem.id}">
          <button type="submit" style="background:blue; border:none; border-radius:5px; color:white; cursor:pointer; padding:5px; ">進捗を更新</button>
        </form>
      </c:when>
      
      <c:otherwise>
        <span style="margin-right: 15px;">
          <%= completed %> : ${todoItem.text}
        </span>       
        <form action="Main" method="post" style="display:inline-block;">
          <input type="hidden" name="id" value="${todoItem.id}">
          <button type="submit" style="background:orange; border:none; border-radius:5px; cursor:pointer; padding:5px; ">todoを削除</button>
        </form>
      </c:otherwise>
    </c:choose>
  </div>
</c:forEach>

<%@ include file="footer.jsp" %>