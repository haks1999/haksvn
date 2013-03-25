
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