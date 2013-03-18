<%@ page pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/haksvn.tld" prefix="haksvn" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="EXPIRES" content="-1">
	<meta http-equiv="Pragma" content="no-cache" />
	<title><tiles:getAsString name="title" /></title>
	
	<link rel="stylesheet" href="<c:url value="/css/style.css"/>" type="text/css"	media="screen" />
	<link rel="stylesheet" href="<c:url value="/css/haksvn.css"/>" type="text/css"	media="screen" />
	<link rel="stylesheet" href="<c:url value="/css/jquery-ui-1.9.2.custom.css"/>" type="text/css"	media="screen" />
	<link rel="stylesheet" href="<c:url value="/css/ui.dynatree.css"/>" type="text/css"	media="screen" />
	<script src="<c:url value="/js/jquery-1.8.3.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.blockUI.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.form.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.cookie.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.validate.min.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery-ui-1.9.2.custom.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/jquery.dynatree-1.2.4.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/global.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/modal.js"/>" type="text/javascript" charset="utf-8"></script>
	<script src="<c:url value="/js/haksvn.js"/>" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$.ajaxSetup({ 
			cache : false,
			error : function(jqXHR, textStatus, errorThrown) {
				var errorMsg = jqXHR.responseText !== '' ? jqXHR.responseText:errorThrown;
				$().Message({type:'error', text: textStatus + " : " + jqXHR.status + " : " + errorMsg});
		    },
		    complete : function( jqXHR ,textStatus ){
		    	$.unblockUI();
		    	$('#loader-main').hide();
		    },
		    beforeSend: function(){
		    	$('#loader-main').show();
		    }
		});
		$(function() {
			$('select.all').each(function(){
				var o = new Option("All", "");
				$(o).html("All");
				$(this).prepend(o);
			});
		});
	</script>
</head>