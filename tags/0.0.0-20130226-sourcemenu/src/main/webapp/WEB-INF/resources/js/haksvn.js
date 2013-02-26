
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