<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="table" class="help">
	<div class="col w10 last">
		<div class="content">
			<table>
				<tr>
					<th>seq</th>
					<th>type</th>
					<th>description</th>
					<th>request date</th>
					<th>result</th>
					<th>transfer date</th>
				</tr>
				<c:forEach items="${requestList }">
					<tr id="id_1">
						<td><c:out value="${transferSeq }" /></td>
						<td><c:out value="${requestType }" /></td>
						<td><c:out value="${description }" /></td>
						<td><c:out value="${requestDate }" /></td>
						<td><c:out value="${requestResult }" /></td>
						<td><c:out value="${transferDate }" /></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<a href="<c:url value="/transfer/request/createRequest"  />" class="button green right"><span>regist</span></a>
	<div class="clear"></div>
</div>