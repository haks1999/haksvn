<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/haksvn.tld" prefix="haksvn" %>
<form>	
	<div class="col w3">
		<p>
			<label for="input" class="left">Description</label>
			<input type="text" class="text w_20" />
		</p>
	</div>
	<div class="col w3">
		<p>
			<label for="input" class="left">RequestType</label>
			<haksvn:select name="requestType" codeGroup="request.type" selectedValue="${request.requestType}" ></haksvn:select>
		</p>
	</div>
	<p>
		<label class="left"></label>
		<a class="button green mt ml form_submit"><small class="icon check"></small><span>Confirm</span></a>
		<a class="button red mt ml"><small class="icon cross"></small><span>Cancel</span></a>
	</p>
</form>