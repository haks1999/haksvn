
/*
 * prototype
 */
String.prototype.trim = function() {
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};

/*
 * jquery extend
 */
jQuery.extend({postJSON: function(url, data, callback){$.post(url, data, callback, "json");}});

jQuery.fn.toggleOption = function (show) {
    $(this).toggle(show);
    if (show) {
        if ($(this).parent('span.toggleOption').length)
            $(this).unwrap();
    } else {
        if ($(this).parent('span.toggleOption').length==0)
            $(this).wrap('<span class="toggleOption" style="display: none;" />');
    }
};

/*
 * functions
 */
function ajaxProcessing(){
	$.blockUI({ css: { 
        border: 'none', 
        padding: '15px', 
        backgroundColor: '#000', 
        '-webkit-border-radius': '10px', 
        '-moz-border-radius': '10px', 
        opacity: .5, 
        color: '#fff' 
    } }); 
}

var haksvn = {};

haksvn.constants = {
	ONE_DAY_MS : 24*60*60*1000,
	DIFF_DAY : ['Today','Yesterday']
};

haksvn.json = {
	stringfy: function( obj ){
		var t = typeof (obj);
	    if (t != "object" || obj === null) {
	        // simple data type
	        if (t == "string") obj = '"'+obj+'"';
	        return String(obj);
	    }
	    else {
	        // recurse array or object
	        var n = {}, v, json = [], arr = (obj && obj.constructor == Array);
	        for (n in obj) {
	            v = obj[n]; t = typeof(v);
	            if (t == "string") v = '"'+v+'"';
	            else if (t == "object" && v !== null) v = JSON.stringify(v);
	            json.push((arr ? "" : '"' + n + '":') + String(v));
	        }
	        return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
	    }
	}	
};

haksvn.block = {
	on: function(){
		$('body').addClass('blocking');
	},
	off: function(){
		$('body').removeClass('blocking');
	}
};

haksvn.date = {
	convertToEasyFormat: function( date ){
		var today_ms = new Date().getTime();
	    var date_ms = date.getTime();
	    var diff_ms = Math.abs(today_ms - date_ms);
	    var diff_day = Math.round(diff_ms/haksvn.constants.ONE_DAY_MS);
	    if( diff_day < 2 ) return haksvn.constants.DIFF_DAY[diff_day];
	    if( diff_day < 7 ) return diff_day + ' days ago';
	    var month = date.getMonth()+1;
	    var day = date.getDate();
	    return date.getFullYear()+"/"+ (month < 10?'0':'') + month  + "/"+ (day < 10?'0':'') + day;
	},
	convertToComplexFullFormat: function( date ){
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hour = date.getHours();
		var minute = date.getMinutes();
		var formatted = date.getFullYear()+"/"+(month < 10?'0':'') + month  + "/"+ (day < 10?'0':'') + day + " " + (hour < 10?'0':'') + hour + ":" + (minute < 10?'0':'') + minute;
		var today_ms = new Date().getTime();
	    var date_ms = date.getTime();
	    var diff_ms = Math.abs(today_ms - date_ms);
	    var diff_day = Math.round(diff_ms/haksvn.constants.ONE_DAY_MS);
	    if( diff_day < 2 ){
	    	formatted += " (" +haksvn.constants.DIFF_DAY[diff_day] + ")";
	    }else if( diff_day < 7 ){
	    	formatted += " (" +diff_day + ' days ago)';
	    }
	    return formatted;
	}
};